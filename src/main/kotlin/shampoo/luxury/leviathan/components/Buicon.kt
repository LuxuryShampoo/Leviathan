package shampoo.luxury.leviathan.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.SolidGroup
import xyz.malefic.ext.modifier.modifyIf

/**
 * A composable function that creates a button with an icon, styled for compact and customizable usage.
 *
 * @param imageVector A lambda that provides the desired icon from the `SolidGroup`.
 * @param contentDescription A description of the icon for accessibility purposes.
 * @param size The size of the icon in Dp. Defaults to 32.dp.
 * @param hitBox The size of the clickable area (hitbox) in Dp. Defaults to 64.dp.
 * @param unbounded A flag indicating whether the icon's size is unbounded. Defaults to false.
 * @param modifier A `Modifier` to apply to the outer `Box`. Defaults to `Modifier.Companion`.
 * @param onClick A lambda function to be executed when the button is clicked.
 */
@Composable
fun Buicon(
    imageVector: SolidGroup.() -> ImageVector,
    contentDescription: String,
    size: Dp = 32.dp,
    hitBox: Dp = 64.dp,
    unbounded: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.size(hitBox),
        colors =
            ButtonDefaults.outlinedButtonColors(
                backgroundColor = Transparent,
                contentColor = MaterialTheme.colors.onBackground,
            ),
        contentPadding = PaddingValues(0.dp), // Remove default padding
    ) {
        Icon(
            imageVector = imageVector(FontAwesomeIcons.Solid),
            contentDescription = contentDescription,
            modifier =
                Modifier
                    .size(size)
                    .modifyIf(!unbounded, Modifier.wrapContentSize(Center)),
            tint = MaterialTheme.colors.onBackground,
        )
    }
}
