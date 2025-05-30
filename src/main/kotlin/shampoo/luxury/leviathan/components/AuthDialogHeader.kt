package shampoo.luxury.leviathan.components

import androidx.compose.runtime.Composable
import xyz.malefic.compose.comps.text.typography.Heading6

/**
 * A composable function that displays the header of the authentication dialog.
 *
 * @param isLogin A boolean indicating whether the dialog is in login mode.
 */
@Composable
fun AuthDialogHeader(isLogin: Boolean) = Heading6(if (isLogin) "Log In" else "Sign Up")
