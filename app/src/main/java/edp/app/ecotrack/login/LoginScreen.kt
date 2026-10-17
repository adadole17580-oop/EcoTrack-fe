
package edp.app.ecotrack.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val EcoGreen = Color(0xFF216B2A)
private val EcoDarkGreen = Color(0xFF174D20)
private val EcoLightGreen = Color(0xFFE6F5EA)
private val EcoTextGreen = Color(0xFF286B32)
private val EcoGray = Color(0xFF6B7280)
private val EcoBorder = Color(0xFFD9E2DA)

@Composable
fun LoginScreen(
    onLoginClick: (
        String,
        String,
        (String?) -> Unit
    ) -> Unit = { _, _, onResult ->
        onResult("Login is not configured.")
    },
    onForgotPasswordClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoLightGreen)
    ) {

        // LEFT BRANDING PANEL
        Column(
            modifier = Modifier
                .weight(0.9f)
                .fillMaxHeight()
                .background(EcoDarkGreen)
                .padding(horizontal = 32.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Spacer(modifier = Modifier.height(110.dp))

                // ECOTRACK LOGO AND NAME
                Text(
                    text = "♧EcoTrack",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(10.dp))

                // MAIN HEADLINE
                Text(
                    text = "A Cleaner\nCommunity",
                    fontSize = 60.sp,
                    lineHeight = 70.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )

                Text(
                    text = "Starts With You.",
                    fontSize = 60.sp,
                    lineHeight = 70.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFFC8E6CC)
                )

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "Manage waste reports, coordinate collection, " +
                            "and help keep our community clean and sustainable.",
                    fontSize = 15.sp,
                    lineHeight = 24.sp,
                    color = Color(0xFFD6E8D8)
                )

                Spacer(modifier = Modifier.height(200.dp))

                // CENTERED SLOGAN
                Text(
                    text = "REDUCE. REUSE. RECYCLE.",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFFC8E6CC),
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = "© 2026 EcoTrack. All rights reserved.",
                fontSize = 11.sp,
                color = Color(0xFFB8D5BC)
            )
        }

        // RIGHT LOGIN PANEL
        Box(
            modifier = Modifier
                .weight(1.1f)
                .fillMaxHeight()
                .background(EcoLightGreen)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 460.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 32.dp,
                            vertical = 34.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // WELCOME TEXT
                    Text(
                        text = "WELCOME BACK",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 13.sp,
                        color = EcoTextGreen,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Sign in to your account",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = EcoDarkGreen,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Enter your credentials to access the admin dashboard.",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = EcoGray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // EMAIL LABEL
                    Text(
                        text = "Email Address",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = EcoDarkGreen,
                        textAlign = TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // EMAIL FIELD
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            errorMessage = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                "Enter your email address",
                                fontSize = 13.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email",
                                tint = EcoGray
                            )
                        },
                        singleLine = true,
                        enabled = !isLoading,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        shape = RoundedCornerShape(9.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EcoGreen,
                            unfocusedBorderColor = EcoBorder,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // PASSWORD LABEL
                    Text(
                        text = "Password",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = EcoDarkGreen,
                        textAlign = TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // PASSWORD FIELD
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorMessage = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                "Enter your password",
                                fontSize = 13.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password",
                                tint = EcoGray
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    passwordVisible = !passwordVisible
                                }
                            ) {
                                Icon(
                                    imageVector = if (passwordVisible) {
                                        Icons.Default.VisibilityOff
                                    } else {
                                        Icons.Default.Visibility
                                    },
                                    contentDescription = if (passwordVisible) {
                                        "Hide password"
                                    } else {
                                        "Show password"
                                    },
                                    tint = EcoGray
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        singleLine = true,
                        enabled = !isLoading,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        shape = RoundedCornerShape(9.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EcoGreen,
                            unfocusedBorderColor = EcoBorder,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // REMEMBER ME AND FORGOT PASSWORD
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = rememberMe,
                                onCheckedChange = {
                                    rememberMe = it
                                },
                                enabled = !isLoading,
                                colors = CheckboxDefaults.colors(
                                    checkedColor = EcoGreen
                                )
                            )

                            Text(
                                text = "Remember me",
                                fontSize = 12.sp,
                                color = EcoGray
                            )
                        }

                        TextButton(
                            onClick = onForgotPasswordClick,
                            enabled = !isLoading,
                            contentPadding = PaddingValues(
                                horizontal = 4.dp,
                                vertical = 4.dp
                            )
                        ) {
                            Text(
                                text = "Forgot Password?",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = EcoGreen
                            )
                        }
                    }

                    // ERROR MESSAGE
                    if (errorMessage.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = errorMessage,
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFFB3261E),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // LOGIN BUTTON
                    Button(
                        onClick = {
                            if (isLoading) {
                                return@Button
                            }

                            if (email.isBlank() || password.isBlank()) {
                                errorMessage =
                                    "Please enter your email and password."
                            } else if (!email.contains("@")) {
                                errorMessage =
                                    "Please enter a valid email address."
                            } else {
                                errorMessage = ""
                                isLoading = true

                                try {
                                    onLoginClick(
                                        email.trim(),
                                        password
                                    ) { error ->
                                        isLoading = false
                                        errorMessage = error.orEmpty()
                                    }
                                } catch (exception: Exception) {
                                    isLoading = false
                                    errorMessage = exception.message
                                        ?: "Login failed. Please try again."
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = !isLoading,
                        shape = RoundedCornerShape(9.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EcoGreen
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Login",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Authorized admin access only.",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 11.sp,
                        color = EcoGray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
