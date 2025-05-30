package shampoo.luxury.leviathan.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import compose.icons.fontawesomeicons.SolidGroup
import compose.icons.fontawesomeicons.solid.Bars

@Composable
fun Burger() {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Buicon(
            { SolidGroup.Bars },
            "Menu",
            32.dp,
            48.dp,
        ) { expanded = true }
        DropdownMenu(
            expanded,
            { expanded = false },
        ) {
            DropdownText("Option 1") {
                Logger.i("Option 1")
                expanded = false
            }
            DropdownText("Option 2") {
                Logger.i("Option 2")
                expanded = false
            }
        }
    }
}
