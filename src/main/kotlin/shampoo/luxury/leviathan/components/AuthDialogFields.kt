package shampoo.luxury.leviathan.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import xyz.malefic.compose.comps.text.typography.Body2

/**
 * A composable function that displays the input fields for username and password.
 *
 * @param username The current value of the username input field.
 * @param password The current value of the password input field.
 * @param onUsernameChange A callback function invoked when the username changes.
 * @param onPasswordChange A callback function invoked when the password changes.
 */
@Composable
fun AuthDialogFields(
    username: String,
    password: String,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
) {
    val usernameFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    TextField(
        username,
        onUsernameChange,
        Modifier
            .fillMaxWidth()
            .focusRequester(usernameFocusRequester)
            .onPreviewKeyEvent { event ->
                if (event.key == Key.Tab && event.type == KeyEventType.KeyDown) {
                    passwordFocusRequester.requestFocus()
                    true
                } else {
                    false
                }
            },
        label = { Body2("Username") },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
        keyboardActions =
            KeyboardActions(
                onNext = { passwordFocusRequester.requestFocus() },
            ),
    )

    TextField(
        password,
        onPasswordChange,
        Modifier
            .fillMaxWidth()
            .focusRequester(passwordFocusRequester),
        label = { Body2("Password") },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
        keyboardActions =
            KeyboardActions(
                onDone = { focusManager.clearFocus() },
            ),
    )
}
