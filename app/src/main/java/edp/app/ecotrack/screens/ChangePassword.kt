package edp.app.ecotrack.screens

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*

import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

import edp.app.ecotrack.ui.theme.EcoDarkGreen
import edp.app.ecotrack.ui.theme.EcoGreen
import edp.app.ecotrack.ui.theme.EcoLightGreen
import edp.app.ecotrack.ui.theme.EcoTextGreen

@Composable
fun ChangePasswordDialog(
    onDismiss: () -> Unit
) {

    var currentPassword by remember {
        mutableStateOf("")
    }

    var newPassword by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var showCurrentPassword by remember {
        mutableStateOf(false)
    }

    var showNewPassword by remember {
        mutableStateOf(false)
    }

    var showConfirmPassword by remember {
        mutableStateOf(false)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = false
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.White,
                    RoundedCornerShape(18.dp)
                )
                .padding(22.dp)
        ) {

            // Green top line
            Spacer(
                modifier = Modifier
                    .size(
                        width = 44.dp,
                        height = 4.dp
                    )
                    .background(
                        EcoGreen,
                        RoundedCornerShape(50)
                    )
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            EcoLightGreen,
                            RoundedCornerShape(50)
                        ),
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

                Spacer(
                    modifier = Modifier.size(12.dp)
                )

                Column {

                    Text(
                        text = "Change Password",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = EcoDarkGreen
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = "Choose a strong password you do not use elsewhere.",
                        fontSize = 12.sp,
                        color = EcoTextGreen
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            Text(
                text = "Current password",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = EcoDarkGreen
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            OutlinedTextField(
                value = currentPassword,
                onValueChange = {
                    currentPassword = it
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(9.dp),
                visualTransformation =
                    if (showCurrentPassword) {
                        androidx.compose.ui.text.input.VisualTransformation.None
                    } else {
                        androidx.compose.ui.text.input.PasswordVisualTransformation()
                    },
                trailingIcon = {

                    IconButton(
                        onClick = {
                            showCurrentPassword =
                                !showCurrentPassword
                        }
                    ) {

                        Icon(
                            imageVector =
                                if (showCurrentPassword) {
                                    Icons.Default.VisibilityOff
                                } else {
                                    Icons.Default.Visibility
                                },
                            contentDescription = "Show password"
                        )
                    }
                }
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Text(
                text = "New password",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = EcoDarkGreen
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            OutlinedTextField(
                value = newPassword,
                onValueChange = {
                    newPassword = it
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(9.dp),
                visualTransformation =
                    if (showNewPassword) {
                        androidx.compose.ui.text.input.VisualTransformation.None
                    } else {
                        androidx.compose.ui.text.input.PasswordVisualTransformation()
                    },
                trailingIcon = {

                    IconButton(
                        onClick = {
                            showNewPassword =
                                !showNewPassword
                        }
                    ) {

                        Icon(
                            imageVector =
                                if (showNewPassword) {
                                    Icons.Default.VisibilityOff
                                } else {
                                    Icons.Default.Visibility
                                },
                            contentDescription = "Show password"
                        )
                    }
                }
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = "At least 8 characters with a number and symbol.",
                fontSize = 11.sp,
                color = EcoTextGreen
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Text(
                text = "Confirm new password",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = EcoDarkGreen
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(9.dp),
                visualTransformation =
                    if (showConfirmPassword) {
                        androidx.compose.ui.text.input.VisualTransformation.None
                    } else {
                        androidx.compose.ui.text.input.PasswordVisualTransformation()
                    },
                trailingIcon = {

                    IconButton(
                        onClick = {
                            showConfirmPassword =
                                !showConfirmPassword
                        }
                    ) {

                        Icon(
                            imageVector =
                                if (showConfirmPassword) {
                                    Icons.Default.VisibilityOff
                                } else {
                                    Icons.Default.Visibility
                                },
                            contentDescription = "Show password"
                        )
                    }
                }
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = EcoDarkGreen
                    ),
                    shape = RoundedCornerShape(9.dp)
                ) {

                    Text(
                        text = "Cancel"
                    )
                }

                Button(
                    onClick = {
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    enabled = currentPassword.isNotBlank() &&
                            newPassword.isNotBlank() &&
                            confirmPassword.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EcoDarkGreen
                    ),
                    shape = RoundedCornerShape(9.dp)
                ) {

                    Text(
                        text = "Update password"
                    )
                }
            }
        }
    }
}