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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
fun RegisterScreen(
    onBackToLogin: () -> Unit
) {
    var fullName by rememberSaveable { mutableStateOf("") }
    var mobile by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var acceptedTerms by rememberSaveable { mutableStateOf(false) }
    var message by rememberSaveable { mutableStateOf("") }
    var isError by rememberSaveable { mutableStateOf(true) }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    AuthPage(
        title = "Create your Account",
        subtitle = "Fill in your information. All fields are required.",
        topLabel = "WELCOME TO ECOTRACK!"
    ) {
        AuthTextField(
            label = "Full Name",
            value = fullName,
            onValueChange = { fullName = it },
            placeholder = "Enter your full name"
        )

        AuthTextField(
            label = "Mobile Number",
            value = mobile,
            onValueChange = {
                mobile = it.filter(Char::isDigit).take(15)
            },
            placeholder = "Enter mobile number",
            keyboardType = KeyboardType.Phone
        )

        AuthTextField(
            label = "Email Address",
            value = email,
            onValueChange = { email = it },
            placeholder = "Enter email address",
            keyboardType = KeyboardType.Email
        )

        AuthTextField(
            label = "Barangay",
            value = address,
            onValueChange = { address = it },
            placeholder = "Enter your barangay"
        )

        AuthTextField(
            label = "Password",
            value = password,
            onValueChange = { password = it },
            placeholder = "Enter at least 8 characters",
            isPassword = true
        )

        AuthTextField(
            label = "Confirm Password",
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = "Confirm password",
            isPassword = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = acceptedTerms,
                onCheckedChange = { acceptedTerms = it }
            )

            Text(
                text = "I confirm that the information above is accurate and agree to use EcoTrack responsibly.",
                fontSize = 11.sp
            )
        }

        if (message.isNotBlank()) {
            Text(
                text = message,
                color = if (isError) Color.Red else Color(0xFF24652A),
                fontSize = 12.sp
            )
        }

        AuthButton(
            text = if (isLoading) "Creating Account..." else "Create Account",
            onClick = {
                if (isLoading) {
                    return@AuthButton
                }

                message = ""
                isError = true

                when {
                    fullName.isBlank() -> {
                        message = "Please enter your full name."
                    }

                    mobile.isBlank() -> {
                        message = "Please enter your mobile number."
                    }

                    email.isBlank() -> {
                        message = "Please enter your email address."
                    }

                    !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> {
                        message = "Please enter a valid email address."
                    }

                    address.isBlank() -> {
                        message = "Please enter your barangay."
                    }

                    password.length < 8 -> {
                        message = "Password must be at least 8 characters."
                    }

                    password != confirmPassword -> {
                        message = "Passwords do not match."
                    }

                    !acceptedTerms -> {
                        message = "Please confirm the information above."
                    }

                    else -> {
                        coroutineScope.launch {
                            isLoading = true

                            try {
                                val result = ApiClient.register(
                                    fullName = fullName,
                                    email = email.trim(),
                                    password = password,
                                    phone = mobile,
                                    barangay = address
                                )

                                message = result.optString(
                                    "message",
                                    "Account created successfully."
                                )

                                isError = false
                                password = ""
                                confirmPassword = ""
                            } catch (exception: Exception) {
                                message = exception.message
                                    ?: "Registration failed. Please try again."

                                isError = true
                            } finally {
                                isLoading = false
                            }
                        }
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
                text = "Already have an account?",
                fontSize = 11.sp
            )

            TextButton(
                onClick = onBackToLogin
            ) {
                Text(
                    text = "Log In",
                    fontSize = 11.sp
                )
            }
        }
    }
}