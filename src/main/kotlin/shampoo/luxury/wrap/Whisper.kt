package shampoo.luxury.wrap

import io.github.givimad.whisperjni.WhisperFullParams
import io.github.givimad.whisperjni.WhisperJNI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.malefic.Signal
import java.nio.file.Path
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.TargetDataLine
import kotlin.math.absoluteValue

/**
 * A wrapper class for the WhisperJNI speech-to-text engine.
 * This class listens for a wake word, starts transcription upon detection,
 * and stops transcription after detecting silence for a specified duration.
 *
 * @property modelPath The path to the Whisper model file.
 * @property wakeWord The wake word to listen for.
 * @property silenceThreshold The duration of silence (in milliseconds) to stop transcription.
 */
class Whisper(
    private val modelPath: Path,
    private val wakeWord: String,
    private val silenceThreshold: Int = 2000,
) {
    private val transcriptSignal = Signal<String>()
    private var targetDataLine: TargetDataLine? = null
    private val whisper = WhisperJNI()
    private val ctx = whisper.init(modelPath)

    init {
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
        val buffer = ByteArray(2048) // Buffer to hold raw audio data.
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
        val result = whisper.full(ctx, params, audioBatch.toFloatArray(), audioBatch.size)
        audioBatch.clear()
        if (result != 0) {
            throw RuntimeException("Transcription failed with code $result")
        }
        return true
    }

    /**
     * Detects if the wake word is present in the transcribed segments.
     *
     * @return True if the wake word is detected, false otherwise.
     */
    private fun detectWakeWord(): Boolean {
        val numSegments = whisper.fullNSegments(ctx)
        for (i in 0 until numSegments) {
            val text = whisper.fullGetSegmentText(ctx, i)
            if (text.contains(wakeWord, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    /**
     * Detects if the audio is silent based on the average amplitude.
     *
     * @param silenceBuffer The buffer containing audio data to analyze.
     * @return True if the audio is silent, false otherwise.
     */
    private fun detectSilence(silenceBuffer: List<Float>): Boolean {
        val averageAmplitude = silenceBuffer.map { it.absoluteValue }.average()
        return averageAmplitude < 0.01
    }

    /**
     * Emits the final transcript by concatenating all transcribed segments.
     */
    private suspend fun emitTranscript() {
        val numSegments = whisper.fullNSegments(ctx)
        val transcript =
            buildString {
                (0 until numSegments).forEach { i ->
                    append(whisper.fullGetSegmentText(ctx, i))
                }
            }
        transcriptSignal.emit(transcript.toString())
    }

    /**
     * Starts listening for the wake word and transcribes audio after detection.
     * Stops transcription after detecting silence for the specified duration.
     *
     * @param scope The coroutine scope in which the listening process runs.
     */
    fun startListening(scope: CoroutineScope) {
        scope.launch(Dispatchers.IO) {
            val params = WhisperFullParams()
            var isTranscribing = false
            val silenceBuffer = mutableListOf<Float>()
            val audioBatch = mutableListOf<Float>()

            while (true) {
                val audioFrame = getNextAudioFrame()
                audioBatch.addAll(audioFrame.toList())

                if (audioBatch.size < 16000) continue

                processAudioBatch(audioBatch, params)

                when {
                    !isTranscribing && detectWakeWord() -> {
                        isTranscribing = true
                        silenceBuffer.clear()
                    }
                    isTranscribing -> {
                        silenceBuffer.addAll(audioFrame.toList())
                        if (silenceBuffer.size > silenceThreshold * 16 && detectSilence(silenceBuffer)) {
                            emitTranscript()
                            isTranscribing = false
                            silenceBuffer.clear()
                        }
                    }
                }
            }
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
     * Registers a listener to receive transcribed text.
     * This version does not require a coroutine scope.
     *
     * @param action The action to perform on each emitted transcript.
     */
    fun onTranscript(action: suspend (String) -> Unit) {
        transcriptSignal.connect(action)
    }

    /**
     * Releases resources used by WhisperJNI and the audio input line.
     */
    fun close() {
        ctx.close()
        targetDataLine?.close()
    }
}
