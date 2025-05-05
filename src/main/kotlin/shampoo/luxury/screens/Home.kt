package shampoo.luxury.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Cog
import compose.icons.fontawesomeicons.solid.QuestionCircle
import moe.tlaster.precompose.navigation.Navigator
import xyz.malefic.compose.comps.text.typography.Body1
import xyz.malefic.compose.comps.text.typography.Heading1
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource

@Composable
fun Home(navi: Navigator) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = CenterHorizontally,
    ) {
        TopRow()
        Divider()
        MiddleBox()
        Divider()
        BottomRow()
    }
}

@Composable
private fun TopRow() {
    Row(
        modifier = Modifier
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
        val expanded = remember { mutableStateOf(false) }
        Box {
            Button(onClick = { expanded.value = true }) {
                Body1("Menu")
            }
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                DropdownMenuItem(
                    onClick = {
                        println("Option 1")
                        expanded.value = false
                    }
                ) {
                    Body1("Option 1")
                }
                DropdownMenuItem(
                    onClick = {
                        println("Option 2")
                        expanded.value = false
                    }
                ) {
                    Body1("Option 2")
                }
            }
        }
    }
}

@Composable
private fun MiddleBox() {
    Box(
        modifier = Modifier
            .fillMaxHeight(0.75f)
            .fillMaxWidth(),
        contentAlignment = Center,
    ) {
        Column(
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource("src/main/resources/bobtest.png"),
                contentDescription = "Character Image",
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Heading1("Character TEST")
        }
    }
}

@Composable
private fun BottomRow() {
    Row(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        horizontalArrangement = SpaceEvenly,
        verticalAlignment = CenterVertically,
    ) {
        Buicon(FontAwesomeIcons.Solid.Cog, "Settings") { println("Settings clicked!") }
        Buicon(FontAwesomeIcons.Solid.QuestionCircle, "Help") { println("Help clicked!") }
    }
}

@Composable
private fun Buicon(
    imageVector: ImageVector,
    desc: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = MaterialTheme.colors.onBackground,
        ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
        ),
        modifier = Modifier.size(64.dp),
        contentPadding = PaddingValues(0.dp),
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = desc,
            modifier = Modifier.size(96.dp).wrapContentSize(align = Center),
        )
    }
}