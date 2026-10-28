
package edp.app.ecotrack.screens.residents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edp.app.ecotrack.network.ApiClient
import edp.app.ecotrack.ui.theme.EcoDarkGreen
import edp.app.ecotrack.ui.theme.EcoGreen
import edp.app.ecotrack.ui.theme.EcoLightGreen
import edp.app.ecotrack.ui.theme.EcoTextGreen
import edp.app.ecotrack.viewmodel.ResidentDashboardViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    accessToken: String,
    onNotificationsClick: () -> Unit = {},
    onViewSchedule: () -> Unit = {},
    onLogout: () -> Unit = {},
    dashboardViewModel: ResidentDashboardViewModel
) {
    var showChangePassword by remember { mutableStateOf(false) }
    var showContactBarangay by remember { mutableStateOf(false) }
    var showMyBarangay by remember { mutableStateOf(false) }
    var showDeleteAccount by remember { mutableStateOf(false) }
    var showLogout by remember { mutableStateOf(false) }
    var showEditProfile by remember { mutableStateOf(false) }

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var homeAddress by remember { mutableStateOf("") }
    var barangayId by remember { mutableStateOf(0) }
    var barangayName by remember { mutableStateOf("") }

    var barangayOptions by remember {
        mutableStateOf<List<Pair<Int, String>>>(emptyList())
    }

    var isLoadingProfile by remember { mutableStateOf(true) }
    var isSavingProfile by remember { mutableStateOf(false) }
    var isDeletingAccount by remember { mutableStateOf(false) }
    var profileError by remember { mutableStateOf("") }
    var profileMessage by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(accessToken) {
        isLoadingProfile = true
        profileError = ""

        try {
            val result = ApiClient.getResidentProfile(accessToken)
            val user = result.getJSONObject("user")
            val barangay = result.optJSONObject("barangay")

            fullName = user.optString("full_name")
                .takeUnless { it == "null" }
                .orEmpty()

            email = user.optString("email")
                .takeUnless { it == "null" }
                .orEmpty()

            mobileNumber = user.optString("contact_number")
                .takeUnless { it == "null" }
                .orEmpty()

            homeAddress = user.optString("address")
                .takeUnless { it == "null" }
                .orEmpty()

            barangayId = user.optInt("barangay_id", 0)

            barangayName = barangay
                ?.optString("barangay_name")
                ?.takeUnless { it == "null" }
                .orEmpty()
        } catch (error: Exception) {
            profileError = error.message ?: "Unable to load your profile."
        } finally {
            isLoadingProfile = false
        }

        try {
            val result = ApiClient.getResidentBarangays(accessToken)
            val barangays = result.getJSONArray("barangays")

            barangayOptions = buildList {
                for (index in 0 until barangays.length()) {
                    val item = barangays.getJSONObject(index)
                    val id = item.optInt("barangay_id")
                    val name = item.optString("barangay_name")

                    if (id > 0 && name.isNotBlank() && name != "null") {
                        add(id to name)
                    }
                }
            }
        } catch (error: Exception) {
            if (profileError.isBlank()) {
                profileError = error.message ?: "Unable to load barangays."
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(EcoDarkGreen)
                .padding(
                    start = 24.dp,
                    end = 16.dp,
                    top = 32.dp,
                    bottom = 24.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "My Profile",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Manage your resident information and account settings",
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Box {
                IconButton(onClick = onNotificationsClick) {
                    Icon(
                        imageVector = Icons.Default.NotificationsNone,
                        contentDescription = "Notifications",
                        tint = Color.White,
                        modifier = Modifier.size(27.dp)
                    )
                }

                val hasUnread by dashboardViewModel.hasUnreadNotifications.collectAsState()
                if (hasUnread) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = (-4).dp, y = 4.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFF6B6B))
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = 20.dp,
                    top = 20.dp,
                    end = 20.dp,
                    bottom = 30.dp
                )
        ) {
            if (isLoadingProfile) {
                Text(
                    text = "Loading your profile...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    color = EcoTextGreen,
                    textAlign = TextAlign.Center,
                    fontSize = 13.sp
                )
            }

            if (profileError.isNotBlank()) {
                Text(
                    text = profileError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    fontSize = 13.sp
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE8F5E9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        Spacer(modifier = Modifier.size(12.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = fullName.ifBlank {
                                    if (isLoadingProfile) "Loading..." else "Resident"
                                },
                                color = EcoDarkGreen,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "Resident",
                                color = EcoGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = barangayName.ifBlank {
                                    if (isLoadingProfile) {
                                        "Loading barangay..."
                                    } else {
                                        "No barangay assigned"
                                    }
                                },
                                color = EcoTextGreen,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(13.dp))

                    OutlinedButton(
                        onClick = {
                            profileMessage = ""
                            showEditProfile = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(9.dp),
                        border = BorderStroke(1.dp, EcoGreen),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = EcoDarkGreen
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile",
                            tint = EcoGreen,
                            modifier = Modifier.size(17.dp)
                        )

                        Spacer(modifier = Modifier.size(7.dp))

                        Text(
                            text = "Edit Profile",
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Account",
                color = EcoDarkGreen,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            ProfileOptionCard(
                icon = Icons.Default.Lock,
                title = "Change Password",
                description = "Choose a strong password you do not use elsewhere.",
                iconBgColor = Color(0xFFE8F0FE),
                iconTint = Color(0xFF1A73E8),
                onClick = {
                    showChangePassword = true
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Community",
                color = EcoDarkGreen,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            ProfileOptionCard(
                icon = Icons.Default.Phone,
                title = "Contact Barangay Hall",
                description = "Get help with local services and concerns.",
                iconBgColor = Color(0xFFE6F4EA),
                iconTint = Color(0xFF1E8E3E),
                onClick = {
                    showContactBarangay = true
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileOptionCard(
                icon = Icons.Default.LocationOn,
                title = "My Barangay",
                description = "View your assigned community and resident information.",
                iconBgColor = Color(0xFFFEF7E0),
                iconTint = Color(0xFFF29900),
                onClick = {
                    showMyBarangay = true
                }
            )

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Session",
                color = EcoDarkGreen,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            ProfileOptionCard(
                icon = Icons.Default.Logout,
                title = "Log Out",
                description = "Sign out of your EcoTrack account.",
                iconBgColor = Color(0xFFFCE8E6),
                iconTint = Color(0xFFD93025),
                onClick = {
                    showLogout = true
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            ProfileOptionCard(
                icon = Icons.Default.Person,
                title = "Delete Account",
                description = "Permanently remove your EcoTrack account.",
                iconBgColor = Color(0xFFFFEBEE),
                iconTint = Color(0xFFB83A36),
                titleColor = Color(0xFFB83A36),
                arrowTint = Color(0xFFB83A36),
                onClick = {
                    showDeleteAccount = true
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "EcoTrack",
                modifier = Modifier.fillMaxWidth(),
                color = EcoTextGreen,
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = "Community waste reporting and environmental services",
                modifier = Modifier.fillMaxWidth(),
                color = EcoTextGreen,
                fontSize = 9.sp,
                textAlign = TextAlign.Center
            )
        }
    }

    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier.padding(16.dp)
    )

    if (showEditProfile) {
        EditProfileDialog(
            fullName = fullName,
            email = email,
            mobile = mobileNumber,
            address = homeAddress,
            currentBarangayId = barangayId,
            barangays = barangayOptions,
            isSaving = isSavingProfile,
            message = profileMessage,
            onDismiss = {
                if (!isSavingProfile) {
                    showEditProfile = false
                }
            },
            onSave = { newName, newMobile, newAddress, newBarangayId ->
                coroutineScope.launch {
                    isSavingProfile = true
                    profileMessage = ""

                    try {
                        val result = ApiClient.updateResidentProfile(
                            accessToken = accessToken,
                            fullName = newName,
                            contactNumber = newMobile,
                            address = newAddress,
                            barangayId = newBarangayId
                        )

                        val updatedUser = result.optJSONObject("user")
                        val updatedBarangay = result.optJSONObject("barangay")

                        fullName = updatedUser
                            ?.optString("full_name")
                            ?.takeUnless { it == "null" }
                            ?: newName.trim()

                        mobileNumber = updatedUser
                            ?.optString("contact_number")
                            ?.takeUnless { it == "null" }
                            ?: newMobile.trim()

                        homeAddress = updatedUser
                            ?.optString("address")
                            ?.takeUnless { it == "null" }
                            ?: newAddress.trim()

                        barangayId = updatedUser
                            ?.optInt("barangay_id", newBarangayId)
                            ?: newBarangayId

                        barangayName = updatedBarangay
                            ?.optString("barangay_name")
                            ?.takeUnless { it == "null" }
                            ?: barangayOptions.firstOrNull {
                                it.first == newBarangayId
                            }?.second.orEmpty()

                        dashboardViewModel.updateResidentProfile(
                            fullName = fullName,
                            phone = mobileNumber,
                            address = homeAddress,
                            barangay = barangayName
                        )

                        showEditProfile = false
                        snackbarHostState.showSnackbar(
                            "Profile updated successfully."
                        )
                    } catch (error: Exception) {
                        profileMessage = error.message
                            ?: "Unable to update your profile."
                    } finally {
                        isSavingProfile = false
                    }
                }
            }
        )
    }

    if (showChangePassword) {
        ChangePasswordDialog(
            accessToken = accessToken,
            onDismiss = {
                showChangePassword = false
            }
        )
    }

    if (showContactBarangay) {
        ContactBarangayDialog(
            accessToken = accessToken,
            onDismiss = {
                showContactBarangay = false
            }
        )
    }

    if (showMyBarangay) {
        MyBarangayDialog(
            accessToken = accessToken,
            onDismiss = {
                showMyBarangay = false
            },
            onViewSchedule = {
                showMyBarangay = false
                onViewSchedule()
            }
        )
    }

    if (showLogout) {
        LogoutDialog(
            residentName = fullName.ifBlank { "Resident" },
            residentEmail = email,
            onDismiss = {
                showLogout = false
            },
            onLogout = {
                showLogout = false
                onLogout()
            }
        )
    }

    if (showDeleteAccount) {
        DeleteAccountDialog(
            onDismiss = {
                if (!isDeletingAccount) {
                    showDeleteAccount = false
                }
            },
            onDelete = {
                if (!isDeletingAccount) {
                    coroutineScope.launch {
                        if (isDeletingAccount) {
                            return@launch
                        }

                        isDeletingAccount = true

                        try {
                            ApiClient.deleteResidentAccount(accessToken)
                            showDeleteAccount = false
                            onLogout()
                        } catch (exception: Exception) {
                            snackbarHostState.showSnackbar(
                                exception.message
                                    ?: "Unable to delete your account. Please try again."
                            )
                        } finally {
                            isDeletingAccount = false
                        }
                    }
                }
            },
            isDeleting = isDeletingAccount
        )
    }
}

@Composable
private fun ProfileOptionCard(
    icon: ImageVector,
    title: String,
    description: String,
    iconBgColor: Color = EcoLightGreen,
    iconTint: Color = EcoDarkGreen,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    arrowTint: Color = EcoTextGreen,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(11.dp))
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.size(13.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = titleColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = description,
                    color = EcoTextGreen,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }

            Spacer(modifier = Modifier.size(8.dp))

            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = "Open $title",
                tint = arrowTint,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}