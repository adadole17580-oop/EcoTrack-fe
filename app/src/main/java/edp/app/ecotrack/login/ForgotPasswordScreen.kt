
package edp.app.ecotrack.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
fun ForgotPasswordScreen(
    onContinueClick: (String) -> Unit = {},
    onBackToLoginClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

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

                // ORIGINAL LOGO TEXT
                Text(
                    text = "♧EcoTrack",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(10.dp))

                // ORIGINAL HEADLINE
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

        // RIGHT FORGOT PASSWORD PANEL — MATCH ORIGINAL LOGIN DIMENSIONS
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

                    Text(
                        text = "FORGOT PASSWORD",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 13.sp,
                        color = EcoTextGreen,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Reset your password",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = EcoDarkGreen,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Enter your registered email address to continue.",
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
                                text = "Enter your email address",
                                fontSize = 13.sp
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        shape = RoundedCornerShape(9.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EcoGreen,
                            unfocusedBorderColor = EcoBorder,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            cursorColor = EcoGreen
                        )
                    )

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

                    Spacer(modifier = Modifier.height(24.dp))

                    // CONTINUE BUTTON
                    Button(
                        onClick = {
                            when {
                                email.isBlank() -> {
                                    errorMessage =
                                        "Please enter your email address."
                                }

                                !email.trim().contains("@") -> {
                                    errorMessage =
                                        "Please enter a valid email address."
                                }

                                else -> {
                                    errorMessage = ""
                                    onContinueClick(email.trim())
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(9.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EcoGreen
                        )
                    ) {
                        Text(
                            text = "Continue",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(
                        onClick = onBackToLoginClick,
                        contentPadding = PaddingValues(
                            horizontal = 4.dp,
                            vertical = 4.dp
                        )
                    ) {
                        Text(
                            text = "Back to Login",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = EcoGreen
                        )
                    }
                }
            }
        }
    }
}
