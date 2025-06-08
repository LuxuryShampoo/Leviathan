package shampoo.luxury.leviathan.wrap

import co.touchlab.kermit.Logger
import io.github.givimad.whisperjni.WhisperContext
import io.github.givimad.whisperjni.WhisperFullParams
import io.github.givimad.whisperjni.WhisperJNI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import shampoo.luxury.leviathan.global.Resource.extractResourceToLocal
import shampoo.luxury.leviathan.global.Values.Prefs.getListenSetting
import xyz.malefic.Signal
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.TargetDataLine

/**
 * A singleton object for the WhisperJNI speech-to-text engine.
 * This object listens for a wake word, starts transcription upon detection,
 * and stops transcription after detecting silence for a specified duration.
 */
object Whisper {
    private var isClosed = false
    private lateinit var wakeWord: String
    private var modelPath =
        extractResourceToLocal("files/model/ggml-tiny.en.bin").toPath().also {
            Logger.d("Whisper") { "Model path: $it" }
        }
    private var silenceThreshold = 2000
    private val transcriptSignal = Signal<String>()
    private var targetDataLine: TargetDataLine? = null
    private var whisper: WhisperJNI
    private var ctx: WhisperContext

    init {
        try {
            WhisperJNI.loadLibrary()
            WhisperJNI.setLibraryLogger { text -> Logger.d("WhisperJNI") { text } }

            whisper = WhisperJNI()

            val systemInfo = whisper.systemInfo
            Logger.d("Whisper") { "Native system info:\n$systemInfo" }

            ctx =
                requireNotNull(
                    whisper.init(modelPath),
                ) { "Failed to initialize Whisper context. Check if model file exists and system info above." }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    /**
     * Initializes the Whisper object with the required parameters.
     *
     * @param wakeWord The wake word to listen for.
     * @param silenceThreshold The duration of silence (in milliseconds) to stop transcription.
     */
    fun initialize(
        wakeWord: String,
        silenceThreshold: Int = 2000,
    ) {
        this.wakeWord = wakeWord
        this.silenceThreshold = silenceThreshold
        setupAudioLine()
    }

    /**
     * Configures and starts the audio input line for capturing microphone data.
     * Throws an exception if the audio line is not supported.
     */
    private fun setupAudioLine() {
        val audioFormat = AudioFormat(16000f, 16, 1, true, false) // 16kHz, 16-bit, mono, signed, little-endian
        val info = DataLine.Info(TargetDataLine::class.java, audioFormat)
        check(AudioSystem.isLineSupported(info)) {
            "Audio line not supported"
        }
        targetDataLine = AudioSystem.getLine(info) as TargetDataLine
        targetDataLine?.open(audioFormat)
        targetDataLine?.start()
    }

    /**
     * Captures the next audio frame from the microphone.
     * Converts the captured byte array into a float array for processing.
     *
     * @return A float array representing the audio frame.
     */
    private fun getNextAudioFrame(): FloatArray {
        val buffer = ByteArray(2048) // Buffer to hold raw audio data
        val bytesRead = targetDataLine?.read(buffer, 0, buffer.size) ?: 0
        val floatBuffer = FloatArray(bytesRead / 2)
        for (i in floatBuffer.indices) {
            floatBuffer[i] = ((buffer[2 * i + 1].toInt() shl 8) or (buffer[2 * i].toInt() and 0xFF)).toShort() / 32768f
        }
        return floatBuffer
    }

    /**
     * Processes a batch of audio data using WhisperJNI.
     * Clears the audio batch after processing.
     *
     * @param audioBatch The batch of audio data to process.
     * @param params The parameters for Whisper transcription.
     * @return True if processing is successful, otherwise throws an exception.
     */
    private fun processAudioBatch(
        audioBatch: MutableList<Float>,
        params: WhisperFullParams,
    ): Boolean {
        if (isClosed) return false
        val result = whisper.full(ctx, params, audioBatch.toFloatArray(), audioBatch.size)
        audioBatch.clear()
        if (result != 0) {
            throw RuntimeException("Transcription failed with code $result")
        }
        return true
    }

    /**
     * Processes the audio batch and logs the processing status.
     *
     * @param audioBatch The batch of audio data to process.
     * @param params The parameters for Whisper transcription.
     */
    private fun processAudio(
        audioBatch: MutableList<Float>,
        params: WhisperFullParams,
    ) {
        Logger.d("Processing audio batch of size: ${audioBatch.size}")
        processAudioBatch(audioBatch, params)
        Logger.d("Audio batch processed.")
    }

    /**
     * Handles transcription segments, updates silence count, and manages transcription state.
     *
     * @param numSegments The number of transcription segments.
     * @param isTranscribing The current transcription state.
     * @param transcriptBuffer The buffer to store meaningful transcription segments.
     * @param silenceCount The current count of consecutive silence segments.
     * @return A pair containing the updated transcription state and silence count.
     */
    private fun handleSegments(
        numSegments: Int,
        isTranscribing: Boolean,
        transcriptBuffer: MutableList<String>,
        silenceCount: Int,
        setter: (Boolean, Int) -> Unit,
    ) {
        var transcribing = isTranscribing
        var silence = silenceCount

        for (i in 0 until numSegments) {
            val text = whisper.fullGetSegmentText(ctx, i).trim()
            Logger.d("Segment text: $text")

            if (text.startsWith("[") && text.endsWith("]") || text.startsWith("(") && text.endsWith(")")) {
                silence++
                Logger.d("Silence count incremented: $silence")
            } else {
                silence = 0
                if (transcribing) {
                    transcriptBuffer.add(text)
                }
                if (!transcribing && text.contains(wakeWord, ignoreCase = true)) {
                    Logger.d("Wake word detected: $wakeWord")
                    transcribing = true
                    transcriptBuffer.clear()
                }
            }
        }

        setter(transcribing, silence)
    }

    /**
     * Emits the transcript if a prolonged silence is detected.
     *
     * This function checks the silence count and determines if the transcription
     * should be emitted. If the silence count exceeds the threshold and transcription
     * is ongoing, it emits the final transcript, clears the buffer, and resets the
     * transcription state. Otherwise, it updates the transcription state and silence count.
     *
     * @param isTranscribing Indicates whether transcription is currently active.
     * @param silenceCount The current count of consecutive silence segments.
     * @param transcriptBuffer A buffer containing the accumulated transcription segments.
     * @param setter A function to update the transcription state and silence count.
     */
    private suspend fun emitTranscriptIfSilent(
        isTranscribing: Boolean,
        silenceCount: Int,
        transcriptBuffer: MutableList<String>,
        setter: (Boolean, Int) -> Unit,
    ) {
        Logger.d("Checking silence count: $silenceCount, isTranscribing: $isTranscribing")
        if (isTranscribing && silenceCount >= 3) {
            Logger.d("Silence detected. Emitting transcript...")
            val finalTranscript = transcriptBuffer.joinToString(" ")
            transcriptSignal.emit(finalTranscript)
            transcriptBuffer.clear()
            setter(false, 0)
            return
        }
        setter(isTranscribing, silenceCount)
    }

    /**
     * Starts listening for audio input, processes transcription, and emits transcripts.
     *
     * @param scope The coroutine scope in which the listening process runs.
     * @param dispatcher The coroutine dispatcher for managing background tasks.
     */
    fun listen(
        scope: CoroutineScope,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) {
        if (isClosed) return
        scope.launch(dispatcher) {
            val params = WhisperFullParams()
            var isTranscribing = false
            var silenceCount = 0
            val audioBatch = mutableListOf<Float>()
            val transcriptBuffer = mutableListOf<String>()

            Logger.d("Listening started...")

            while (!isClosed) {
                if (!getListenSetting()) {
                    Logger.d("Listening preference is disabled. Skipping...")
                    continue
                }

                val audioFrame = getNextAudioFrame()
                audioBatch.addAll(audioFrame.toList())

                if (audioBatch.size < 48000) continue

                processAudio(audioBatch, params)

                handleSegments(whisper.fullNSegments(ctx), isTranscribing, transcriptBuffer, silenceCount) { bool, int ->
                    isTranscribing = bool
                    silenceCount = int
                }

                emitTranscriptIfSilent(isTranscribing, silenceCount, transcriptBuffer) { bool, int ->
                    isTranscribing = bool
                    silenceCount = int
                }
            }

            Logger.d("Listening stopped.")
        }
    }

    /**
     * Registers a listener to receive transcribed text.
     * The listener is invoked whenever a new transcript is emitted.
     *
     * @param scope The coroutine scope in which the listener runs.
     * @param action The action to perform on each emitted transcript.
     */
    fun onTranscript(
        scope: CoroutineScope,
        action: suspend (String) -> Unit,
    ) {
        transcriptSignal.connect(scope, action)
    }

    /**
     * Releases resources used by WhisperJNI and the audio input line.
     * Ensures that no further operations are performed after closure.
     */
    fun close() {
        if (isClosed) return
        isClosed = true
        whisper.free(ctx)
        targetDataLine?.close()
    }
}
