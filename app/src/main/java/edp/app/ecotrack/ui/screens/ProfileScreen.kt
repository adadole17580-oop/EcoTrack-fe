package edp.app.ecotrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edp.app.ecotrack.ui.components.EcoTrackTopBar
import edp.app.ecotrack.ui.theme.EcoBackground
import edp.app.ecotrack.ui.theme.EcoError
import edp.app.ecotrack.ui.theme.EcoGreen
import edp.app.ecotrack.ui.theme.EcoGreenLight
import edp.app.ecotrack.ui.theme.EcoSecondaryText
import edp.app.ecotrack.ui.theme.EcoText
import edp.app.ecotrack.ui.theme.EcoWhite

@Composable
fun ProfileScreen(
    onCompletedTasksClick: () -> Unit,
    onEditPasswordClick: () -> Unit,
    onContactAdminClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoBackground)
    ) {

        EcoTrackTopBar(
            title = "Profile",
            subtitle = "Manage your account"
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = EcoWhite
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        modifier = Modifier
                            .size(58.dp)
                            .background(
                                EcoGreenLight,
                                CircleShape
                            ),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Profile",
                            tint = EcoGreen,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.padding(start = 14.dp)
                    ) {
                        Text(
                            text = "Juan dela Cruz",
                            color = EcoText,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = "Garbage Collector",
                            color = EcoSecondaryText,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 2.dp)
                        )

                        Text(
                            text = "Barangay Bulua",
                            color = EcoSecondaryText,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 1.dp)
                        )
                    }
                }
            }

            ProfileOption(
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.CheckCircle,
                        contentDescription = "Completed Tasks",
                        tint = EcoGreen
                    )
                },
                title = "Completed Tasks",
                subtitle = "View completed and waiting tasks",
                onClick = onCompletedTasksClick
            )

            ProfileOption(
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Key,
                        contentDescription = "Change Password",
                        tint = EcoGreen
                    )
                },
                title = "Change Password",
                subtitle = "Update your account password",
                onClick = onEditPasswordClick
            )

            ProfileOption(
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.SupportAgent,
                        contentDescription = "Contact Admin",
                        tint = EcoGreen
                    )
                },
                title = "Contact Admin",
                subtitle = "Chat with the administrator",
                onClick = onContactAdminClick
            )

            ProfileOption(
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Logout,
                        contentDescription = "Log Out",
                        tint = EcoError
                    )
                },
                title = "Log Out",
                subtitle = "Sign out of your account",
                titleColor = EcoError,
                onClick = {
                    showLogoutDialog = true
                }
            )
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = {
                showLogoutDialog = false
            },
            title = {
                Text(
                    text = "Log Out?"
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to log out of your account?"
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
                        text = "Log Out",
                        color = EcoError
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
                        color = EcoText
                    )
                }
            }
        )
    }
}

@Composable
private fun ProfileOption(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    titleColor: Color = EcoText,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = EcoWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 15.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        if (title == "Log Out") {
                            EcoError.copy(alpha = 0.10f)
                        } else {
                            EcoGreenLight
                        },
                        CircleShape
                    ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon()
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = title,
                    color = titleColor,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = subtitle,
                    color = EcoSecondaryText,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            IconButton(
                onClick = onClick
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowForwardIos,
                    contentDescription = "Open $title",
                    tint = EcoSecondaryText,
                    modifier = Modifier.size(17.dp)
                )
            }
        }
    }
}