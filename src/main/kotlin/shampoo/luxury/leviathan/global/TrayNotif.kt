package shampoo.luxury.leviathan.global

import androidx.compose.ui.window.Notification
import xyz.malefic.Signal

/**
 * Object responsible for managing tray notifications in the application.
 *
 * This object provides a signal mechanism to handle notifications, allowing
 * other parts of the application to send and receive notifications through
 * a centralized system.
 */
object TrayNotif {
    /**
     * A signal used to emit and observe notifications.
     *
     * @property notifSignal A `Signal` object that emits `Notification` instances.
     * Other components can subscribe to this signal to react to notifications.
     */
    val notifSignal = Signal<Notification>()
}
