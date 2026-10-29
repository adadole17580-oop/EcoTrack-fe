
package edp.app.ecotrack.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import kotlinx.coroutines.launch
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import edp.app.ecotrack.network.ApiClient
import kotlinx.coroutines.launch

// =========================================================
// ECOTRACK COLORS
// =========================================================

private val EcoDarkGreen = Color(0xFF216B2A)
private val EcoGreen = Color(0xFF2E7D32)
private val EcoLightGreen = Color(0xFFE6F5EA)
private val EcoTopBar = Color(0xFFE0F2E5)
private val EcoBorder = Color(0xFF8BE5A7)
private val EcoTextGreen = Color(0xFF286B32)
private val White = Color.White
private val Red = Color(0xFFB94A48)
private val FieldBackground = Color(0xFFF0FAF3)

// =========================================================
// PROFILE SCREEN
// =========================================================

@Composable
fun ProfileScreen(
    accessToken: String,
    onDashboardClick: () -> Unit = {},
    onWasteReportClick: () -> Unit = {},
    onCollectorsClick: () -> Unit = {},
    onResidentsClick: () -> Unit = {},
    onScheduleClick: () -> Unit = {},
    onAnalyticsClick: () -> Unit = {},
    onWasteGuideClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    var fullName by remember { mutableStateOf("System Admin") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("Not provided") }

    var profileLoading by remember { mutableStateOf(true) }
    var profileError by remember { mutableStateOf<String?>(null) }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    var showEditProfile by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    var isSavingProfile by remember { mutableStateOf(false) }
    var saveProfileError by remember { mutableStateOf<String?>(null) }
    var saveProfileSuccess by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    val imagePickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            if (uri != null) {
                profileImageUri = uri
            }
        }

    // LOAD ADMIN PROFILE FROM BACKEND / SUPABASE
    LaunchedEffect(accessToken) {
        profileLoading = true
        profileError = null

        try {
            val response = ApiClient.getAdminProfile(accessToken)

            if (response.optString("status") == "OK") {
                val profile = response.optJSONObject("profile")

                if (profile != null) {
                    fullName = profile.optString("full_name")
                        .takeIf { it.isNotBlank() }
                        ?: "System Admin"

                    email = profile.optString("email")
                        .takeIf { it.isNotBlank() }
                        ?: ""

                    phoneNumber = profile.optString("phone")
                        .takeUnless {
                            it.isBlank() || it.equals("null", ignoreCase = true)
                        } ?: "Not provided"
                } else {
                    profileError = "Profile data is unavailable."
                }
            } else {
                profileError = response.optString(
                    "message",
                    "Could not load admin profile."
                )
            }
        } catch (e: Exception) {
            profileError = e.message
                ?: "Could not connect to the backend."
        } finally {
            profileLoading = false
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoLightGreen)
    ) {
        ProfileSidebar(
            onDashboardClick = onDashboardClick,
            onWasteReportClick = onWasteReportClick,
            onCollectorsClick = onCollectorsClick,
            onResidentsClick = onResidentsClick,
            onScheduleClick = onScheduleClick,
            onAnalyticsClick = onAnalyticsClick,
            onWasteGuideClick = onWasteGuideClick
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            ProfileTopBar(
                onNotificationClick = onNotificationClick
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 30.dp,
                        vertical = 18.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "My Profile",
                    color = EcoTextGreen,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                // PROFILE SUMMARY CARD
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            White,
                            RoundedCornerShape(14.dp)
                        )
                        .border(
                            1.dp,
                            EcoBorder,
                            RoundedCornerShape(14.dp)
                        )
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(EcoGreen)
                            .clickable {
                                imagePickerLauncher.launch("image/*")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (profileImageUri != null) {
                            AsyncImage(
                                model = profileImageUri,
                                contentDescription = "Profile Picture",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text(
                                text = initials(fullName),
                                color = White,
                                fontSize = 23.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(Modifier.width(16.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = fullName,
                            color = EcoTextGreen,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = email,
                            color = EcoGreen,
                            fontSize = 11.sp
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = if (profileLoading) {
                                "Contact: Loading..."
                            } else {
                                "Contact: $phoneNumber"
                            },
                            color = EcoGreen,
                            fontSize = 11.sp
                        )

                        profileError?.let { message ->
                            Spacer(Modifier.height(3.dp))
                            Text(
                                text = message,
                                color = Red,
                                fontSize = 10.sp
                            )
                        }

                        saveProfileSuccess?.let { message ->
                            Spacer(Modifier.height(3.dp))
                            Text(
                                text = message,
                                color = EcoGreen,
                                fontSize = 10.sp
                            )
                        }

                        saveProfileError?.let { message ->
                            Spacer(Modifier.height(3.dp))
                            Text(
                                text = message,
                                color = Red,
                                fontSize = 10.sp
                            )
                        }

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = "Administrator",
                            color = Color.Gray,
                            fontSize = 11.sp
                        )
                    }

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(9.dp))
                            .background(White)
                            .border(
                                1.dp,
                                EcoBorder,
                                RoundedCornerShape(9.dp)
                            )
                            .clickable {
                                saveProfileError = null
                                saveProfileSuccess = null
                                showEditProfile = true
                            }
                            .padding(
                                horizontal = 12.dp,
                                vertical = 8.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile",
                            tint = EcoGreen,
                            modifier = Modifier.size(14.dp)
                        )

                        Spacer(Modifier.width(5.dp))

                        Text(
                            text = "Edit",
                            color = EcoGreen,
                            fontSize = 12.sp
                        )
                    }
                }

                ProfileOptionButton(
                    icon = Icons.Default.Lock,
                    title = "Change Password",
                    onClick = {
                        showPasswordDialog = true
                    }
                )

                ProfileLogoutButton(
                    onClick = {
                        showLogoutDialog = true
                    }
                )
            }
        }
    }

    // EDIT PROFILE POPUP
    if (showEditProfile) {
        EditProfileDialog(
            currentName = fullName,
            currentEmail = email,
            currentPhone = phoneNumber,
            profileImageUri = profileImageUri,
            isSaving = isSavingProfile,
            onPickImage = {
                imagePickerLauncher.launch("image/*")
            },
            onDismiss = {
                if (!isSavingProfile) {
                    showEditProfile = false
                    saveProfileError = null
                }
            },
            onSave = { updatedName, _, updatedPhone ->
                coroutineScope.launch {
                    isSavingProfile = true
                    saveProfileError = null
                    saveProfileSuccess = null

                    try {
                        val response = ApiClient.updateAdminProfile(
                            accessToken = accessToken,
                            fullName = updatedName,
                            phone = updatedPhone
                        )

                        if (
                            response.optString("status")
                                .equals("OK", ignoreCase = true)
                        ) {
                            val profile = response.optJSONObject("profile")

                            if (profile != null) {
                                fullName = profile.optString(
                                    "full_name",
                                    updatedName
                                ).ifBlank { updatedName }

                                email = profile.optString(
                                    "email",
                                    email
                                ).ifBlank { email }

                                phoneNumber = profile.optString(
                                    "phone",
                                    updatedPhone
                                ).takeUnless {
                                    it.isBlank() ||
                                            it.equals("null", ignoreCase = true)
                                } ?: "Not provided"
                                    .takeUnless {
                                    it.isBlank() ||
                                            it.equals("null", ignoreCase = true)
                                } ?: "Not provided"

                                showEditProfile = false
                                saveProfileSuccess =
                                    "Profile updated successfully."
                            } else {
                                saveProfileError =
                                    "Profile was saved, but response data is missing."
                            }
                        } else {
                            saveProfileError = response.optString(
                                "message",
                                "Could not save profile changes."
                            )
                        }
                    } catch (e: Exception) {
                        saveProfileError = e.message
                            ?: "Could not save profile. Please try again."
                    } finally {
                        isSavingProfile = false
                    }
                }
            }
        )
    }

    // CHANGE PASSWORD POPUP
    if (showPasswordDialog) {
        ChangePasswordDialog(
            onDismiss = {
                showPasswordDialog = false
            },
            onSave = { currentPassword, newPassword, confirmPassword ->
                ApiClient.changePassword(
                    accessToken = accessToken,
                    currentPassword = currentPassword,
                    newPassword = newPassword,
                    confirmPassword = confirmPassword
                )
            }
        )
    }

    // LOGOUT CONFIRMATION
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = {
                showLogoutDialog = false
            },
            containerColor = White,
            title = {
                Text(
                    text = "Log out?",
                    color = EcoTextGreen
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to exit the admin account?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogoutClick()
                    }
                ) {
                    Text(
                        text = "Log out",
                        color = Red
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                    }
                ) {
                    Text(
                        text = "Cancel",
                        color = EcoGreen
                    )
                }
            }
        )
    }
}

// =========================================================
// EDIT PROFILE POPUP
// =========================================================

@Composable
private fun EditProfileDialog(
    currentName: String,
    currentEmail: String,
    currentPhone: String,
    profileImageUri: Uri?,
    isSaving: Boolean,
    onPickImage: () -> Unit,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var name by remember(currentName) {
        mutableStateOf(currentName)
    }

    var email by remember(currentEmail) {
        mutableStateOf(currentEmail)
    }

    var phone by remember(currentPhone) {
        mutableStateOf(
            if (currentPhone == "Not provided") "" else currentPhone
        )
    }

    Dialog(
        onDismissRequest = {
            if (!isSaving) onDismiss()
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .width(570.dp),
            shape = RoundedCornerShape(16.dp),
            color = White,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Text(
                    text = "Profile Information",
                    color = EcoTextGreen,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF00883E))
                            .clickable(enabled = !isSaving) {
                                onPickImage()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (profileImageUri != null) {
                            AsyncImage(
                                model = profileImageUri,
                                contentDescription = "Profile Picture",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text(
                                text = initials(name),
                                color = White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(Modifier.width(14.dp))

                    Column {
                        Text(
                            text = name.ifBlank { "System Admin" },
                            color = Color(0xFF123D27),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(Modifier.height(3.dp))

                        Text(
                            text = email,
                            color = EcoGreen,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(Modifier.height(15.dp))

                ProfileInputLabel("Full Name")

                ProfileRoundedInput(
                    value = name,
                    placeholder = "Enter full name",
                    onValueChange = {
                        name = it
                    }
                )

                Spacer(Modifier.height(10.dp))

                ProfileInputLabel("Email Address")

                ProfileRoundedInput(
                    value = email,
                    placeholder = "Enter email address",
                    onValueChange = {
                        email = it
                    }
                )

                Spacer(Modifier.height(10.dp))

                ProfileInputLabel("Phone Number")

                ProfileRoundedInput(
                    value = phone,
                    placeholder = "Enter phone number",
                    onValueChange = {
                        phone = it
                    }
                )

                Spacer(Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(White)
                            .border(
                                1.dp,
                                EcoBorder,
                                RoundedCornerShape(22.dp)
                            )
                            .clickable(enabled = !isSaving) {
                                onDismiss()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Cancel",
                            color = EcoGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(Color(0xFF00883E))
                            .clickable(enabled = !isSaving) {
                                if (name.isBlank()) {
                                    return@clickable
                                }

                                if (phone.isNotBlank()) {
                                    onSave(
                                        name.trim(),
                                        email.trim(),
                                        phone.trim()
                                    )
                                }
                            }
                            .padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isSaving) {
                            androidx.compose.material3.CircularProgressIndicator(
                                modifier = Modifier.size(13.dp),
                                color = White,
                                strokeWidth = 2.dp
                            )
                            Spacer(Modifier.width(6.dp))
                        } else {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = null,
                                tint = White,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                        }

                        Text(
                            text = if (isSaving) "Saving..." else "Save Changes",
                            color = White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                if (name.isBlank()) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Full name is required.",
                        color = Red,
                        fontSize = 10.sp
                    )
                }

                if (phone.isBlank()) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Phone number is required.",
                        color = Red,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

// =========================================================
// PROFILE INPUT LABEL
// =========================================================

@Composable
private fun ProfileInputLabel(text: String) {
    Text(
        text = text,
        color = EcoGreen,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    )

    Spacer(Modifier.height(5.dp))
}

// =========================================================
// ROUNDED PROFILE INPUT
// =========================================================

@Composable
private fun ProfileRoundedInput(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(37.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(FieldBackground)
            .border(
                width = 1.dp,
                color = EcoBorder,
                shape = RoundedCornerShape(22.dp)
            )
            .padding(horizontal = 11.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(
                color = EcoTextGreen,
                fontSize = 12.sp
            ),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }

                    innerTextField()
                }
            }
        )
    }
}

// =========================================================
// CHANGE PASSWORD POPUP
// =========================================================

@Composable
private fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    onSave: suspend (
        String,
        String,
        String
    ) -> org.json.JSONObject
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Dialog(
        onDismissRequest = {
            if (!isSaving) onDismiss()
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = White,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Change Password",
                        color = EcoTextGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = {
                            if (!isSaving) onDismiss()
                        },
                        enabled = !isSaving,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = EcoTextGreen
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                PasswordInput(
                    label = "Current Password",
                    value = currentPassword,
                    visible = showCurrentPassword,
                    onValueChange = {
                        currentPassword = it
                        errorMessage = ""
                    },
                    onToggleVisibility = {
                        showCurrentPassword = !showCurrentPassword
                    }
                )

                Spacer(Modifier.height(12.dp))

                PasswordInput(
                    label = "New Password",
                    value = newPassword,
                    visible = showNewPassword,
                    onValueChange = {
                        newPassword = it
                        errorMessage = ""
                    },
                    onToggleVisibility = {
                        showNewPassword = !showNewPassword
                    }
                )

                Spacer(Modifier.height(12.dp))

                PasswordInput(
                    label = "Confirm Password",
                    value = confirmPassword,
                    visible = showConfirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        errorMessage = ""
                    },
                    onToggleVisibility = {
                        showConfirmPassword = !showConfirmPassword
                    }
                )

                if (errorMessage.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = errorMessage,
                        color = Red,
                        fontSize = 12.sp
                    )
                }

                if (successMessage.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = successMessage,
                        color = EcoGreen,
                        fontSize = 12.sp
                    )
                }

                Spacer(Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        enabled = !isSaving,
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = White,
                            contentColor = EcoGreen
                        ),
                        shape = RoundedCornerShape(22.dp),
                        border = BorderStroke(1.dp, EcoBorder)
                    ) {
                        Text(
                            text = "Cancel",
                            fontSize = 12.sp
                        )
                    }

                    Button(
                        onClick = {
                            if (successMessage.isNotBlank()) {
                                onDismiss()
                                return@Button
                            }

                            when {
                                currentPassword.isBlank() ||
                                        newPassword.isBlank() ||
                                        confirmPassword.isBlank() -> {
                                    errorMessage =
                                        "Please complete all password fields."
                                }

                                newPassword.length < 8 -> {
                                    errorMessage =
                                        "New password must be at least 8 characters."
                                }

                                newPassword != confirmPassword -> {
                                    errorMessage =
                                        "New password and confirmation do not match."
                                }

                                currentPassword == newPassword -> {
                                    errorMessage =
                                        "Your new password must be different."
                                }

                                else -> {
                                    coroutineScope.launch {
                                        isSaving = true
                                        errorMessage = ""

                                        try {
                                            val response = onSave(
                                                currentPassword,
                                                newPassword,
                                                confirmPassword
                                            )

                                            if (
                                                response.optString("status")
                                                    .equals(
                                                        "ERROR",
                                                        ignoreCase = true
                                                    )
                                            ) {
                                                errorMessage =
                                                    response.optString(
                                                        "message",
                                                        "Could not change password."
                                                    )
                                            } else {
                                                successMessage =
                                                    response.optString(
                                                        "message",
                                                        "Password changed successfully."
                                                    )

                                                currentPassword = ""
                                                newPassword = ""
                                                confirmPassword = ""
                                            }
                                        } catch (e: Exception) {
                                            errorMessage = e.message
                                                ?: "Could not change password. Please try again."
                                        } finally {
                                            isSaving = false
                                        }
                                    }
                                }
                            }
                        },
                        enabled = !isSaving,
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00883E)
                        ),
                        shape = RoundedCornerShape(22.dp)
                    ) {
                        Text(
                            text = when {
                                isSaving -> "Saving..."
                                successMessage.isNotBlank() -> "Done"
                                else -> "Save Changes"
                            },
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

// =========================================================
// PASSWORD INPUT WITH SHOW/HIDE BUTTON
// =========================================================

@Composable
private fun PasswordInput(
    label: String,
    value: String,
    visible: Boolean,
    onValueChange: (String) -> Unit,
    onToggleVisibility: () -> Unit
) {
    Column {
        ProfileInputLabel(label)

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(9.dp),
            visualTransformation = if (visible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(
                    onClick = onToggleVisibility
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
                        tint = EcoGreen
                    )
                }
            }
        )
    }
}

// =========================================================
// SIDEBAR
// =========================================================

@Composable
private fun ProfileSidebar(
    onDashboardClick: () -> Unit,
    onWasteReportClick: () -> Unit,
    onCollectorsClick: () -> Unit,
    onResidentsClick: () -> Unit,
    onScheduleClick: () -> Unit,
    onAnalyticsClick: () -> Unit,
    onWasteGuideClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(250.dp)
            .fillMaxHeight()
            .background(EcoDarkGreen)
            .padding(bottom = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp)
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "♧EcoTrack",
                color = White,
                fontSize = 19.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFF4A8A50))
                .padding(
                    horizontal = 18.dp,
                    vertical = 12.dp
                )
        ) {
            Text(
                text = "ADMINISTRATOR",
                color = Color(0xFF9FD6A5),
                fontSize = 11.sp
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "System Admin",
                color = White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "admin@ecotrack.gov.ph",
                color = Color(0xFFB9DDBD),
                fontSize = 10.sp
            )
        }

        Spacer(Modifier.height(20.dp))

        ProfileSidebarItem(
            icon = Icons.Default.GridView,
            title = "Dashboard",
            onClick = onDashboardClick
        )

        ProfileSidebarItem(
            icon = Icons.Default.Description,
            title = "Waste Report",
            onClick = onWasteReportClick
        )

        ProfileSidebarItem(
            icon = Icons.Default.LocalShipping,
            title = "Collectors",
            onClick = onCollectorsClick
        )

        ProfileSidebarItem(
            icon = Icons.Default.Groups,
            title = "Residents",
            onClick = onResidentsClick
        )

        ProfileSidebarItem(
            icon = Icons.Default.CalendarMonth,
            title = "Schedules",
            onClick = onScheduleClick
        )

        ProfileSidebarItem(
            icon = Icons.Default.BarChart,
            title = "Analytics",
            onClick = onAnalyticsClick
        )

        ProfileSidebarItem(
            icon = Icons.Default.MenuBook,
            title = "Waste Guide",
            onClick = onWasteGuideClick
        )
    }
}

// =========================================================
// SIDEBAR ITEM
// =========================================================

@Composable
private fun ProfileSidebarItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 15.dp,
                vertical = 4.dp
            )
            .height(42.dp)
            .clip(RoundedCornerShape(22.dp))
            .clickable {
                onClick()
            }
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = White,
            modifier = Modifier.size(19.dp)
        )

        Spacer(Modifier.width(14.dp))

        Text(
            text = title,
            color = White,
            fontSize = 13.sp
        )
    }
}

// =========================================================
// TOP BAR
// =========================================================

@Composable
private fun ProfileTopBar(
    onNotificationClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp)
            .background(EcoTopBar)
            .padding(horizontal = 25.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(15.dp))

        Text(
            text = "My Profile",
            color = EcoDarkGreen,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.weight(1f))

        IconButton(
            onClick = onNotificationClick
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = Color.Black,
                modifier = Modifier.size(23.dp)
            )
        }
    }
}

// =========================================================
// PROFILE OPTION BUTTON
// =========================================================

@Composable
private fun ProfileOptionButton(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(
                White,
                RoundedCornerShape(14.dp)
            )
            .border(
                1.dp,
                EcoBorder,
                RoundedCornerShape(14.dp)
            )
            .clickable {
                onClick()
            }
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = EcoGreen,
            modifier = Modifier.size(22.dp)
        )

        Spacer(Modifier.width(14.dp))

        Text(
            text = title,
            color = EcoTextGreen,
            fontSize = 13.sp
        )

        Spacer(Modifier.weight(1f))

        Text(
            text = "›",
            color = Color.Gray,
            fontSize = 24.sp
        )
    }
}

// =========================================================
// LOGOUT BUTTON
// =========================================================

@Composable
private fun ProfileLogoutButton(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clip(RoundedCornerShape(14.dp))
            .border(
                1.dp,
                Color(0xFFD6AAA8),
                RoundedCornerShape(14.dp)
            )
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Logout,
            contentDescription = "Log out",
            tint = Red,
            modifier = Modifier.size(20.dp)
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = "Log out",
            color = Red,
            fontSize = 13.sp
        )
    }
}

// =========================================================
// PROFILE INITIALS
// =========================================================

private fun initials(name: String): String {
    return name.trim()
        .split(Regex("\\s+"))
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") {
            it.first().uppercase()
        }
        .ifBlank { "SA" }
}
