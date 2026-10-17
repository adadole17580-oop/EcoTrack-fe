
package edp.app.ecotrack.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
private val EcoBorder = Color(0xFFD9E2DA)
private val EcoTextGreen = Color(0xFF286B32)
private val White = Color.White
private val EcoMutedText = Color(0xFF737373)
private val EcoError = Color(0xFFD32F2F)

@Composable
fun ResetPasswordScreen(
    onFinishClick: (String) -> Unit = {},
    onBackToLoginClick: () -> Unit = {}
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var newPasswordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }
    var showSuccessScreen by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoLightGreen)
    ) {

        // LEFT BRANDING PANEL — MATCH ORIGINAL LOGIN SCREEN
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

                // ORIGINAL ECOTRACK LOGO
                Text(
                    text = "♧EcoTrack",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    color = White
                )

                Spacer(modifier = Modifier.height(10.dp))

                // ORIGINAL HEADLINE
                Text(
                    text = "A Cleaner\nCommunity",
                    fontSize = 60.sp,
                    lineHeight = 70.sp,
                    fontWeight = FontWeight.Normal,
                    color = White
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

                // ORIGINAL CENTERED SLOGAN
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

        // RIGHT RESET PASSWORD PANEL
        Box(
            modifier = Modifier
                .weight(1.1f)
                .fillMaxHeight()
                .background(EcoLightGreen)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {

            if (!showSuccessScreen) {

                // CREATE NEW PASSWORD CARD
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 460.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = White
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
                        horizontalAlignment = Alignment.Start
                    ) {

                        Text(
                            text = "Create New Password",
                            color = EcoDarkGreen,
                            fontSize = 27.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Create a new password for your account. " +
                                    "Make sure it is strong and easy for you to remember.",
                            color = EcoMutedText,
                            fontSize = 14.sp,
                            lineHeight = 21.sp
                        )

                        Spacer(modifier = Modifier.height(28.dp))

                        Text(
                            text = "New Password",
                            color = EcoTextGreen,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        PasswordInputField(
                            value = newPassword,
                            onValueChange = {
                                newPassword = it
                                newPasswordError = ""
                            },
                            visible = showNewPassword,
                            onVisibilityChange = {
                                showNewPassword = !showNewPassword
                            },
                            placeholder = "Enter new password"
                        )

                        if (newPasswordError.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(5.dp))

                            Text(
                                text = newPasswordError,
                                color = EcoError,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Confirm Password",
                            color = EcoTextGreen,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        PasswordInputField(
                            value = confirmPassword,
                            onValueChange = {
                                confirmPassword = it
                                confirmPasswordError = ""
                            },
                            visible = showConfirmPassword,
                            onVisibilityChange = {
                                showConfirmPassword = !showConfirmPassword
                            },
                            placeholder = "Confirm new password"
                        )

                        if (confirmPasswordError.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(5.dp))

                            Text(
                                text = confirmPasswordError,
                                color = EcoError,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        Button(
                            onClick = {
                                newPasswordError = ""
                                confirmPasswordError = ""

                                when {
                                    newPassword.isBlank() -> {
                                        newPasswordError =
                                            "Please enter your new password."
                                    }

                                    newPassword.length < 8 -> {
                                        newPasswordError =
                                            "Password must be at least 8 characters."
                                    }

                                    confirmPassword.isBlank() -> {
                                        confirmPasswordError =
                                            "Please confirm your password."
                                    }

                                    newPassword != confirmPassword -> {
                                        confirmPasswordError =
                                            "Passwords do not match."
                                    }

                                    else -> {
                                        // Frontend demonstration only.
                                        // Connect the backend before treating
                                        // this as a real password reset.
                                        onFinishClick(newPassword)
                                        showSuccessScreen = true
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(9.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EcoGreen,
                                contentColor = White
                            )
                        ) {
                            Text(
                                text = "Continue",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

            } else {

                // PASSWORD RESET SUCCESS CARD
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 460.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = White
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
                                vertical = 42.dp
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Password reset successful",
                            tint = EcoGreen,
                            modifier = Modifier.size(76.dp)
                        )

                        Spacer(modifier = Modifier.height(22.dp))

                        Text(
                            text = "Password Reset Successful!",
                            color = EcoDarkGreen,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Your password has been reset successfully. " +
                                    "You can now sign in using your new password.",
                            color = EcoMutedText,
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        Button(
                            onClick = onBackToLoginClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(9.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EcoGreen,
                                contentColor = White
                            )
                        ) {
                            Text(
                                text = "Back to Login",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PasswordInputField(
    value: String,
    onValueChange: (String) -> Unit,
    visible: Boolean,
    onVisibilityChange: () -> Unit,
    placeholder: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .border(
                width = 1.dp,
                color = EcoBorder,
                shape = RoundedCornerShape(9.dp)
            )
            .background(
                White,
                RoundedCornerShape(9.dp)
            )
            .padding(
                start = 14.dp,
                end = 6.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    color = Color(0xFF9E9E9E),
                    fontSize = 14.sp
                )
            }

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF222222),
                    fontSize = 14.sp
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = if (visible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                }
            )
        }

        IconButton(
            onClick = onVisibilityChange,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = if (visible) {
                    Icons.Default.VisibilityOff
                } else {
                    Icons.Default.Visibility
                },
                contentDescription = if (visible) {
                    "Hide password"
                } else {
                    "Show password"
                },
                tint = EcoTextGreen
            )
        }
    }
}
