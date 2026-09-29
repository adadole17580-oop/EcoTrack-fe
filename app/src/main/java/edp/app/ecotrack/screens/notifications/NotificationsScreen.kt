package edp.app.ecotrack.screens.notifications

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

private val notificationList = listOf(
    NotificationData(
        icon = Icons.Default.Warning,
        title = "New report requiring assignment",
        description = "Report #1024 submitted by Maria Santos — Overflowing Garbage at Rizal St., Barangay Area.",
        time = "10m",
        isNew = true
    ),
    NotificationData(
        icon = Icons.Default.Schedule,
        title = "Schedule updated",
        description = "Schedule #27 for Barangay Area has been modified — time changed to 9:30 AM.",
        time = "3h ago",
        isNew = true
    ),
    NotificationData(
        icon = Icons.Default.CheckCircle,
        title = "Report resolved",
        description = "Report #1021 has been marked resolved after admin approval of collector proof.",
        time = "Yesterday",
        isNew = false
    )
)

// =========================================================
// NOTIFICATIONS SCREEN
// =========================================================

@Composable
fun NotificationsScreen(
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

            // =================================================
            // TOP BAR
            // =================================================

            NotificationsTopBar(
                onNotificationClick = onNotificationClick,
                onProfileClick = onProfileClick
            )

            // =================================================
            // PAGE CONTENT
            // =================================================

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

                // =================================================
                // NOTIFICATION CONTAINER
                // =================================================

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

        // =================================================
        // LOGO
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
        // ADMINISTRATOR
        // =================================================

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
                modifier = Modifier.padding(
                    horizontal = 18.dp
                )
            )

            Text(
                text = "admin@ecotrack.gov.ph",
                color = Color(0xFFB9DDBD),
                fontSize = 10.sp,
                modifier = Modifier.padding(
                    horizontal = 18.dp
                )
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // 1DP LINE BELOW EMAIL
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

        // =================================================
        // MENU
        // =================================================

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

        // =================================================
        // MENU ICON
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
            text = "Notifications",
            color = EcoDarkGreen,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        // =================================================
        // BELL BUTTON
        // =================================================

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

        // =================================================
        // PROFILE BUTTON
        // =================================================

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

        // =================================================
        // DOT
        // =================================================

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

        // =================================================
        // ICON
        // =================================================

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

        // =================================================
        // TEXT
        // =================================================

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

        // =================================================
        // NEW BADGE
        // =================================================

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