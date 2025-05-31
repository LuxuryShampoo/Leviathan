package shampoo.luxury.leviathan.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Palette
import shampoo.luxury.leviathan.theme.Theme.Companion.fromPath
import shampoo.luxury.leviathan.theme.ThemeManager.currentThemePath
import shampoo.luxury.leviathan.theme.ThemeManager.updateTheme
import xyz.malefic.compose.comps.text.typography.Body1
import xyz.malefic.compose.comps.text.typography.ColorType.OnSecondary
import xyz.malefic.compose.comps.text.typography.ColorType.OnSurface

@Composable
fun ThemeSelector() {
    var expanded by remember { mutableStateOf(false) }
    var selectedTheme by remember { mutableStateOf(fromPath(currentThemePath)) }

    Row(
        verticalAlignment = CenterVertically,
    ) {
        Icon(
            FontAwesomeIcons.Solid.Palette,
            "Theme",
            Modifier
                .size(20.dp)
                .padding(end = 4.dp),
            MaterialTheme.colors.onBackground,
        )

        Body1(
            "Theme",
            Modifier.padding(end = 8.dp),
        )

        Surface(
            Modifier.clickable { expanded = true },
            border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.12f)),
        ) {
            Row(
                verticalAlignment = CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Body1(selectedTheme.displayName, colorType = OnSurface)
                Body1(" ▼", colorType = OnSurface)
            }
        }

        DropdownMenu(
            expanded,
            { expanded = false },
        ) {
            Theme.entries.forEach { theme ->
                Surface(color = MaterialTheme.colors.surface) {
                    DropdownMenuItem(
                        {
                            selectedTheme = theme
                            expanded = false

                            updateTheme(theme.filePath).invokeOnCompletion {
                                Logger.d("Selected theme: ${theme.displayName}, file: ${theme.filePath}")
                            }
                        },
                    ) {
                        Body1(theme.displayName, colorType = OnSecondary)
                    }
                }
            }
        }
    }
}
