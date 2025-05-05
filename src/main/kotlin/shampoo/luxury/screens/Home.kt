package shampoo.luxury.screens

import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Cog
import compose.icons.fontawesomeicons.solid.QuestionCircle
import moe.tlaster.precompose.navigation.Navigator
import xyz.malefic.compose.comps.text.typography.Body1
import xyz.malefic.compose.comps.text.typography.Heading1

@Composable
fun Home(navi: Navigator) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
        horizontalAlignment = CenterHorizontally,
    ) {
        TopRow()
        Divider()
        MiddleBox()
        Divider()
        BottomRow(navi)
    }
}

@Composable
private fun TopRow() {
    Row(
        modifier =
            Modifier
                .fillMaxHeight(0.2f)
                .fillMaxWidth(),
        horizontalArrangement = SpaceEvenly,
        verticalAlignment = CenterVertically,
    ) {
        Body1("Hello, World!")
        Button(onClick = {
            println("Button clicked!")
        }) {
            Body1("Button")
        }
    }
}

@Composable
private fun MiddleBox() {
    Box(
        modifier =
            Modifier
                .fillMaxHeight(0.75f)
                .fillMaxWidth(),
        contentAlignment = Center,
    ) {
        Heading1("Character TEST")
    }
}

@Composable
private fun BottomRow(navi: Navigator) {
    Row(
        modifier =
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
        horizontalArrangement = SpaceEvenly,
        verticalAlignment = CenterVertically,
    ) {
        Buicon(FontAwesomeIcons.Solid.Cog, "Settings") { navi.navigate("settings") }
        Buicon(FontAwesomeIcons.Solid.QuestionCircle, "Help") { println("Help clicked!") }
    }
}

@Composable
fun Buicon(
    imageVector: ImageVector,
    contentDescription: String,
    size: Dp = 32.dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors =
            ButtonDefaults.buttonColors(
                backgroundColor = Transparent,
                contentColor = MaterialTheme.colors.onBackground,
            ),
        modifier = Modifier.size(64.dp),
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier.size(size).wrapContentSize(align = Center).then(modifier),
        )
    }
}
