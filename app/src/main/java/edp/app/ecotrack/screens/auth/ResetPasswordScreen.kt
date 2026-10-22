
package edp.app.ecotrack.screens.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import edp.app.ecotrack.components.auth.AuthButton
import edp.app.ecotrack.components.auth.AuthPage
import edp.app.ecotrack.components.auth.AuthTextField
import edp.app.ecotrack.network.ApiClient
import kotlinx.coroutines.launch

@Composable
fun ResetPasswordScreen(
    accessToken: String,
    onBackToLogin: () -> Unit
) {
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }
    var isError by rememberSaveable { mutableStateOf(true) }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    AuthPage(
        title = "Create Your New EcoTrack Account Password",
        subtitle = "Enter and confirm your new password."
    ) {
        AuthTextField(
            label = "New Password",
            value = password,
            onValueChange = {
                password = it
                message = ""
            },
            placeholder = "Enter your new password",
            isPassword = true
        )

        AuthTextField(
            label = "Confirm Password",
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                message = ""
            },
            placeholder = "Confirm your new password",
            isPassword = true
        )

        if (message.isNotBlank()) {
            Text(
                text = message,
                color = if (isError) Color.Red else Color(0xFF24652A),
                fontSize = 12.sp
            )
        }

        AuthButton(
            text = if (isLoading) "Updating Password..." else "Reset Password",
            onClick = {
                when {
                    password.length < 8 -> {
                        message = "Password must be at least 8 characters."
                        isError = true
                    }

                    password != confirmPassword -> {
                        message = "Passwords do not match."
                        isError = true
                    }

                    accessToken.isBlank() -> {
                        message = "Your verification session has expired. Please request a new code."
                        isError = true
                    }

                    isLoading -> Unit

                    else -> {
                        isLoading = true
                        message = ""

                        coroutineScope.launch {
                            try {
                                val result = ApiClient.resetPassword(
                                    accessToken = accessToken,
                                    newPassword = password
                                )

                                message = result.optString(
                                    "message",
                                    "Password updated successfully."
                                )
                                isError = false
                                password = ""
                                confirmPassword = ""
                                onBackToLogin()
                            } catch (e: Exception) {
                                message = e.message
                                    ?: "Unable to reset your password. Please try again."
                                isError = true
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                }
            }
        )

        if (isLoading) {
            CircularProgressIndicator()
        }

        TextButton(
            onClick = onBackToLogin,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text("Back to Login")
        }
    }
}
