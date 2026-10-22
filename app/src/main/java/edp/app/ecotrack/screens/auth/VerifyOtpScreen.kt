package edp.app.ecotrack.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edp.app.ecotrack.components.auth.AuthButton
import edp.app.ecotrack.components.auth.AuthPage
import edp.app.ecotrack.network.ApiClient
import edp.app.ecotrack.ui.theme.EcoBorderGreen
import edp.app.ecotrack.ui.theme.EcoWhite
import kotlinx.coroutines.launch

@Composable
fun VerifyOtpScreen(
    email: String,
    onBack: () -> Unit,
    onVerify: (String, String) -> Unit,
    onResendCode: () -> Unit
) {
    var otp by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }
    var isError by rememberSaveable { mutableStateOf(false) }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    AuthPage(
        title = "Verify OTP Now",
        subtitle = "Enter the 6-digit code sent to your email."
    ) {
        Text(
            text = email,
            fontSize = 12.sp,
            color = Color(0xFF426C52)
        )

        Box {
            BasicTextField(
                value = otp,
                onValueChange = {
                    otp = it.filter(Char::isDigit).take(6)
                    message = ""
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.Transparent,
                    fontSize = 1.sp
                ),
                cursorBrush = SolidColor(Color.Transparent),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            repeat(6) { index ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(54.dp)
                                        .border(
                                            1.dp,
                                            EcoBorderGreen,
                                            RoundedCornerShape(3.dp)
                                        )
                                        .background(
                                            EcoWhite,
                                            RoundedCornerShape(3.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = otp.getOrNull(index)?.toString() ?: "",
                                        fontSize = 20.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                        ) {
                            innerTextField()
                        }
                    }
                }
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
            text = if (isLoading) "Please wait..." else "Verify",
            onClick = {
                when {
                    isLoading -> Unit

                    otp.length != 6 -> {
                        message = "Enter the complete 6-digit code."
                        isError = true
                    }

                    email.isBlank() -> {
                        message = "Email address is missing. Please try again."
                        isError = true
                    }

                    else -> {
                        isLoading = true
                        message = ""

                        coroutineScope.launch {
                            try {
                                val result = ApiClient.verifyRecoveryOtp(email, otp)
                                val accessToken = result.optString("access_token")
                                val refreshToken = result.optString("refresh_token")

                                if (accessToken.isBlank() || refreshToken.isBlank()) {
                                    throw Exception(
                                        "Verification succeeded but the session was not returned. Please try again."
                                    )
                                }

                                isError = false
                                onVerify(accessToken, refreshToken)
                            } catch (e: Exception) {
                                message = e.message
                                    ?: "Verification failed. Check your code and try again."
                                isError = true
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                }
            }
        )

        TextButton(
            onClick = {
                if (!isLoading) {
                    isLoading = true
                    message = ""

                    coroutineScope.launch {
                        try {
                            val result = ApiClient.resendRecoveryOtp(email)
                            message = result.optString(
                                "message",
                                "If the account is eligible, a recovery email will be sent."
                            )
                            isError = false
                            onResendCode()
                        } catch (e: Exception) {
                            message = e.message
                                ?: "Unable to resend the code. Please try again."
                            isError = true
                        } finally {
                            isLoading = false
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text(
                text = "Didn't receive a code? Resend Code",
                fontSize = 11.sp
            )
        }

        TextButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text("Back")
        }
    }
}