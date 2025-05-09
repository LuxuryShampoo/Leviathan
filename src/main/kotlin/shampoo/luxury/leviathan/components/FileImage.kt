package shampoo.luxury.leviathan.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ContentScale.Companion.Fit
import java.io.File

/**
 * A composable function that displays an image from a file.
 *
 * @param file The file containing the image to be displayed.
 * @param contentDescription A description of the image for accessibility purposes.
 * @param modifier A lambda to apply additional modifiers to the image.
 * @param alignment The alignment of the image within its bounds. Defaults to [Alignment.Center].
 * @param contentScale Defines how the image should be scaled to fit its bounds. Defaults to [ContentScale.Fit].
 * @param alpha The opacity of the image, where 1.0 is fully opaque and 0.0 is fully transparent. Defaults to [DefaultAlpha].
 * @param colorFilter An optional color filter to apply to the image. Defaults to null.
 * @param filterQuality The quality of the image filtering. Defaults to [DefaultFilterQuality].
 */
@Composable
fun FileImage(
    file: File,
    contentDescription: String?,
    alignment: Alignment = Center,
    contentScale: ContentScale = Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DefaultFilterQuality,
    modifier: Modifier.() -> Modifier = { Modifier },
) = Image(
    file.inputStream().readAllBytes().decodeToImageBitmap(),
    contentDescription,
    Modifier.modifier(),
    alignment,
    contentScale,
    alpha,
    colorFilter,
    filterQuality,
)
