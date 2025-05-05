package shampoo.luxury.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Palette
import kotlinx.coroutines.launch
import xyz.malefic.compose.comps.text.typography.Body1

@Composable
fun ThemeSelector() {
    var expanded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val initialTheme = Theme.fromPath(ThemeManager.getCurrentThemePath())
    var selectedTheme by remember { mutableStateOf(initialTheme) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = FontAwesomeIcons.Solid.Palette,
            contentDescription = "Theme",
            modifier =
                Modifier
                    .size(20.dp)
                    .padding(end = 4.dp),
            tint = MaterialTheme.colors.onBackground,
        )

        Body1(
            "Theme",
            Modifier.padding(end = 8.dp),
        )

        Surface(
            border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.12f)),
            modifier = Modifier.clickable { expanded = true },
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Body1(selectedTheme.displayName)
                Body1(" ▼")
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            Theme.entries.forEach { theme ->
                DropdownMenuItem(
                    onClick = {
                        selectedTheme = theme
                        expanded = false

                        coroutineScope.launch {
                            val success = ThemeManager.updateTheme(theme.filePath)
                            println("Selected theme: ${theme.displayName}, file: ${theme.filePath}, success: $success")
                        }
                    },
                ) {
                    Text(text = theme.displayName)
                }
            }
        }
    }
}
