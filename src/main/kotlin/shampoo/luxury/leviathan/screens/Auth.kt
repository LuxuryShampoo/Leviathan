package shampoo.luxury.leviathan.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import shampoo.luxury.leviathan.components.MaxLoading
import shampoo.luxury.leviathan.components.auth.AuthDialogError
import shampoo.luxury.leviathan.components.auth.AuthDialogFields
import shampoo.luxury.leviathan.components.auth.AuthDialogHeader
import shampoo.luxury.leviathan.global.GlobalLoadingState.addLoading
import shampoo.luxury.leviathan.global.GlobalLoadingState.removeLoading
import shampoo.luxury.leviathan.global.Values.Prefs.prefs
import shampoo.luxury.leviathan.global.Values.user
import shampoo.luxury.leviathan.global.clearPreferences
import shampoo.luxury.leviathan.wrap.data.pets.initializePets
import shampoo.luxury.leviathan.wrap.data.users.checkPassword
import shampoo.luxury.leviathan.wrap.data.users.getUserIdByUsername
import shampoo.luxury.leviathan.wrap.data.users.insertUser
import shampoo.luxury.leviathan.wrap.data.users.isValidPassword
import shampoo.luxury.leviathan.wrap.data.users.isValidUsername
import shampoo.luxury.leviathan.wrap.data.users.userIdExists
import xyz.malefic.compose.comps.text.typography.Body2
import xyz.malefic.compose.prefs.delegate.BooleanPreference

@Composable
fun AuthDialog(onAuthenticated: (String) -> Unit) {
    var isLogin by remember { mutableStateOf(true) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = {}) {
        Surface(
            Modifier.padding(16.dp),
            MaterialTheme.shapes.medium,
        ) {
            Box {
                AuthDialogContent(
                    isLogin = isLogin,
                    username = username,
                    password = password,
                    errorMessage = errorMessage,
                    onUsernameChange = { username = it },
                    onPasswordChange = { password = it },
                    onSwitchMode = {
                        isLogin = !isLogin
                        errorMessage = null
                    },
                    onSubmit = {
                        handleAuthSubmit(
                            isLogin = isLogin,
                            username = username,
                            password = password,
                            setLoading = { loading = it },
                            setError = { errorMessage = it },
                            onAuthenticated = onAuthenticated,
                        )
                    },
                )
                if (loading) {
                    MaxLoading(Modifier.background(colors.surface.copy(alpha = 0.6f)).fillMaxHeight().fillMaxWidth())
                }
            }
        }
    }
}

@Composable
private fun AuthDialogContent(
    isLogin: Boolean,
    username: String,
    password: String,
    errorMessage: String?,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSwitchMode: () -> Unit,
    onSubmit: () -> Unit,
) {
    Column(
        Modifier.padding(16.dp),
        Arrangement.spacedBy(8.dp),
        CenterHorizontally,
    ) {
        AuthDialogHeader(isLogin)
        AuthDialogFields(username, password, onUsernameChange, onPasswordChange)
        errorMessage?.let { AuthDialogError(it) }
        AuthDialogActions(isLogin, onSwitchMode, onSubmit)
    }
}

private fun handleAuthSubmit(
    isLogin: Boolean,
    username: String,
    password: String,
    setLoading: (Boolean) -> Unit,
    setError: (String?) -> Unit,
    onAuthenticated: (String) -> Unit,
) {
    setError(null)
    if (!validate(isLogin, username, password, setError)) {
        return
    }
    setLoading(true)
    val action = if (isLogin) ::authenticateUser else ::registerUser
    action(username, password, { user ->
        setLoading(false)
        onAuthenticated(user)
    }) { error ->
        setLoading(false)
        setError(error)
    }
}

private fun validate(
    isLogin: Boolean,
    username: String,
    password: String,
    onError: (String) -> Unit,
): Boolean {
    if (!isLogin) {
        when {
            !isValidUsername(username) -> {
                onError("Username must be 3-20 characters, letters, digits, or underscores.")
                return false
            }

            !isValidPassword(password) -> {
                onError(
                    "Password must be at least 8 characters, include at least one letter, one digit, one special character (!@#\\$%^&*()-_=+[]{}|;:',.<>?/`~), and contain no whitespace.",
                )
                return false
            }
        }
    }
    return true
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
) = Row(
    Modifier.fillMaxWidth(),
    Arrangement.spacedBy(8.dp),
) {
    Button(onSwitchMode, Modifier.weight(1f)) {
        Body2(if (isLogin) "Sign Up" else "Log In")
    }
    Button(onSubmit, Modifier.weight(1f)) {
        Body2("Submit")
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
) = CoroutineScope(Dispatchers.IO).launch {
    try {
        val isValid = checkPassword(username, password)
        withContext(Dispatchers.Main) {
            if (isValid) onSuccess(username) else onError("Invalid username or password.")
        }
    } catch (e: Exception) {
        withContext(Dispatchers.Main) {
            onError("An error occurred: ${e.message}")
        }
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
) = CoroutineScope(Dispatchers.IO).launch {
    try {
        insertUser(username, password)
        withContext(Dispatchers.Main) {
            onSuccess(username)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        withContext(Dispatchers.Main) {
            onError("An error occurred: ${e.message}")
        }
    }
}

@Composable
fun HomeWithAuth() {
    var prefIsAuthenticated by BooleanPreference("isAuthenticated", false, prefs)
    var isAuthenticated by remember { mutableStateOf(prefIsAuthenticated) }

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated && !userIdExists(user)) {
            isAuthenticated = false
            prefIsAuthenticated = false
            user = -1
            clearPreferences()
        }
    }

    if (!isAuthenticated) {
        AuthDialog {
            isAuthenticated = true
            prefIsAuthenticated = true
            user = getUserIdByUsername(it)
            Logger.i("User ID set to $user and logged in as $it")
        }
    } else {
        LaunchedEffect(Unit) {
            addLoading("pets initialization")
            initializePets()
            removeLoading("pets initialization")
        }

        Home()
    }
}
