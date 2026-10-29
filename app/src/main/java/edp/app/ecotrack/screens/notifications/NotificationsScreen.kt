
package edp.app.ecotrack.screens.notifications

import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import edp.app.ecotrack.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// =========================================================
// COLORS
// =========================================================

private val EcoDarkGreen = Color(0xFF216B2A)
private val EcoGreen = Color(0xFF2E7D32)
private val EcoLightGreen = Color(0xFFE6F5EA)
private val EcoTopBar = Color(0xFFE0F2E5)
private val EcoBorder = Color(0xFF8BE5A7)
private val EcoTextGreen = Color(0xFF286B32)
private val White = Color.White

// =========================================================
// NOTIFICATION DATA
// =========================================================

private data class NotificationData(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val time: String,
    val isNew: Boolean
)

// =========================================================
// FORMAT NOTIFICATION DATE
// =========================================================

private fun formatNotificationTime(createdAt: String): String {
    if (createdAt.isBlank()) return ""

    return try {
        val inputFormats = listOf(
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault()),
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.getDefault()),
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        )

        val date = inputFormats.firstNotNullOfOrNull { format ->
            format.isLenient = false
            try {
                format.parse(createdAt)
            } catch (_: Exception) {
                null
            }
        } ?: return createdAt

        val difference = System.currentTimeMillis() - date.time

        when {
            difference < 60_000 -> "Just now"
            difference < 3_600_000 -> "${difference / 60_000}m ago"
            difference < 86_400_000 -> "${difference / 3_600_000}h ago"
            difference < 172_800_000 -> "Yesterday"
            else -> SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                .format(date)
        }
    } catch (_: Exception) {
        createdAt
    }
}

// =========================================================
// NOTIFICATIONS SCREEN
// =========================================================

@Composable
fun NotificationsScreen(
    accessToken: String,
    onDashboardClick: () -> Unit = {},
    onWasteReportClick: () -> Unit = {},
    onCollectorsClick: () -> Unit = {},
    onResidentsClick: () -> Unit = {},
    onScheduleClick: () -> Unit = {},
    onAnalyticsClick: () -> Unit = {},
    onWasteGuideClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    var notificationList by remember {
        mutableStateOf<List<NotificationData>>(emptyList())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    // Load notifications from the backend.
    LaunchedEffect(accessToken) {
        if (accessToken.isBlank()) {
            errorMessage = "Please log in again."
            isLoading = false
            return@LaunchedEffect
        }

        isLoading = true
        errorMessage = ""

        try {
            val response = withContext(Dispatchers.IO) {
                ApiClient.getNotifications(accessToken)
            }

            if (response.optString("status") == "OK") {
                val array = response.optJSONArray("notifications")
                val loadedNotifications = mutableListOf<NotificationData>()

                if (array != null) {
                    for (i in 0 until array.length()) {
                        val item = array.optJSONObject(i) ?: continue

                        val title = item.optString(
                            "title",
                            "Notification"
                        )

                        val description = item.optString(
                            "message",
                            ""
                        )

                        val type = item.optString(
                            "notification_type",
                            item.optString("type", "")
                        )

                        val icon = when {
                            type.contains("schedule", ignoreCase = true) ||
                                    title.contains("schedule", ignoreCase = true) ->
                                Icons.Default.Schedule

                            type.contains("resolved", ignoreCase = true) ||
                                    title.contains("resolved", ignoreCase = true) ->
                                Icons.Default.CheckCircle

                            type.contains("report", ignoreCase = true) ||
                                    title.contains("report", ignoreCase = true) ->
                                Icons.Default.Warning

                            else -> Icons.Default.Notifications
                        }

                        val createdAt = item.optString(
                            "created_at",
                            ""
                        )

                        loadedNotifications.add(
                            NotificationData(
                                icon = icon,
                                title = title,
                                description = description,
                                time = formatNotificationTime(createdAt),
                                isNew = !item.optBoolean("is_read", false)
                            )
                        )
                    }
                }

                notificationList = loadedNotifications
            } else {
                errorMessage = response.optString(
                    "message",
                    "Unable to load notifications."
                )
            }
        } catch (e: Exception) {
            errorMessage = e.message
                ?: "Unable to load notifications."

            Log.e(
                "ECO_TRACK",
                "Notifications loading failed",
                e
            )
        } finally {
            isLoading = false
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoLightGreen)
    ) {
        // =====================================================
        // SIDEBAR
        // =====================================================

        NotificationsSidebar(
            onDashboardClick = onDashboardClick,
            onWasteReportClick = onWasteReportClick,
            onCollectorsClick = onCollectorsClick,
            onResidentsClick = onResidentsClick,
            onScheduleClick = onScheduleClick,
            onAnalyticsClick = onAnalyticsClick,
            onWasteGuideClick = onWasteGuideClick
        )

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            // TOP BAR

            NotificationsTopBar(
                onNotificationClick = onNotificationClick,
                onProfileClick = onProfileClick
            )

            // PAGE CONTENT

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 30.dp,
                        vertical = 18.dp
                    )
            ) {
                Text(
                    text = "Notifications",
                    color = EcoTextGreen,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                // NOTIFICATION CONTAINER

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = White,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = EcoBorder,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    when {
                        isLoading -> {
                            Text(
                                text = "Loading notifications...",
                                modifier = Modifier.padding(20.dp),
                                color = EcoTextGreen,
                                fontSize = 13.sp
                            )
                        }

                        errorMessage.isNotBlank() -> {
                            Text(
                                text = errorMessage,
                                modifier = Modifier.padding(20.dp),
                                color = Color.Red,
                                fontSize = 13.sp
                            )
                        }

                        notificationList.isEmpty() -> {
                            Text(
                                text = "No notifications yet.",
                                modifier = Modifier.padding(20.dp),
                                color = EcoTextGreen,
                                fontSize = 13.sp
                            )
                        }

                        else -> {
                            notificationList.forEach { notification ->
                                NotificationItem(
                                    icon = notification.icon,
                                    title = notification.title,
                                    description = notification.description,
                                    time = notification.time,
                                    isNew = notification.isNew
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// =========================================================
// SIDEBAR
// =========================================================

@Composable
private fun NotificationsSidebar(
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
    ) {
        // LOGO

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

        // ADMINISTRATOR

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(EcoDarkGreen)
        ) {
            Text(
                text = "ADMINISTRATOR",
                color = Color(0xFFA8D6AA),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(
                    start = 18.dp,
                    top = 12.dp,
                    end = 18.dp
                )
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "System Admin",
                color = White,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 18.dp)
            )

            Text(
                text = "admin@ecotrack.gov.ph",
                color = Color(0xFFB9DDBD),
                fontSize = 10.sp,
                modifier = Modifier.padding(horizontal = 18.dp)
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.White)
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )
        }

        // MENU

        NotificationsSidebarItem(
            icon = Icons.Default.Groups,
            title = "Dashboard",
            selected = false,
            onClick = onDashboardClick
        )

        NotificationsSidebarItem(
            icon = Icons.Default.Description,
            title = "Waste Report",
            selected = false,
            onClick = onWasteReportClick
        )

        NotificationsSidebarItem(
            icon = Icons.Default.LocalShipping,
            title = "Collectors",
            selected = false,
            onClick = onCollectorsClick
        )

        NotificationsSidebarItem(
            icon = Icons.Default.People,
            title = "Residents",
            selected = false,
            onClick = onResidentsClick
        )

        NotificationsSidebarItem(
            icon = Icons.Default.CalendarMonth,
            title = "Schedules",
            selected = false,
            onClick = onScheduleClick
        )

        NotificationsSidebarItem(
            icon = Icons.Default.Analytics,
            title = "Analytics",
            selected = false,
            onClick = onAnalyticsClick
        )

        NotificationsSidebarItem(
            icon = Icons.Default.MenuBook,
            title = "Waste Guide",
            selected = false,
            onClick = onWasteGuideClick
        )
    }
}

// =========================================================
// SIDEBAR ITEM
// =========================================================

@Composable
private fun NotificationsSidebarItem(
    icon: ImageVector,
    title: String,
    selected: Boolean,
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
// =========================================================

@Composable
private fun NotificationsTopBar(
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp)
            .background(EcoTopBar)
            .padding(horizontal = 25.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(
            modifier = Modifier.width(15.dp)
        )

        // TITLE

        Text(
            text = "Notifications",
            color = EcoDarkGreen,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        // BELL BUTTON

        androidx.compose.material3.IconButton(
            onClick = onNotificationClick
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = Color.Black,
                modifier = Modifier.size(23.dp)
            )
        }

        // PROFILE BUTTON

        androidx.compose.material3.IconButton(
            onClick = onProfileClick
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile",
                tint = EcoDarkGreen,
                modifier = Modifier.size(31.dp)
            )
        }
    }
}

// =========================================================
// NOTIFICATION ITEM
// =========================================================

@Composable
private fun NotificationItem(
    icon: ImageVector,
    title: String,
    description: String,
    time: String,
    isNew: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 18.dp,
                vertical = 14.dp
            ),
        verticalAlignment = Alignment.Top
    ) {
        // DOT

        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .size(6.dp)
                .background(
                    color = if (isNew) {
                        EcoGreen
                    } else {
                        Color.LightGray
                    },
                    shape = RoundedCornerShape(50)
                )
        )

        Spacer(
            modifier = Modifier.width(10.dp)
        )

        // ICON

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = EcoGreen,
            modifier = Modifier
                .padding(top = 1.dp)
                .size(20.dp)
        )

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        // TEXT

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = EcoTextGreen,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = description,
                color = Color(0xFF4E8056),
                fontSize = 11.sp,
                lineHeight = 15.sp
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = time,
                color = Color(0xFF7D9A82),
                fontSize = 10.sp
            )
        }

        // NEW BADGE

        if (isNew) {
            Text(
                text = "New",
                color = EcoGreen,
                fontSize = 10.sp,
                modifier = Modifier
                    .background(
                        color = Color(0xFFD5F4DC),
                        shape = RoundedCornerShape(7.dp)
                    )
                    .padding(
                        horizontal = 8.dp,
                        vertical = 4.dp
                    )
            )
        }
    }
}
