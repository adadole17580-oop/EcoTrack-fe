package edp.app.ecotrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edp.app.ecotrack.ui.components.EcoTrackTopBar
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

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        EcoTrackTopBar(
            title = "Profile",
            subtitle = "Manage your account"
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            ProfileHeaderCard()

            ProfileOptionCard(
                icon = Icons.Outlined.CheckCircle,
                title = "Completed Tasks",
                subtitle = "View completed and waiting tasks",
                onClick = onCompletedTasksClick
            )

            ProfileOptionCard(
                icon = Icons.Outlined.Lock,
                title = "Change Password",
                subtitle = "Update your account password",
                onClick = onEditPasswordClick
            )

            ProfileOptionCard(
                icon = Icons.Outlined.Chat,
                title = "Contact Admin",
                subtitle = "Chat with the administrator",
                onClick = onContactAdminClick
            )

            Spacer(
                modifier = Modifier.size(4.dp)
            )

            LogoutCard(
                onClick = onLogoutClick
            )

            Spacer(
                modifier = Modifier.size(8.dp)
            )
        }
    }
}

@Composable
private fun ProfileHeaderCard() {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = EcoWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        color = EcoGreenLight,
                        shape = CircleShape
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Profile picture",
                    tint = EcoGreen,
                    modifier = Modifier.size(34.dp)
                )
            }

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Garbage Collector",
                    style = MaterialTheme.typography.titleLarge,
                    color = EcoText
                )

                Text(
                    text = "Collector Account",
                    style = MaterialTheme.typography.bodySmall,
                    color = EcoGreen,
                    modifier = Modifier.padding(top = 3.dp)
                )

                Text(
                    text = "EcoTrack",
                    style = MaterialTheme.typography.bodySmall,
                    color = EcoSecondaryText,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }
        }
    }
}

@Composable
private fun ProfileOptionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
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
                .padding(
                    horizontal = 16.dp,
                    vertical = 14.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = EcoGreen,
                modifier = Modifier.size(24.dp)
            )

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = EcoText
                )

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = EcoSecondaryText,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun LogoutCard(
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
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
                .padding(
                    horizontal = 16.dp,
                    vertical = 15.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Outlined.Logout,
                contentDescription = "Logout",
                tint = EcoSecondaryText,
                modifier = Modifier.size(23.dp)
            )

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Text(
                text = "Log Out",
                style = MaterialTheme.typography.titleMedium,
                color = EcoText
            )
        }
    }
}