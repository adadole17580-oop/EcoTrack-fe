
package edp.app.ecotrack.screens.residents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import edp.app.ecotrack.ui.theme.EcoDarkGreen
import edp.app.ecotrack.ui.theme.EcoGreen
import edp.app.ecotrack.ui.theme.EcoLightGreen
import edp.app.ecotrack.ui.theme.EcoTextGreen

@Composable
fun EditProfileDialog(
    fullName: String,
    email: String,
    mobile: String,
    address: String,
    currentBarangayId: Int,
    barangays: List<Pair<Int, String>>,
    isSaving: Boolean,
    message: String,
    onDismiss: () -> Unit,
    onSave: (
        fullName: String,
        mobile: String,
        address: String,
        barangayId: Int
    ) -> Unit
) {
    var editedName by remember(fullName) {
        mutableStateOf(fullName)
    }

    var editedMobile by remember(mobile) {
        mutableStateOf(mobile)
    }

    var editedAddress by remember(address) {
        mutableStateOf(address)
    }

    var selectedBarangayId by remember(currentBarangayId) {
        mutableStateOf(currentBarangayId)
    }

    var barangayMenuExpanded by remember {
        mutableStateOf(false)
    }

    var validationMessage by remember {
        mutableStateOf("")
    }

    val selectedBarangayName = barangays.firstOrNull {
        it.first == selectedBarangayId
    }?.second.orEmpty()

    Dialog(
        onDismissRequest = {
            if (!isSaving) {
                onDismiss()
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.White,
                    RoundedCornerShape(18.dp)
                )
                .verticalScroll(rememberScrollState())
                .padding(18.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .size(
                        width = 44.dp,
                        height = 3.dp
                    )
                    .align(Alignment.CenterHorizontally)
                    .background(
                        EcoGreen,
                        RoundedCornerShape(50)
                    )
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(EcoLightGreen),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = EcoDarkGreen,
                        modifier = Modifier.size(21.dp)
                    )
                }

                Spacer(modifier = Modifier.size(10.dp))

                Column {
                    Text(
                        text = "Edit Profile",
                        color = EcoDarkGreen,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Keep your resident information current.",
                        color = EcoTextGreen,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(EcoLightGreen),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = editedName
                            .trim()
                            .split(Regex("\\s+"))
                            .filter { it.isNotBlank() }
                            .take(2)
                            .joinToString("") {
                                it.first().uppercase()
                            }
                            .ifBlank { "R" },
                        color = EcoDarkGreen,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.size(8.dp))

                Column {
                    Text(
                        text = editedName.ifBlank { "Resident" },
                        color = EcoDarkGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Resident profile",
                        color = EcoGreen,
                        fontSize = 9.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            ProfileField(
                label = "Full name",
                value = editedName,
                onValueChange = {
                    editedName = it
                    validationMessage = ""
                },
                enabled = !isSaving
            )

            Spacer(modifier = Modifier.height(9.dp))

            ProfileField(
                label = "Email address",
                value = email,
                onValueChange = {},
                enabled = false
            )

            Spacer(modifier = Modifier.height(9.dp))

            ProfileField(
                label = "Mobile number",
                value = editedMobile,
                onValueChange = {
                    editedMobile = it
                    validationMessage = ""
                },
                enabled = !isSaving
            )

            Spacer(modifier = Modifier.height(9.dp))

            Text(
                text = "Barangay",
                color = EcoDarkGreen,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Column {
                OutlinedButton(
                    onClick = {
                        barangayMenuExpanded = true
                    },
                    enabled = !isSaving && barangays.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = selectedBarangayName.ifBlank {
                            if (barangays.isEmpty()) {
                                "No barangays available"
                            } else {
                                "Select barangay"
                            }
                        },
                        modifier = Modifier.weight(1f),
                        color = EcoDarkGreen,
                        fontSize = 12.sp
                    )

                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Choose barangay",
                        tint = EcoDarkGreen
                    )
                }

                DropdownMenu(
                    expanded = barangayMenuExpanded,
                    onDismissRequest = {
                        barangayMenuExpanded = false
                    },
                    modifier = Modifier.background(Color.White)
                ) {
                    barangays.forEach { barangay ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = barangay.second,
                                    fontSize = 12.sp
                                )
                            },
                            onClick = {
                                selectedBarangayId = barangay.first
                                barangayMenuExpanded = false
                                validationMessage = ""
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(9.dp))

            ProfileField(
                label = "Specific home address",
                value = editedAddress,
                onValueChange = {
                    editedAddress = it
                    validationMessage = ""
                },
                enabled = !isSaving,
                minHeight = 56.dp
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Enter your street, purok, zone, or other address details.",
                color = EcoTextGreen,
                fontSize = 9.sp
            )

            if (validationMessage.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = validationMessage,
                    color = Color(0xFFB3261E),
                    fontSize = 11.sp
                )
            }

            if (message.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = message,
                    color = if (
                        message.contains("successfully", ignoreCase = true)
                    ) {
                        EcoGreen
                    } else {
                        Color(0xFFB3261E)
                    },
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    enabled = !isSaving,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Cancel",
                        fontSize = 11.sp,
                        color = EcoDarkGreen
                    )
                }

                Button(
                    onClick = {
                        when {
                            editedName.isBlank() -> {
                                validationMessage = "Enter your full name."
                            }

                            selectedBarangayId <= 0 -> {
                                validationMessage = "Select your barangay."
                            }

                            editedAddress.isBlank() -> {
                                validationMessage = "Enter your specific home address."
                            }

                            else -> {
                                validationMessage = ""
                                onSave(
                                    editedName.trim(),
                                    editedMobile.trim(),
                                    editedAddress.trim(),
                                    selectedBarangayId
                                )
                            }
                        }
                    },
                    enabled = !isSaving,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EcoDarkGreen
                    )
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(17.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Save changes",
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    minHeight: androidx.compose.ui.unit.Dp = 48.dp
) {
    Column {
        Text(
            text = label,
            color = EcoDarkGreen,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(minHeight),
            singleLine = true,
            shape = RoundedCornerShape(7.dp),
            textStyle = TextStyle(
                fontSize = 12.sp
            )
        )
    }
}
