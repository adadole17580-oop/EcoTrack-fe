package edp.app.ecotrack.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edp.app.ecotrack.ui.theme.EcoDarkGreen
import edp.app.ecotrack.ui.theme.EcoGray
import edp.app.ecotrack.ui.theme.EcoGreen
import edp.app.ecotrack.ui.theme.EcoPaleGreen
import edp.app.ecotrack.ui.theme.EcoPendingText
import edp.app.ecotrack.ui.theme.EcoPendingYellow
import edp.app.ecotrack.ui.theme.EcoResolvedGreen
import edp.app.ecotrack.ui.theme.EcoResolvedText
import edp.app.ecotrack.ui.theme.EcoWhite

@Composable
fun NotificationScreen(
    onBackToHome: () -> Unit = {}
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        item {
            NotificationHeader(
                onBackToHome = onBackToHome
            )
        }

        item {
            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }

        item {
            CollectionNotification()
        }

        item {
            Spacer(
                modifier = Modifier.height(12.dp)
            )
        }

        item {
            ResolvedNotification()
        }

        item {
            Spacer(
                modifier = Modifier.height(12.dp)
            )
        }

        item {
            InProgressNotification()
        }

        item {
            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }

        item {
            PreviousNotificationsButton()
        }

        item {
            Spacer(
                modifier = Modifier.height(30.dp)
            )
        }
    }
}

@Composable
private fun NotificationHeader(
    onBackToHome: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(EcoDarkGreen)
            .padding(
                start = 12.dp,
                end = 24.dp,
                top = 28.dp,
                bottom = 24.dp
            )
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBackToHome
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to Home",
                    tint = Color.White
                )
            }

            Spacer(
                modifier = Modifier.width(4.dp)
            )

            Text(
                text = "Back to Home",
                color = Color.White.copy(alpha = 0.9f),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        onBackToHome()
                    }
            )

            Box {
                IconButton(
                    onClick = { }
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

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Column(
            modifier = Modifier.padding(start = 12.dp)
        ) {

            Text(
                text = "Notifications",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = "Track your waste collection reminders and report updates",
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun CollectionNotification() {

    NotificationCard(
        backgroundColor = EcoPaleGreen
    ) {

        NotificationTitleRow(
            icon = "♻",
            title = "Upcoming Collection Reminder",
            badge = "Collection",
            badgeColor = EcoGreen,
            badgeTextColor = EcoWhite
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "Tomorrow is Biodegradable waste collection for Zone 10.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            text = "Please put your bins out between 6:00 AM - 9:00 AM.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )

        NotificationBottomRow(
            time = "10 mins ago",
            isNew = true
        )
    }
}

@Composable
private fun ResolvedNotification() {

    NotificationCard(
        backgroundColor = EcoWhite
    ) {

        NotificationTitleRow(
            icon = "✓",
            title = "Report Resolved",
            badge = "Resolved",
            badgeColor = EcoResolvedGreen,
            badgeTextColor = EcoResolvedText
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "Your report 'Overflowing Trash Bin' at Zone 7, Bulua has been successfully resolved by the barangay team.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )

        NotificationBottomRow(
            time = "2 hours ago",
            isNew = true
        )
    }
}

@Composable
private fun InProgressNotification() {

    NotificationCard(
        backgroundColor = EcoWhite
    ) {

        NotificationTitleRow(
            icon = "◌",
            title = "Report In-progress",
            badge = "In progress",
            badgeColor = EcoPendingYellow,
            badgeTextColor = EcoPendingText
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "The report 'Missed Collection' at Zone 8 has been assigned to Driver Unit 3.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )

        NotificationBottomRow(
            time = "2 days ago",
            isNew = false
        )
    }
}

@Composable
private fun NotificationCard(
    backgroundColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(14.dp),
            content = content
        )
    }
}

@Composable
private fun NotificationTitleRow(
    icon: String,
    title: String,
    badge: String,
    badgeColor: Color,
    badgeTextColor: Color
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = icon,
            color = EcoGreen,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.width(24.dp)
        )

        Text(
            text = title,
            color = EcoDarkGreen,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(badgeColor)
                .padding(
                    horizontal = 8.dp,
                    vertical = 3.dp
                )
        ) {

            Text(
                text = badge,
                color = badgeTextColor,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun NotificationBottomRow(
    time: String,
    isNew: Boolean
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = time,
            color = EcoGray,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.weight(1f)
        )

        if (isNew) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(EcoGreen)
                )

                Spacer(
                    modifier = Modifier.width(4.dp)
                )

                Text(
                    text = "New",
                    color = EcoGreen,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun PreviousNotificationsButton() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(42.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(EcoGreen.copy(alpha = 0.15f))
            .clickable {},
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "See previous notifications",
            color = EcoDarkGreen,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}
