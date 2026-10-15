package edp.app.ecotrack.screens.auth

import android.util.Patterns
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import edp.app.ecotrack.components.auth.AuthButton
import edp.app.ecotrack.components.auth.AuthPage
import edp.app.ecotrack.components.auth.AuthTextField

@Composable
fun ForgotPasswordScreen(
    onBackToLogin: () -> Unit,
    onContinue: (String) -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }

    AuthPage(
        title = "Forgot Password",
        subtitle = "Enter your email address to receive a reset code and regain access to your account."
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
                color = Color.Red,
                fontSize = 12.sp
            )
        }

        AuthButton(
            text = "Continue",
            onClick = {
                when {
                    email.isBlank() ->
                        message = "Please enter your email address."

                    !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                        message = "Please enter a valid email address."

                    else ->
                        onContinue(email.trim())
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