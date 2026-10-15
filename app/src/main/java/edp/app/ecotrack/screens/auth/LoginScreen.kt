package edp.app.ecotrack.screens.auth

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.KeyboardType
import edp.app.ecotrack.components.auth.AuthButton
import edp.app.ecotrack.components.auth.AuthPage
import edp.app.ecotrack.components.auth.AuthTextField

@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onLogin: (String, String) -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var keepSignedIn by rememberSaveable { mutableStateOf(false) }
    var message by rememberSaveable { mutableStateOf("") }
    var isError by rememberSaveable { mutableStateOf(true) }

    AuthPage(
        title = "Sign in to you account",
        subtitle = "Enter your account details to continue.",
        topLabel = "WELCOME BACK!"
    ) {
        AuthTextField(
            label = "Email Address",
            value = email,
            onValueChange = {
                email = it
                message = ""
            },
            placeholder = "name@ecotrack.com",
            keyboardType = KeyboardType.Email
        )

        AuthTextField(
            label = "Password",
            value = password,
            onValueChange = {
                password = it
                message = ""
            },
            placeholder = "Enter your password",
            isPassword = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = keepSignedIn,
                onCheckedChange = {
                    keepSignedIn = it
                }
            )

            Text(
                text = "Keep me signed in",
                fontSize = 11.sp,
                modifier = Modifier.weight(1f)
            )

            TextButton(
                onClick = onForgotPasswordClick
            ) {
                Text(
                    text = "Forgot Password?",
                    fontSize = 11.sp
                )
            }
        }

        if (message.isNotBlank()) {
            Text(
                text = message,
                color = if (isError) Color.Red else Color(0xFF24652A),
                fontSize = 12.sp
            )
        }

        AuthButton(
            text = "Log In",
            onClick = {
                when {
                    email.isBlank() -> {
                        message = "Please enter your email address."
                        isError = true
                    }

                    !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                        message = "Please enter a valid email address."
                        isError = true
                    }

                    password.isBlank() -> {
                        message = "Please enter your password."
                        isError = true
                    }

                    else -> {
                        onLogin(email.trim(), password)
                        message = "Login is ready for backend integration."
                        isError = false
                    }
                }
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Don't have an account?",
                fontSize = 11.sp
            )

            TextButton(onClick = onRegisterClick) {
                Text(
                    text = "Register",
                    fontSize = 11.sp
                )
            }
        }
    }
}
