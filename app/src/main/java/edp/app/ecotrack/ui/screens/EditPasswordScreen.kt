package edp.app.ecotrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import edp.app.ecotrack.ui.theme.EcoGreen
import edp.app.ecotrack.ui.theme.EcoGreenLight
import edp.app.ecotrack.ui.theme.EcoSecondaryText
import edp.app.ecotrack.ui.theme.EcoSuccessLight
import edp.app.ecotrack.ui.theme.EcoText
import edp.app.ecotrack.ui.theme.EcoWhite

@Composable
fun EditPasswordScreen(
    onBack: () -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var currentVisible by remember { mutableStateOf(false) }
    var newVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showSuccess by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoGreenLight)
            .statusBarsPadding()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(EcoGreen)
                .padding(
                    start = 12.dp,
                    end = 20.dp,
                    top = 18.dp,
                    bottom = 26.dp
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = "Back",
                        tint = EcoWhite,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(58.dp)
                        .background(
                            EcoWhite.copy(alpha = 0.18f),
                            RoundedCornerShape(17.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "Password",
                        tint = EcoWhite,
                        modifier = Modifier.size(31.dp)
                    )
                }

                Column(
                    modifier = Modifier.padding(start = 14.dp)
                ) {
                    Text(
                        text = "Change Password",
                        style = MaterialTheme.typography.headlineSmall,
                        color = EcoWhite
                    )

                    Text(
                        text = "Keep your account secure",
                        style = MaterialTheme.typography.bodySmall,
                        color = EcoWhite.copy(alpha = 0.78f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 20.dp,
                    bottom = 16.dp
                ),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = EcoWhite
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(13.dp)
            ) {

                Text(
                    text = "Update your password",
                    style = MaterialTheme.typography.titleLarge,
                    color = EcoText
                )

                Text(
                    text = "Enter your current password and choose a new one.",
                    style = MaterialTheme.typography.bodySmall,
                    color = EcoSecondaryText
                )

                Spacer(
                    modifier = Modifier.size(2.dp)
                )

                PasswordField(
                    value = currentPassword,
                    onValueChange = {
                        currentPassword = it
                        errorMessage = null
                        showSuccess = false
                    },
                    label = "Current Password",
                    visible = currentVisible,
                    onVisibilityChange = {
                        currentVisible = !currentVisible
                    }
                )

                PasswordField(
                    value = newPassword,
                    onValueChange = {
                        newPassword = it
                        errorMessage = null
                        showSuccess = false
                    },
                    label = "New Password",
                    visible = newVisible,
                    onVisibilityChange = {
                        newVisible = !newVisible
                    }
                )

                PasswordField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        errorMessage = null
                        showSuccess = false
                    },
                    label = "Confirm New Password",
                    visible = confirmVisible,
                    onVisibilityChange = {
                        confirmVisible = !confirmVisible
                    }
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }

                if (showSuccess) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                EcoSuccessLight,
                                RoundedCornerShape(12.dp)
                            )
                            .padding(13.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = EcoGreen,
                            modifier = Modifier.size(22.dp)
                        )

                        Text(
                            text = "Password updated successfully.",
                            style = MaterialTheme.typography.bodySmall,
                            color = EcoText,
                            modifier = Modifier.padding(start = 9.dp)
                        )
                    }
                }

                Button(
                    onClick = {
                        when {
                            currentPassword.isBlank() -> {
                                errorMessage = "Please enter your current password."
                            }

                            newPassword.length < 6 -> {
                                errorMessage =
                                    "New password must be at least 6 characters."
                            }

                            newPassword != confirmPassword -> {
                                errorMessage =
                                    "New passwords do not match."
                            }

                            else -> {
                                showSuccess = true
                                errorMessage = null

                                currentPassword = ""
                                newPassword = ""
                                confirmPassword = ""

                                currentVisible = false
                                newVisible = false
                                confirmVisible = false
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 3.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EcoGreen
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Update Password",
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
                }

                Text(
                    text = "For your security, do not share your password with anyone.",
                    style = MaterialTheme.typography.bodySmall,
                    color = EcoSecondaryText,
                    modifier = Modifier.padding(
                        horizontal = 4.dp,
                        vertical = 2.dp
                    )
                )
            }
        }
    }
}

@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visible: Boolean,
    onVisibilityChange: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(label)
        },
        singleLine = true,
        visualTransformation = if (visible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            IconButton(
                onClick = onVisibilityChange
            ) {
                Icon(
                    imageVector = if (visible) {
                        Icons.Outlined.VisibilityOff
                    } else {
                        Icons.Outlined.Visibility
                    },
                    contentDescription = if (visible) {
                        "Hide password"
                    } else {
                        "Show password"
                    }
                )
            }
        }
    )
}