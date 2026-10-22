
package edp.app.ecotrack.screens.residents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import edp.app.ecotrack.network.ApiClient
import edp.app.ecotrack.ui.theme.EcoDarkGreen
import edp.app.ecotrack.ui.theme.EcoGreen
import edp.app.ecotrack.ui.theme.EcoLightGreen
import edp.app.ecotrack.ui.theme.EcoTextGreen
import kotlinx.coroutines.launch

@Composable
fun ChangePasswordDialog(
    accessToken: String,
    onDismiss: () -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Dialog(
        onDismissRequest = {
            if (!isLoading) onDismiss()
        },
        properties = DialogProperties(
            dismissOnClickOutside = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(18.dp))
                .padding(22.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .size(width = 44.dp, height = 4.dp)
                    .background(EcoGreen, RoundedCornerShape(50))
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .size(44.dp)
                        .background(EcoLightGreen, RoundedCornerShape(50.dp)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password",
                        tint = EcoDarkGreen,
                        modifier = Modifier.size(23.dp)
                    )
                }

                Spacer(modifier = Modifier.size(12.dp))

                Column {
                    Text(
                        text = "Change Password",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = EcoDarkGreen
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "Choose a strong password you do not use elsewhere.",
                        fontSize = 12.sp,
                        color = EcoTextGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Current password",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = EcoDarkGreen
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = currentPassword,
                onValueChange = {
                    currentPassword = it
                    message = ""
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                singleLine = true,
                shape = RoundedCornerShape(9.dp),
                visualTransformation = if (showCurrentPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            showCurrentPassword = !showCurrentPassword
                        }
                    ) {
                        Icon(
                            imageVector = if (showCurrentPassword) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                            contentDescription = "Toggle current password visibility"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "New password",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = EcoDarkGreen
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = {
                    newPassword = it
                    message = ""
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                singleLine = true,
                shape = RoundedCornerShape(9.dp),
                visualTransformation = if (showNewPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            showNewPassword = !showNewPassword
                        }
                    ) {
                        Icon(
                            imageVector = if (showNewPassword) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                            contentDescription = "Toggle new password visibility"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "At least 8 characters with a number and symbol.",
                fontSize = 11.sp,
                color = EcoTextGreen
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Confirm new password",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = EcoDarkGreen
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    message = ""
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                singleLine = true,
                shape = RoundedCornerShape(9.dp),
                visualTransformation = if (showConfirmPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            showConfirmPassword = !showConfirmPassword
                        }
                    ) {
                        Icon(
                            imageVector = if (showConfirmPassword) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                            contentDescription = "Toggle confirmation password visibility"
                        )
                    }
                }
            )

            if (message.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = message,
                    fontSize = 12.sp,
                    color = if (isSuccess) EcoGreen else Color(0xFFB3261E)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = EcoDarkGreen
                    ),
                    shape = RoundedCornerShape(9.dp)
                ) {
                    Text(text = "Cancel")
                }

                Button(
                    onClick = {
                        when {
                            currentPassword.isBlank() -> {
                                message = "Enter your current password."
                                isSuccess = false
                            }

                            newPassword.length < 8 -> {
                                message = "Your new password must be at least 8 characters."
                                isSuccess = false
                            }

                            !newPassword.any { it.isDigit() } ||
                                    !newPassword.any { !it.isLetterOrDigit() } -> {
                                message = "Include at least one number and one symbol."
                                isSuccess = false
                            }

                            newPassword == currentPassword -> {
                                message = "Your new password must differ from your current password."
                                isSuccess = false
                            }

                            newPassword != confirmPassword -> {
                                message = "The new passwords do not match."
                                isSuccess = false
                            }

                            else -> {
                                coroutineScope.launch {
                                    isLoading = true
                                    message = ""

                                    try {
                                        ApiClient.changeResidentPassword(
                                            accessToken = accessToken,
                                            currentPassword = currentPassword,
                                            newPassword = newPassword
                                        )

                                        isSuccess = true
                                        message = "Password updated successfully."
                                        currentPassword = ""
                                        newPassword = ""
                                        confirmPassword = ""
                                    } catch (error: Exception) {
                                        isSuccess = false
                                        message = error.message
                                            ?: "Unable to change your password."
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading &&
                            currentPassword.isNotBlank() &&
                            newPassword.isNotBlank() &&
                            confirmPassword.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EcoDarkGreen
                    ),
                    shape = RoundedCornerShape(9.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(text = "Update password")
                    }
                }
            }
        }
    }
}
