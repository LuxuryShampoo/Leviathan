package shampoo.luxury.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
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
    size: Dp = 32.dp,
    hitBox: Dp = 64.dp,
    unbounded: Boolean = false,
    modifier: Modifier = Modifier.Companion,
    onClick: () -> Unit,
) {
    Box(
        contentAlignment = Center,
        modifier = modifier,
    ) {
        Button(
            onClick = onClick,
            colors =
                ButtonDefaults.buttonColors(
                    backgroundColor = Transparent,
                    MaterialTheme.colors.onBackground,
                ),
            modifier = Modifier.size(hitBox),
        ) {}
        Icon(
            imageVector = imageVector(FontAwesomeIcons.Solid),
            contentDescription = contentDescription,
            modifier = Modifier.size(size).modifyIf(!unbounded, Modifier.wrapContentSize(Center)),
            tint = MaterialTheme.colors.onBackground,
        )
    }
}
