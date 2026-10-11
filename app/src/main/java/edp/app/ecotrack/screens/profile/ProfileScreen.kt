package edp.app.ecotrack.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage

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
    var email by remember { mutableStateOf("admin@ecotrack.gov.ph") }
    var phoneNumber by remember { mutableStateOf("0917-555-0100") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    var showEditProfile by remember { mutableStateOf(false) }
    var showAnnouncementDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            if (uri != null) profileImageUri = uri
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
                    .padding(horizontal = 30.dp, vertical = 18.dp),
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
                        .background(White, RoundedCornerShape(14.dp))
                        .border(1.dp, EcoBorder, RoundedCornerShape(14.dp))
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
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
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

                    Column(modifier = Modifier.weight(1f)) {
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
                            text = "Administrator",
                            color = Color.Gray,
                            fontSize = 11.sp
                        )
                    }

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(9.dp))
                            .background(White)
                            .border(1.dp, EcoBorder, RoundedCornerShape(9.dp))
                            .clickable { showEditProfile = true }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
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
                    icon = Icons.Default.Campaign,
                    title = "Send Announcement",
                    onClick = { showAnnouncementDialog = true }
                )

                ProfileOptionButton(
                    icon = Icons.Default.Lock,
                    title = "Change Password",
                    onClick = { showPasswordDialog = true }
                )

                ProfileOptionButton(
                    icon = Icons.Default.Settings,
                    title = "System Settings",
                    onClick = { showSettingsDialog = true }
                )

                ProfileLogoutButton(
                    onClick = { showLogoutDialog = true }
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
            onPickImage = {
                imagePickerLauncher.launch("image/*")
            },
            onDismiss = {
                showEditProfile = false
            },
            onSave = { updatedName, updatedEmail, updatedPhone ->
                fullName = updatedName
                email = updatedEmail
                phoneNumber = updatedPhone
                showEditProfile = false
            }
        )
    }

    // CHANGE PASSWORD POPUP
    if (showPasswordDialog) {
        ChangePasswordDialog(
            onDismiss = {
                showPasswordDialog = false
            },
            onSave = { _, _, _ ->
                showPasswordDialog = false
            }
        )
    }

    if (showAnnouncementDialog) {
        AnnouncementDialog(
            onDismiss = { showAnnouncementDialog = false }
        )
    }

    if (showSettingsDialog) {
        SystemSettingsDialog(
            onDismiss = { showSettingsDialog = false }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            containerColor = White,
            title = {
                Text(
                    text = "Log out?",
                    color = EcoTextGreen
                )
            },
            text = {
                Text("Are you sure you want to exit the admin account?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogoutClick()
                    }
                ) {
                    Text("Log out", color = Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("Cancel", color = EcoGreen)
                }
            }
        )
    }
}

// =========================================================
// EDIT PROFILE POPUP — MATCHES YOUR SCREENSHOT
// =========================================================

@Composable
private fun EditProfileDialog(
    currentName: String,
    currentEmail: String,
    currentPhone: String,
    profileImageUri: Uri?,
    onPickImage: () -> Unit,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var name by remember(currentName) { mutableStateOf(currentName) }
    var email by remember(currentEmail) { mutableStateOf(currentEmail) }
    var phone by remember(currentPhone) { mutableStateOf(currentPhone) }

    Dialog(onDismissRequest = onDismiss) {
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
                            .clickable { onPickImage() },
                        contentAlignment = Alignment.Center
                    ) {
                        if (profileImageUri != null) {
                            AsyncImage(
                                model = profileImageUri,
                                contentDescription = "Profile Picture",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
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
                    onValueChange = { name = it }
                )

                Spacer(Modifier.height(10.dp))

                ProfileInputLabel("Email Address")
                ProfileRoundedInput(
                    value = email,
                    placeholder = "Enter email address",
                    onValueChange = { email = it }
                )

                Spacer(Modifier.height(10.dp))

                ProfileInputLabel("Phone Number")
                ProfileRoundedInput(
                    value = phone,
                    placeholder = "Enter phone number",
                    onValueChange = { phone = it }
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
                            .clickable { onDismiss() },
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
                            .clickable {
                                if (
                                    name.isNotBlank() &&
                                    email.isNotBlank() &&
                                    phone.isNotBlank()
                                ) {
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
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null,
                            tint = White,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Save Changes",
                            color = White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
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
// ROUNDED PROFILE INPUT — LIKE THE SCREENSHOT
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
// CURRENT, NEW, CONFIRM — WITH SHOW/HIDE
// =========================================================

@Composable
private fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
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
                        onClick = onDismiss,
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
                    onValueChange = { currentPassword = it },
                    onToggleVisibility = {
                        showCurrentPassword = !showCurrentPassword
                    }
                )

                Spacer(Modifier.height(12.dp))

                PasswordInput(
                    label = "New Password",
                    value = newPassword,
                    visible = showNewPassword,
                    onValueChange = { newPassword = it },
                    onToggleVisibility = {
                        showNewPassword = !showNewPassword
                    }
                )

                Spacer(Modifier.height(12.dp))

                PasswordInput(
                    label = "Confirm Password",
                    value = confirmPassword,
                    visible = showConfirmPassword,
                    onValueChange = { confirmPassword = it },
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

                Spacer(Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = White,
                            contentColor = EcoGreen
                        ),
                        shape = RoundedCornerShape(22.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            EcoBorder
                        )
                    ) {
                        Text("Cancel", fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            when {
                                currentPassword.isBlank() ||
                                        newPassword.isBlank() ||
                                        confirmPassword.isBlank() -> {
                                    errorMessage = "Please complete all password fields."
                                }

                                newPassword != confirmPassword -> {
                                    errorMessage = "New password and confirmation do not match."
                                }

                                currentPassword == newPassword -> {
                                    errorMessage = "Your new password must be different."
                                }

                                else -> {
                                    onSave(
                                        currentPassword,
                                        newPassword,
                                        confirmPassword
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00883E)
                        ),
                        shape = RoundedCornerShape(22.dp)
                    ) {
                        Text(
                            text = "Save Changes",
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
                IconButton(onClick = onToggleVisibility) {
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
                .padding(horizontal = 18.dp, vertical = 12.dp)
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
            Icons.Default.GridView,
            "Dashboard",
            onClick = onDashboardClick
        )
        ProfileSidebarItem(
            Icons.Default.Description,
            "Waste Report",
            onClick = onWasteReportClick
        )
        ProfileSidebarItem(
            Icons.Default.LocalShipping,
            "Collectors",
            onClick = onCollectorsClick
        )
        ProfileSidebarItem(
            Icons.Default.Groups,
            "Residents",
            onClick = onResidentsClick
        )
        ProfileSidebarItem(
            Icons.Default.CalendarMonth,
            "Schedules",
            onClick = onScheduleClick
        )
        ProfileSidebarItem(
            Icons.Default.BarChart,
            "Analytics",
            onClick = onAnalyticsClick
        )
        ProfileSidebarItem(
            Icons.Default.MenuBook,
            "Waste Guide",
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
            .padding(horizontal = 15.dp, vertical = 4.dp)
            .height(42.dp)
            .clip(RoundedCornerShape(22.dp))
            .clickable { onClick() }
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
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            tint = EcoDarkGreen,
            modifier = Modifier.size(23.dp)
        )

        Spacer(Modifier.width(15.dp))

        Text(
            text = "My Profile",
            color = EcoDarkGreen,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.weight(1f))

        IconButton(onClick = onNotificationClick) {
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
            .background(White, RoundedCornerShape(14.dp))
            .border(1.dp, EcoBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() }
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
            .border(1.dp, Color(0xFFD6AAA8), RoundedCornerShape(14.dp))
            .clickable { onClick() },
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
// ANNOUNCEMENT DIALOG
// =========================================================

@Composable
private fun AnnouncementDialog(
    onDismiss: () -> Unit
) {
    var announcement by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = White,
        title = {
            Text("Send Announcement", color = EcoTextGreen)
        },
        text = {
            OutlinedTextField(
                value = announcement,
                onValueChange = { announcement = it },
                label = { Text("Announcement") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Send", color = EcoGreen)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = EcoGreen)
            }
        }
    )
}

// =========================================================
// SYSTEM SETTINGS DIALOG
// =========================================================

@Composable
private fun SystemSettingsDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = White,
        title = {
            Text("System Settings", color = EcoTextGreen)
        },
        text = {
            Text(
                "System settings will be connected to the backend later."
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK", color = EcoGreen)
            }
        }
    )
}

// =========================================================
// PROFILE INITIALS
// =========================================================

private fun initials(name: String): String {
    return name.trim()
        .split(Regex("\\s+"))
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .ifBlank { "SA" }
}
