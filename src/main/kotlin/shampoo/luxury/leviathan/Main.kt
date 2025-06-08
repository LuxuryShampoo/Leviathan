package shampoo.luxury.leviathan

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition.Aligned
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import shampoo.luxury.leviathan.components.layouts.LoadingBox
import shampoo.luxury.leviathan.components.layouts.ThemedApp
import shampoo.luxury.leviathan.global.GlobalLoadingState.databaseLoaded
import shampoo.luxury.leviathan.wrap.Whisper
import shampoo.luxury.leviathan.wrap.data.currency.addToBalance
import shampoo.luxury.leviathan.wrap.setupTrayIcon
import xyz.malefic.compose.comps.precompose.NavWindow
import xyz.malefic.compose.nav.RouteManager
import xyz.malefic.compose.nav.config.MalefiConfigLoader
import xyz.malefic.ext.stream.grass
import java.awt.Toolkit.getDefaultToolkit
import kotlin.time.Duration.Companion.seconds

@OptIn(DelicateCoroutinesApi::class)
fun main() =
    application {
        val scope = rememberCoroutineScope { IO }
        val screenSize = getDefaultToolkit().screenSize
        val windowWidth = (screenSize.width * 0.3).dp
        val windowHeight = (screenSize.height * 0.7).dp

        scope.launch {
            while (scope.isActive) {
                if (databaseLoaded) {
                    addToBalance(5)
                    delay(5.seconds)
                }
            }
        }

        LaunchedEffect(Unit) {
            Whisper.initialize("please")
            setupTrayIcon(scope)
        }

        NavWindow(
            ::exitApplication,
            rememberWindowState(
                size = DpSize(windowWidth, windowHeight),
                position = Aligned(BottomEnd),
            ),
            title = "Leviathan",
        ) {
            ThemedApp {
                LoadingBox {
                    RouteManager.initialize(
                        composableMap,
                        grass("/routes.mcf")!!,
                        MalefiConfigLoader(),
                    )
                    NavigationMenu()
                }
            }
        }
    }
