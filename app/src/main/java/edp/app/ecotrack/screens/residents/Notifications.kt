package edp.app.ecotrack.screens.residents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edp.app.ecotrack.network.ApiClient
import edp.app.ecotrack.ui.theme.EcoDarkGreen
import edp.app.ecotrack.ui.theme.EcoGreen
import edp.app.ecotrack.ui.theme.EcoPendingText
import edp.app.ecotrack.ui.theme.EcoPendingYellow
import edp.app.ecotrack.ui.theme.EcoResolvedGreen
import edp.app.ecotrack.ui.theme.EcoResolvedText
import edp.app.ecotrack.ui.theme.EcoWhite
import kotlinx.coroutines.CancellationException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun NotificationScreen(
    accessToken: String,
    onBackToHome: () -> Unit = {},
    onNotificationClick: (String) -> Unit = {}
) {
    var notifications by remember { mutableStateOf<List<JSONObject>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(accessToken) {
        isLoading = true
        errorMessage = null

        try {
            val response = ApiClient.getResidentNotifications(accessToken)
            val array = response.optJSONArray("notifications")
            val result = mutableListOf<JSONObject>()

            if (array != null) {
                for (index in 0 until array.length()) {
                    val notification = array.optJSONObject(index)
                    if (notification != null) {
                        result.add(notification)
                    }
                }
            }

            notifications = result
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            errorMessage = exception.message
                ?: "Unable to load your notifications."
        } finally {
            isLoading = false
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            NotificationHeader(onBackToHome = onBackToHome)
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }

        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = EcoGreen)
                }
            }
        } else if (errorMessage != null) {
            item {
                Text(
                    text = errorMessage.orEmpty(),
                    modifier = Modifier.padding(
                        horizontal = 24.dp,
                        vertical = 16.dp
                    ),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else if (notifications.isEmpty()) {
            item {
                Text(
                    text = "You're all caught up. No notifications yet.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 24.dp,
                            vertical = 32.dp
                        ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            items(
                items = notifications,
                key = { it.optInt("notification_id") }
            ) { notification ->
                val title = notification.optString("title", "Notification")
                val message = notification.optString("message", "")
                val type = notification.optString("type", "")
                val isRead = notification.optBoolean("is_read", false)
                val createdAt = notification.optString("created_at", "")

                NotificationDataCard(
                    title = title,
                    message = message,
                    type = type,
                    time = formatNotificationTime(createdAt),
                    isNew = !isRead,
                    onClick = {
                        onNotificationClick("$type $title $message")
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                Spacer(modifier = Modifier.height(18.dp))
            }
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
            IconButton(onClick = onBackToHome) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to Home",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "Back to Home",
                color = Color.White.copy(alpha = 0.9f),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.NotificationsNone,
                contentDescription = "Notifications",
                tint = Color.White,
                modifier = Modifier.size(27.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(
                text = "Notifications",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = "Track your waste collection reminders and report updates",
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun NotificationDataCard(
    title: String,
    message: String,
    type: String,
    time: String,
    isNew: Boolean,
    onClick: () -> Unit
) {
    val badgeColor = when {
        type.contains("resolved", ignoreCase = true) -> EcoResolvedGreen
        type.contains("progress", ignoreCase = true) ||
                type.contains("pending", ignoreCase = true) -> EcoPendingYellow
        else -> EcoGreen
    }

    val badgeTextColor = when {
        type.contains("resolved", ignoreCase = true) -> EcoResolvedText
        type.contains("progress", ignoreCase = true) ||
                type.contains("pending", ignoreCase = true) -> EcoPendingText
        else -> EcoWhite
    }

    NotificationCard(
        backgroundColor = if (isNew) {
            MaterialTheme.colorScheme.surfaceVariant
        } else {
            MaterialTheme.colorScheme.surface
        },
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "●",
                color = EcoGreen,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.width(24.dp)
            )

            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (type.isNotBlank()) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(badgeColor)
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = type,
                    color = badgeTextColor,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        Text(
            text = message,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )

        NotificationBottomRow(
            time = time,
            isNew = isNew
        )
    }
}

@Composable
private fun NotificationCard(
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        content = {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                content = content
            )
        }
    )
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
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.weight(1f)
        )

        if (isNew) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(EcoGreen)
                )

                Spacer(modifier = Modifier.width(4.dp))

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

private fun formatNotificationTime(value: String): String {
    if (value.isBlank()) return "Time unavailable"

    return try {
        val parser = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX",
            Locale.US
        ).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

        val date = parser.parse(value) ?: return value

        val difference = (System.currentTimeMillis() - date.time)
            .coerceAtLeast(0L)

        val minutes = difference / 60_000L
        val hours = difference / 3_600_000L
        val days = difference / 86_400_000L

        when {
            minutes < 1 -> "Just now"
            minutes < 60 -> "$minutes min${if (minutes == 1L) "" else "s"} ago"
            hours < 24 -> "$hours hour${if (hours == 1L) "" else "s"} ago"
            days < 7 -> "$days day${if (days == 1L) "" else "s"} ago"
            else -> SimpleDateFormat(
                "MMM d, yyyy",
                Locale.getDefault()
            ).format(Date(date.time))
        }
    } catch (_: Exception) {
        value
    }
}