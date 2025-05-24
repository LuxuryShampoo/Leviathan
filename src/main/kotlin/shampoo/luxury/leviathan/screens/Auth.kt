package shampoo.luxury.leviathan.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import co.touchlab.kermit.Logger
import shampoo.luxury.leviathan.global.Values.Prefs.prefs
import shampoo.luxury.leviathan.global.Values.user
import shampoo.luxury.leviathan.wrap.data.pets.initializePets
import shampoo.luxury.leviathan.wrap.data.users.checkPassword
import shampoo.luxury.leviathan.wrap.data.users.getUserIdByUsername
import shampoo.luxury.leviathan.wrap.data.users.insertUser
import xyz.malefic.compose.comps.text.typography.Body2
import xyz.malefic.compose.comps.text.typography.ColorType.Error
import xyz.malefic.compose.comps.text.typography.Heading6
import xyz.malefic.compose.prefs.delegate.BooleanPreference

/**
 * A composable function that displays an authentication dialog.
 * The dialog allows the user to either log in or sign up before accessing the application.
 *
 * @param onAuthenticated A callback function that is invoked when the user successfully authenticates.
 */
@Composable
fun AuthDialog(onAuthenticated: (String) -> Unit) {
    var isLogin by remember { mutableStateOf(true) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = {}) {
        Surface(
            Modifier.padding(16.dp),
            MaterialTheme.shapes.medium,
        ) {
            Column(
                Modifier.padding(16.dp),
                Arrangement.spacedBy(8.dp),
                Alignment.CenterHorizontally,
            ) {
                AuthDialogHeader(isLogin)
                AuthDialogFields(username, password, { username = it }, { password = it })
                errorMessage?.let { AuthDialogError(it) }
                AuthDialogActions(
                    isLogin,
                    {
                        isLogin = !isLogin
                        errorMessage = null
                    },
                    {
                        errorMessage = null
                        if (isLogin) {
                            authenticateUser(username, password, onAuthenticated) {
                                errorMessage = it
                            }
                        } else {
                            registerUser(username, password, onAuthenticated) {
                                errorMessage = it
                            }
                        }
                    },
                )
            }
        }
    }
}

/**
 * A composable function that displays the header of the authentication dialog.
 *
 * @param isLogin A boolean indicating whether the dialog is in login mode.
 */
@Composable
private fun AuthDialogHeader(isLogin: Boolean) = Heading6(if (isLogin) "Log In" else "Sign Up")

/**
 * A composable function that displays the input fields for username and password.
 *
 * @param username The current value of the username input field.
 * @param password The current value of the password input field.
 * @param onUsernameChange A callback function invoked when the username changes.
 * @param onPasswordChange A callback function invoked when the password changes.
 */
@Composable
private fun AuthDialogFields(
    username: String,
    password: String,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
) {
    TextField(
        username,
        onUsernameChange,
        Modifier.fillMaxWidth(),
        label = { Text("Username") },
    )
    TextField(
        password,
        onPasswordChange,
        Modifier.fillMaxWidth(),
        label = { Text("Password") },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    )
}

/**
 * A composable function that displays an error message in the dialog.
 *
 * @param errorMessage The error message to display.
 */
@Composable
private fun AuthDialogError(errorMessage: String) {
    Body2(
        errorMessage,
        colorType = Error,
    )
}

/**
 * A composable function that displays the action buttons in the authentication dialog.
 *
 * @param isLogin A boolean indicating whether the dialog is in login mode.
 * @param onSwitchMode A callback function invoked when the user switches between login and sign-up modes.
 * @param onSubmit A callback function invoked when the user submits the form.
 */
@Composable
private fun AuthDialogActions(
    isLogin: Boolean,
    onSwitchMode: () -> Unit,
    onSubmit: () -> Unit,
) {
    Row(
        Modifier.fillMaxWidth(),
        Arrangement.spacedBy(8.dp),
    ) {
        Button(onSwitchMode, Modifier.weight(1f)) {
            Text(if (isLogin) "Switch to Sign Up" else "Switch to Log In")
        }
        Button(onSubmit, Modifier.weight(1f)) {
            Text("Submit")
        }
    }
}

/**
 * Authenticates a user by verifying their username and password.
 *
 * @param username The username entered by the user.
 * @param password The password entered by the user.
 * @param onSuccess A callback function invoked when authentication is successful.
 * @param onError A callback function invoked with an error message if authentication fails.
 */
private fun authenticateUser(
    username: String,
    password: String,
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit,
) {
    try {
        val isValid = checkPassword(username, password)
        if (isValid) {
            onSuccess(username)
        } else {
            onError("Invalid username or password.")
        }
    } catch (e: Exception) {
        e.printStackTrace()
        onError("An error occurred: ${e.message}")
    }
}

/**
 * Registers a new user by inserting their username and password into the database.
 *
 * @param username The username entered by the user.
 * @param password The password entered by the user.
 * @param onSuccess A callback function invoked when registration is successful.
 * @param onError A callback function invoked with an error message if registration fails.
 */
private fun registerUser(
    username: String,
    password: String,
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit,
) {
    try {
        insertUser(username, password)
        onSuccess(username)
    } catch (e: Exception) {
        e.printStackTrace()
        onError("An error occurred: ${e.message}")
    }
}

/**
 * A composable function that wraps the home screen with authentication.
 * Displays the authentication dialog if the user is not authenticated.
 */
@Composable
fun HomeWithAuth() {
    var prefIsAuthenticated by BooleanPreference("isAuthenticated", false, prefs)
    var isAuthenticated by remember { mutableStateOf(prefIsAuthenticated) }
    var petsInitialized by remember { mutableStateOf(false) }

    if (!isAuthenticated) {
        AuthDialog {
            isAuthenticated = true
            prefIsAuthenticated = true
            user = getUserIdByUsername(it)
            Logger.i("User ID set to $user and logged in as $it")
        }
    } else {
        LaunchedEffect(Unit) {
            initializePets()
            petsInitialized = true
        }

        if (!petsInitialized) {
            Box(
                Modifier.fillMaxSize(),
                Alignment.Center,
            ) {
                CircularProgressIndicator(strokeCap = Round)
            }
        } else {
            Home()
        }
    }
}
