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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

// =========================================================
// ECO TRACK COLORS
// Same colors as DashboardScreen
// =========================================================

private val EcoDarkGreen = Color(0xFF216B2A)
private val EcoGreen = Color(0xFF2E7D32)
private val EcoLightGreen = Color(0xFFE6F5EA)
private val EcoBorder = Color(0xFF8BE5A7)
private val EcoTextGreen = Color(0xFF286B32)
private val White = Color.White
private val Red = Color(0xFFB94A48)

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

    // =====================================================
    // PROFILE DATA
    // =====================================================

    var fullName by remember {
        mutableStateOf("System Admin")
    }

    var email by remember {
        mutableStateOf("admin@ecotrack.gov.ph")
    }

    var role by remember {
        mutableStateOf("Administrator")
    }

    var isEditing by remember {
        mutableStateOf(false)
    }

    // =====================================================
    // DIALOG STATES
    // =====================================================

    var showAnnouncementDialog by remember {
        mutableStateOf(false)
    }

    var showPasswordDialog by remember {
        mutableStateOf(false)
    }

    var showSettingsDialog by remember {
        mutableStateOf(false)
    }

    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    // =====================================================
    // PROFILE IMAGE
    // =====================================================

    var profileImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->

            if (uri != null) {
                profileImageUri = uri
            }
        }

    // =====================================================
    // MAIN SCREEN
    // EXACT SAME OVERALL STRUCTURE AS DASHBOARD
    // =====================================================

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoLightGreen)
    ) {

        // =================================================
        // SIDEBAR
        // EXACT DASHBOARD SIDEBAR WIDTH = 250.dp
        // =================================================

        ProfileSidebar(
            onDashboardClick = onDashboardClick,
            onWasteReportClick = onWasteReportClick,
            onCollectorsClick = onCollectorsClick,
            onResidentsClick = onResidentsClick,
            onScheduleClick = onScheduleClick,
            onAnalyticsClick = onAnalyticsClick,
            onWasteGuideClick = onWasteGuideClick
        )

        // =================================================
        // MAIN CONTENT
        // SAME AS DASHBOARD
        // =================================================

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {

            // =================================================
            // TOP BAR
            // EXACT DASHBOARD HEIGHT = 62.dp
            // =================================================

            ProfileTopBar(
                onNotificationClick = onNotificationClick
            )

            // =================================================
            // PROFILE CONTENT
            //
            // Dashboard:
            // horizontal = 30.dp
            // vertical = 18.dp
            // spacing = 16.dp
            // =================================================

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 30.dp,
                        vertical = 18.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // =================================================
                // TITLE
                // SAME TITLE SIZE AS DASHBOARD
                // =================================================

                Text(
                    text = "My Profile",
                    color = EcoTextGreen,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                // =================================================
                // PROFILE INFORMATION CARD
                // =================================================

                ProfileInformationCard(
                    fullName = fullName,
                    email = email,
                    role = role,
                    isEditing = isEditing,
                    profileImageUri = profileImageUri,
                    onImageClick = {
                        imagePickerLauncher.launch("image/*")
                    },
                    onNameChange = {
                        fullName = it
                    },
                    onEditClick = {
                        isEditing = true
                    },
                    onCancelClick = {
                        isEditing = false
                    },
                    onSaveClick = {
                        isEditing = false
                    }
                )

                // =================================================
                // SEND ANNOUNCEMENT
                // =================================================

                ProfileOptionButton(
                    icon = Icons.Default.Campaign,
                    title = "Send Announcement",
                    onClick = {
                        showAnnouncementDialog = true
                    }
                )

                // =================================================
                // CHANGE PASSWORD
                // =================================================

                ProfileOptionButton(
                    icon = Icons.Default.Lock,
                    title = "Change Password",
                    onClick = {
                        showPasswordDialog = true
                    }
                )

                // =================================================
                // SYSTEM SETTINGS
                // =================================================

                ProfileOptionButton(
                    icon = Icons.Default.Settings,
                    title = "System Settings",
                    onClick = {
                        showSettingsDialog = true
                    }
                )

                // =================================================
                // LOG OUT
                // =================================================

                LogoutButton(
                    onClick = {
                        showLogoutDialog = true
                    }
                )
            }
        }
    }

    // =========================================================
    // ANNOUNCEMENT DIALOG
    // =========================================================

    if (showAnnouncementDialog) {

        AnnouncementDialog(
            onDismiss = {
                showAnnouncementDialog = false
            }
        )
    }

    // =========================================================
    // CHANGE PASSWORD DIALOG
    // =========================================================

    if (showPasswordDialog) {

        ChangePasswordDialog(
            onDismiss = {
                showPasswordDialog = false
            }
        )
    }

    // =========================================================
    // SYSTEM SETTINGS DIALOG
    // =========================================================

    if (showSettingsDialog) {

        SystemSettingsDialog(
            onDismiss = {
                showSettingsDialog = false
            }
        )
    }

    // =========================================================
    // LOGOUT DIALOG
    // =========================================================

    if (showLogoutDialog) {

        AlertDialog(
            onDismissRequest = {
                showLogoutDialog = false
            },

            title = {
                Text("Log out?")
            },

            text = {
                Text(
                    "Are you sure you want to exit the admin account?"
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

                    Text("Cancel")
                }
            }
        )
    }
}

// =========================================================
// PROFILE SIDEBAR
// SAME DIMENSIONS AS DASHBOARD SIDEBAR
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

        // =================================================
        // ECOTRACK LOGO
        // EXACT DASHBOARD = 62.dp
        // =================================================

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

        // =================================================
        // ADMIN INFORMATION
        // EXACT DASHBOARD DIMENSIONS
        // =================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color(0xFF4A8A50)
                )
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

            Spacer(
                modifier = Modifier.height(6.dp)
            )

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

        // =================================================
        // SAME DASHBOARD SPACING
        // =================================================

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // =================================================
        // DASHBOARD
        // =================================================

        ProfileSidebarItem(
            icon = Icons.Default.GridView,
            title = "Dashboard",
            selected = false,
            onClick = onDashboardClick
        )

        // =================================================
        // WASTE REPORT
        // =================================================

        ProfileSidebarItem(
            icon = Icons.Default.Description,
            title = "Waste Report",
            onClick = onWasteReportClick
        )

        // =================================================
        // COLLECTORS
        // =================================================

        ProfileSidebarItem(
            icon = Icons.Default.LocalShipping,
            title = "Collectors",
            onClick = onCollectorsClick
        )

        // =================================================
        // RESIDENTS
        // =================================================

        ProfileSidebarItem(
            icon = Icons.Default.Groups,
            title = "Residents",
            onClick = onResidentsClick
        )

        // =================================================
        // SCHEDULES
        // =================================================

        ProfileSidebarItem(
            icon = Icons.Default.CalendarMonth,
            title = "Schedules",
            onClick = onScheduleClick
        )

        // =================================================
        // ANALYTICS
        // =================================================

        ProfileSidebarItem(
            icon = Icons.Default.BarChart,
            title = "Analytics",
            onClick = onAnalyticsClick
        )

        // =================================================
        // WASTE GUIDE
        // =================================================

        ProfileSidebarItem(
            icon = Icons.Default.MenuBook,
            title = "Waste Guide",
            onClick = onWasteGuideClick
        )
    }
}

// =========================================================
// SIDEBAR ITEM
// EXACT SAME DIMENSIONS AS DASHBOARD
// =========================================================

@Composable
private fun ProfileSidebarItem(
    icon: ImageVector,
    title: String,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 15.dp,
                vertical = 4.dp
            )
            .height(42.dp)
            .background(
                color = if (selected) {
                    Color(0xFF719873)
                } else {
                    Color.Transparent
                },
                shape = RoundedCornerShape(22.dp)
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
            tint = White,
            modifier = Modifier.size(19.dp)
        )

        Spacer(
            modifier = Modifier.width(14.dp)
        )

        Text(
            text = title,
            color = White,
            fontSize = 13.sp
        )
    }
}

// =========================================================
// TOP BAR
// SAME DIMENSIONS AS DASHBOARD
// =========================================================

@Composable
private fun ProfileTopBar(
    onNotificationClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp)
            .background(
                Color(0xFFE0F2E5)
            )
            .padding(horizontal = 25.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // =================================================
        // MENU
        // =================================================

        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            tint = EcoDarkGreen,
            modifier = Modifier.size(23.dp)
        )

        Spacer(
            modifier = Modifier.width(15.dp)
        )

        // =================================================
        // TITLE
        // =================================================

        Text(
            text = "My Profile",
            color = EcoDarkGreen,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        // =================================================
        // NOTIFICATION
        // EXACT DASHBOARD SIZE = 23.dp
        // =================================================

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
// PROFILE INFORMATION CARD
// =========================================================

@Composable
private fun ProfileInformationCard(
    fullName: String,
    email: String,
    role: String,
    isEditing: Boolean,
    profileImageUri: Uri?,
    onImageClick: () -> Unit,
    onNameChange: (String) -> Unit,
    onEditClick: () -> Unit,
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = White,
                shape = RoundedCornerShape(14.dp)
            )
            .border(
                width = 1.dp,
                color = EcoBorder,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // =================================================
        // PROFILE PICTURE
        // =================================================

        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(EcoDarkGreen)
                .clickable {
                    onImageClick()
                },
            contentAlignment = Alignment.Center
        ) {

            if (profileImageUri != null) {

                AsyncImage(
                    model = profileImageUri,
                    contentDescription = "Admin Profile Picture",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

            } else {

                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Admin",
                    tint = White,
                    modifier = Modifier.size(38.dp)
                )
            }
        }

        Spacer(
            modifier = Modifier.width(18.dp)
        )

        // =================================================
        // PROFILE DETAILS
        // =================================================

        Column(
            modifier = Modifier.weight(1f)
        ) {

            if (isEditing) {

                OutlinedTextField(
                    value = fullName,
                    onValueChange = onNameChange,
                    label = {
                        Text("Full Name")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

            } else {

                Text(
                    text = fullName,
                    color = EcoTextGreen,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = email,
                color = Color(0xFF4E8056),
                fontSize = 11.sp
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = "$role • Since Jan 2027",
                color = Color(0xFF4E8056),
                fontSize = 11.sp
            )
        }

        Spacer(
            modifier = Modifier.width(16.dp)
        )

        // =================================================
        // EDIT / SAVE
        // =================================================

        if (!isEditing) {

            Row(
                modifier = Modifier
                    .clickable {
                        onEditClick()
                    }
                    .background(
                        color = White,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color(0xFFD0D0D0),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(
                        horizontal = 12.dp,
                        vertical = 8.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(14.dp)
                )

                Spacer(
                    modifier = Modifier.width(6.dp)
                )

                Text(
                    text = "Edit",
                    color = Color.DarkGray,
                    fontSize = 10.sp
                )
            }

        } else {

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                TextButton(
                    onClick = onCancelClick
                ) {

                    Text(
                        text = "Cancel",
                        fontSize = 10.sp
                    )
                }

                Button(
                    onClick = onSaveClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EcoGreen
                    )
                ) {

                    Text(
                        text = "Save",
                        fontSize = 10.sp
                    )
                }
            }
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
                color = White,
                shape = RoundedCornerShape(14.dp)
            )
            .border(
                width = 1.dp,
                color = EcoBorder,
                shape = RoundedCornerShape(14.dp)
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

        Spacer(
            modifier = Modifier.width(14.dp)
        )

        Text(
            text = title,
            color = EcoTextGreen,
            fontSize = 13.sp
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

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
private fun LogoutButton(
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(14.dp)
            )
            .border(
                width = 1.dp,
                color = Color(0xFFD6AAA8),
                shape = RoundedCornerShape(14.dp)
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

        Spacer(
            modifier = Modifier.width(8.dp)
        )

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

    var announcement by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text("Send Announcement")
        },

        text = {

            OutlinedTextField(
                value = announcement,
                onValueChange = {
                    announcement = it
                },
                label = {
                    Text("Announcement")
                },
                modifier = Modifier.fillMaxWidth()
            )
        },

        confirmButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text(
                    text = "Send",
                    color = EcoGreen
                )
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text("Cancel")
            }
        }
    )
}

// =========================================================
// CHANGE PASSWORD DIALOG
// =========================================================

@Composable
private fun ChangePasswordDialog(
    onDismiss: () -> Unit
) {

    var oldPassword by remember {
        mutableStateOf("")
    }

    var newPassword by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text("Change Password")
        },

        text = {

            Column {

                OutlinedTextField(
                    value = oldPassword,
                    onValueChange = {
                        oldPassword = it
                    },
                    label = {
                        Text("Current Password")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = {
                        newPassword = it
                    },
                    label = {
                        Text("New Password")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },

        confirmButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text(
                    text = "Save",
                    color = EcoGreen
                )
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text("Cancel")
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

        title = {
            Text("System Settings")
        },

        text = {
            Text(
                "System settings will be connected to the backend later."
            )
        },

        confirmButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text("OK")
            }
        }
    )
}