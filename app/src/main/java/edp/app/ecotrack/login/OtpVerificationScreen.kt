
package edp.app.ecotrack.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---------------------------------------------------------
// ECOTRACK COLORS — MATCH ORIGINAL LOGIN SCREEN
// ---------------------------------------------------------

private val EcoGreen = Color(0xFF216B2A)
private val EcoDarkGreen = Color(0xFF174D20)
private val EcoLightGreen = Color(0xFFE6F5EA)
private val EcoBorder = Color(0xFFD9E2DA)
private val EcoTextGreen = Color(0xFF286B32)
private val White = Color.White
private val EcoGray = Color(0xFF6B7280)

@Composable
fun OtpVerificationScreen(
    email: String = "",
    onVerifyClick: (String) -> Unit = {},
    onResendCodeClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var otp by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoLightGreen)
    ) {

        // -------------------------------------------------
        // LEFT BRANDING PANEL — ORIGINAL LOGIN DIMENSIONS
        // -------------------------------------------------

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

                // ORIGINAL LOGO
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

        // -------------------------------------------------
        // RIGHT OTP PANEL — ORIGINAL LOGIN DIMENSIONS
        // -------------------------------------------------

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
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Verify OTP Now",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = EcoDarkGreen,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Enter the 5-Digit Code Sent to You",
                        fontSize = 14.sp,
                        color = EcoGray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    if (email.isNotBlank()) {
                        Text(
                            text = "Please check your email:\n$email",
                            fontSize = 12.sp,
                            color = EcoTextGreen,
                            textAlign = TextAlign.Center,
                            lineHeight = 19.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text = "Verification Code",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = EcoTextGreen
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // -------------------------------------------------
                    // FIVE-DIGIT OTP INPUT — DUPLICATE TEXT FIXED
                    // -------------------------------------------------

                    BasicTextField(
                        value = otp,
                        onValueChange = { newValue ->
                            if (
                                newValue.length <= 5 &&
                                newValue.all { it.isDigit() }
                            ) {
                                otp = newValue
                                errorMessage = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.NumberPassword
                        ),
                        singleLine = true,
                        textStyle = TextStyle(
                            color = Color.Transparent,
                            fontSize = 1.sp,
                            textAlign = TextAlign.Center
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp)
                            ) {
                                // Visible OTP boxes
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalArrangement =
                                        Arrangement.spacedBy(8.dp)
                                ) {
                                    repeat(5) { index ->
                                        val character =
                                            otp.getOrNull(index)?.toString() ?: ""

                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxHeight()
                                                .background(
                                                    color = EcoLightGreen,
                                                    shape = RoundedCornerShape(9.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = character.ifEmpty { "—" },
                                                fontSize = 22.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (character.isEmpty()) {
                                                    EcoBorder
                                                } else {
                                                    EcoDarkGreen
                                                }
                                            )
                                        }
                                    }
                                }

                                // Keep the real input field active.
                                // Its text is transparent to prevent duplication.
                                Box(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    innerTextField()
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Didn't you receive any code?",
                            fontSize = 12.sp,
                            color = EcoGray
                        )

                        TextButton(
                            onClick = onResendCodeClick,
                            contentPadding = PaddingValues(
                                horizontal = 4.dp,
                                vertical = 2.dp
                            )
                        ) {
                            Text(
                                text = "Resend Code",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = EcoGreen
                            )
                        }
                    }

                    if (errorMessage.isNotBlank()) {
                        Text(
                            text = errorMessage,
                            color = Color(0xFFB3261E),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (otp.length != 5) {
                                errorMessage =
                                    "Please enter the complete 5-digit code."
                            } else {
                                errorMessage = ""
                                onVerifyClick(otp)
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
                            fontWeight = FontWeight.SemiBold,
                            color = White
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(
                        onClick = onBackClick
                    ) {
                        Text(
                            text = "Back",
                            fontSize = 13.sp,
                            color = EcoTextGreen
                        )
                    }
                }
            }
        }
    }
}
