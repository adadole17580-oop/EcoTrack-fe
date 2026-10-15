package edp.app.ecotrack.screens.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import edp.app.ecotrack.components.auth.AuthButton
import edp.app.ecotrack.components.auth.AuthPage
import edp.app.ecotrack.components.auth.AuthTextField

@Composable
fun ResetPasswordScreen(
    onBackToLogin: () -> Unit
) {
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }

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
                color = Color.Red,
                fontSize = 12.sp
            )
        }

        AuthButton(
            text = "Continue",
            onClick = {
                message = when {
                    password.length < 8 ->
                        "Password must be at least 8 characters."

                    password != confirmPassword ->
                        "Passwords do not match."

                    else ->
                        "Password reset is ready for backend integration."
                }
            }
        )

        TextButton(
            onClick = onBackToLogin,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Login")
        }
    }
}