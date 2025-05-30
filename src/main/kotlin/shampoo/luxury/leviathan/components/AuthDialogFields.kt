package shampoo.luxury.leviathan.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    TextField(
        username,
        onUsernameChange,
        Modifier.fillMaxWidth(),
        label = { Body2("Username") },
    )
    TextField(
        password,
        onPasswordChange,
        Modifier.fillMaxWidth(),
        label = { Body2("Password") },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    )
}
