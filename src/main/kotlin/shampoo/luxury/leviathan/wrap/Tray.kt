package shampoo.luxury.leviathan.wrap

import androidx.compose.ui.window.Notification.Type.Error
import androidx.compose.ui.window.Notification.Type.Info
import androidx.compose.ui.window.Notification.Type.None
import androidx.compose.ui.window.Notification.Type.Warning
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import shampoo.luxury.leviathan.global.Resource.extractResourceToLocal
import shampoo.luxury.leviathan.global.TrayNotif.notifSignal
import java.awt.*
import java.awt.TrayIcon.MessageType.ERROR
import java.awt.TrayIcon.MessageType.INFO
import java.awt.TrayIcon.MessageType.WARNING
import kotlin.system.exitProcess

fun setupTrayIcon(scope: CoroutineScope) {
    if (!SystemTray.isSupported()) {
        Logger.e("SystemTray is not supported")
        return
    }

    val tray = SystemTray.getSystemTray()
    val image = Toolkit.getDefaultToolkit().getImage(extractResourceToLocal("images/Phat.png").absolutePath)

    val startListening = MenuItem("Start Listening")
    val stopListening = MenuItem("Stop Listening")
    val exit = MenuItem("Exit")

    val popup = PopupMenu()
    popup.add(startListening)
    popup.add(stopListening)
    popup.addSeparator()
    popup.add(exit)

    val trayIcon = TrayIcon(image, "Leviathan Whisper", popup)
    trayIcon.isImageAutoSize = true

    notifSignal.connect {
        Logger.d("Received notification: ${it.title} - ${it.message} - ${it.type}")
        trayIcon.displayMessage(
            it.title,
            it.message,
            when (it.type) {
                Info -> INFO
                Warning -> WARNING
                Error -> ERROR
                None -> INFO
            },
        )
    }

    startListening.addActionListener {
        scope.launch {
            Whisper.listen(scope)
            Whisper.onTranscript(scope) { transcript ->
                val aiResponse =
                    parse("This was a transcript from a speech to text so there may be some errors. Here's your prompt: $transcript")
                speak(aiResponse)
                trayIcon.displayMessage("Whisper", "Transcript processed and spoken", INFO)
            }
            trayIcon.displayMessage("Whisper", "Listening started", INFO)
        }
    }

    stopListening.addActionListener {
        Whisper.close()
        trayIcon.displayMessage("Whisper", "Listening stopped", INFO)
    }

    exit.addActionListener {
        Whisper.close()
        tray.remove(trayIcon)
        exitProcess(0)
    }

    tray.add(trayIcon)
}
