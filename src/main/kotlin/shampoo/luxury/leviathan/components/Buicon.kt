package shampoo.luxury.leviathan.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme.colors
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

@Composable
fun Buicon(
    imageVector: SolidGroup.() -> ImageVector,
    contentDescription: String,
    iconSize: Dp = 32.dp,
    hitBox: Dp = 64.dp,
    unbounded: Boolean = false,
    modifier: Modifier = Modifier,
    outlined: Boolean = true,
    onClick: () -> Unit,
) {
    val buttonColors =
        ButtonDefaults.outlinedButtonColors(Transparent, colors.onBackground)
    val content: @Composable RowScope.() -> Unit = {
        Icon(
            imageVector(FontAwesomeIcons.Solid),
            contentDescription,
            Modifier
                .size(iconSize)
                .modifyIf(!unbounded, Modifier.wrapContentSize(Center)),
            colors.onBackground,
        )
    }
    if (outlined) {
        OutlinedButton(
            onClick,
            modifier.size(hitBox),
            colors = buttonColors,
            contentPadding = PaddingValues(0.dp),
            content = content,
        )
    } else {
        Button(
            onClick,
            modifier.size(hitBox),
            colors = buttonColors,
            contentPadding = PaddingValues(0.dp),
            elevation = null,
            content = content,
        )
    }
}
