package edp.app.ecotrack.screens

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edp.app.ecotrack.ui.theme.EcoDarkGreen
import edp.app.ecotrack.ui.theme.EcoGreen
import edp.app.ecotrack.ui.theme.EcoLightGreen
import edp.app.ecotrack.ui.theme.EcoSubmitGreen
import edp.app.ecotrack.ui.theme.EcoTextGreen

@Composable
fun ProfileScreen(
    onNotificationsClick: () -> Unit = {}
) {

    var showChangePassword by remember {
        mutableStateOf(false)
    }

    var showContactBarangay by remember {
        mutableStateOf(false)
    }

    var showMyBarangay by remember {
        mutableStateOf(false)
    }

    var showDeleteAccount by remember {
        mutableStateOf(false)
    }

    var showLogout by remember {
        mutableStateOf(false)
    }

    var showEditProfile by remember {
        mutableStateOf(false)
    }

    var fullName by remember {
        mutableStateOf("Shania Castro")
    }

    var email by remember {
        mutableStateOf("shaniacastro@gmail.com")
    }

    var mobileNumber by remember {
        mutableStateOf("0917 814 2210")
    }

    var homeAddress by remember {
        mutableStateOf("Zone 10, Barangay Bulua")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoLightGreen)
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

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Manage your resident information and account settings",
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Box {

                IconButton(
                    onClick = onNotificationsClick
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsNone,
                        contentDescription = "Notifications",
                        tint = Color.White,
                        modifier = Modifier.size(27.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF6B6B))
                        .align(Alignment.TopEnd)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    start = 20.dp,
                    top = 20.dp,
                    end = 20.dp,
                    bottom = 30.dp
                )
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
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

                        Spacer(
                            modifier = Modifier.size(12.dp)
                        )

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = fullName,
                                color = EcoDarkGreen,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier = Modifier.height(2.dp)
                            )

                            Text(
                                text = "Resident",
                                color = EcoGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(
                                modifier = Modifier.height(2.dp)
                            )

                            Text(
                                text = "Barangay Bulua",
                                color = EcoTextGreen,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(13.dp)
                    )

                    OutlinedButton(
                        onClick = {
                            showEditProfile = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(9.dp),
                        border = BorderStroke(
                            width = 1.dp,
                            color = EcoGreen
                        ),
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

                        Spacer(
                            modifier = Modifier.size(7.dp)
                        )

                        Text(
                            text = "Edit Profile",
                            fontSize = 13.sp
                        )
                    }
                }
            }


            Spacer(
                modifier = Modifier.height(22.dp)
            )


            Text(
                text = "Account",
                color = EcoDarkGreen,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

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


            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Community",
                color = EcoDarkGreen,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

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


            Spacer(
                modifier = Modifier.height(12.dp)
            )

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


            Spacer(
                modifier = Modifier.height(22.dp)
            )

            Text(
                text = "Session",
                color = EcoDarkGreen,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

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


            Spacer(
                modifier = Modifier.height(14.dp)
            )

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


            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "EcoTrack",
                modifier = Modifier.fillMaxWidth(),
                color = EcoTextGreen,
                fontSize = 11.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = "Community waste reporting and environmental services",
                modifier = Modifier.fillMaxWidth(),
                color = EcoTextGreen,
                fontSize = 9.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

        if (showEditProfile) {

            EditProfileDialog(
                onDismiss = {
                    showEditProfile = false
                },
                onSave = { newName, newEmail, newMobile, newAddress ->

                    fullName = newName
                    email = newEmail
                    mobileNumber = newMobile
                    homeAddress = newAddress

                    showEditProfile = false
                }
            )
        }

        if (showChangePassword) {

            ChangePasswordDialog(
                onDismiss = {
                    showChangePassword = false
                }
            )
        }

        if (showContactBarangay) {

            ContactBarangayDialog(
                onDismiss = {
                    showContactBarangay = false
                }
            )
        }

        if (showMyBarangay) {

            MyBarangayDialog(
                onDismiss = {
                    showMyBarangay = false
                }
            )
        }

        if (showLogout) {

            LogoutDialog(
                onDismiss = {
                    showLogout = false
                },
                onLogout = {

                    showLogout = false
                }
            )
        }

        if (showDeleteAccount) {

            DeleteAccountDialog(
                onDismiss = {
                    showDeleteAccount = false
                },
                onDelete = {

                    showDeleteAccount = false
                }
            )
        }
    }
}

@Composable
private fun ProfileOptionCard(
    icon: ImageVector,
    title: String,
    description: String,
    iconBgColor: Color = EcoLightGreen,
    iconTint: Color = EcoDarkGreen,
    titleColor: Color = EcoDarkGreen,
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
            containerColor = Color.White
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

            Spacer(
                modifier = Modifier.size(13.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    color = titleColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = description,
                    color = EcoTextGreen,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }

            Spacer(
                modifier = Modifier.size(8.dp)
            )

            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = "Open $title",
                tint = arrowTint,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}
