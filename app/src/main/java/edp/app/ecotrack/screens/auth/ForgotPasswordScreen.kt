package edp.app.ecotrack.screens.auth

import android.util.Patterns
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import edp.app.ecotrack.components.auth.AuthButton
import edp.app.ecotrack.components.auth.AuthPage
import edp.app.ecotrack.components.auth.AuthTextField
import edp.app.ecotrack.network.ApiClient
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreen(
    onBackToLogin: () -> Unit,
    onContinue: (String) -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }
    var isError by rememberSaveable { mutableStateOf(false) }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    AuthPage(
        title = "Forgot Password",
        subtitle = "Enter your email address to receive a password recovery link."
    ) {
        AuthTextField(
            label = "Email Address",
            value = email,
            onValueChange = {
                email = it
                message = ""
            },
            placeholder = "Email address",
            keyboardType = KeyboardType.Email
        )

        if (message.isNotBlank()) {
            Text(
                text = message,
                color = if (isError) Color.Red else Color(0xFF24652A),
                fontSize = 12.sp
            )
        }

        AuthButton(
            text = if (isLoading) "Sending..." else "Send Reset Link",
            onClick = {
                when {
                    email.isBlank() -> {
                        message = "Please enter your email address."
                        isError = true
                    }

                    !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> {
                        message = "Please enter a valid email address."
                        isError = true
                    }

                    isLoading -> Unit

                    else -> {
                        isLoading = true
                        message = ""

                        coroutineScope.launch {
                            try {
                                val result = ApiClient.forgotPassword(email.trim())
                                message = result.optString(
                                    "message",
                                    "If the account is eligible, a password recovery email will be sent."
                                )
                                isError = false
                                onContinue(email.trim())
                            } catch (e: Exception) {
                                message = e.message
                                    ?: "Unable to send the password recovery request. Please try again."
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