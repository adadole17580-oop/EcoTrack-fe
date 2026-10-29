
package edp.app.ecotrack.screens.dashboard

import org.json.JSONObject
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.MenuBook
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edp.app.ecotrack.screens.waste_report.ViewReportScreen
import edp.app.ecotrack.screens.waste_report.WasteReport

// ---------------------------------------------------------
// ECOTRACK COLORS
// ---------------------------------------------------------

private val EcoDarkGreen = Color(0xFF216B2A)
private val EcoGreen = Color(0xFF2E7D32)
private val EcoLightGreen = Color(0xFFE6F5EA)
private val EcoBorder = Color(0xFF8BE5A7)
private val EcoTextGreen = Color(0xFF286B32)
private val White = Color.White

// ---------------------------------------------------------
// ADMIN INFORMATION
// ---------------------------------------------------------

data class AdminInfo(
    val role: String,
    val name: String,
    val email: String
)

private val currentAdmin = AdminInfo(
    role = "ADMINISTRATOR",
    name = "System Admin",
    email = "admin@ecotrack.gov.ph"
)

// ---------------------------------------------------------
// DASHBOARD
// ---------------------------------------------------------

@Composable
fun DashboardScreen(
    admin: AdminInfo = currentAdmin,
    dashboardStatistics: JSONObject? = null,
    statisticsLoading: Boolean = false,
    statisticsError: String = "",
    onViewAllClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onWasteReportClick: () -> Unit = {},
    onCollectorsClick: () -> Unit = {},
    onResidentsClick: () -> Unit = {},
    onSchedulesClick: () -> Unit = {},
    onAnalyticsClick: () -> Unit = {},
    onWasteGuideClick: () -> Unit = {},

    // Recent waste reports from the backend
    recentWasteReports: List<WasteReport> = emptyList(),
    reportsLoading: Boolean = false,
    reportsError: String = ""
) {
    // Expected API response:
    // {
    //   "status": "OK",
    //   "statistics": {
    //     "totalResidents": 0,
    //     "totalCollectors": 0,
    //     "pendingReports": 0,
    //     "inProgressReports": 0,
    //     "forVerificationReports": 0,
    //     "resolvedReports": 0
    //   }
    // }

    val stats = dashboardStatistics?.optJSONObject("statistics")

    val unavailableNumber = when {
        statisticsLoading -> "..."
        else -> "—"
    }

    // Do not display a fake zero if the API did not return a field.
    fun getStatistic(key: String): String {
        return if (stats != null && stats.has(key) &&
            !stats.isNull(key)
        ) {
            stats.optInt(key).toString()
        } else {
            unavailableNumber
        }
    }

    var selectedReport by remember {
        mutableStateOf<WasteReport?>(null)
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoLightGreen)
    ) {
        // -------------------------------------------------
        // LEFT SIDEBAR
        // -------------------------------------------------

        DashboardSidebar(
            admin = admin,
            onWasteReportClick = onWasteReportClick,
            onCollectorsClick = onCollectorsClick,
            onResidentsClick = onResidentsClick,
            onSchedulesClick = onSchedulesClick,
            onAnalyticsClick = onAnalyticsClick,
            onWasteGuideClick = onWasteGuideClick
        )

        // -------------------------------------------------
        // MAIN CONTENT
        // -------------------------------------------------

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            DashboardTopBar(
                onNotificationClick = onNotificationClick,
                onProfileClick = onProfileClick
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 30.dp,
                        vertical = 18.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // -------------------------------------------------
                // API LOADING / ERROR MESSAGE
                // -------------------------------------------------

                if (statisticsLoading || statisticsError.isNotBlank()) {
                    item {
                        Text(
                            text = when {
                                statisticsLoading ->
                                    "Loading dashboard statistics..."
                                else ->
                                    "Unable to load statistics: $statisticsError"
                            },
                            color = if (statisticsError.isNotBlank()) {
                                Color(0xFFB3261E)
                            } else {
                                EcoTextGreen
                            },
                            fontSize = 12.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    White,
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(12.dp)
                        )
                    }
                }

                // -------------------------------------------------
                // FIRST ROW OF STATISTICS
                // -------------------------------------------------

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        StatisticCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.People,
                            iconBackground = Color(0xFFD9EFFC),
                            iconColor = Color(0xFF5A9BC7),
                            number = getStatistic("totalResidents"),
                            title = "Total Residents",
                            subtitle = "Registered residents"
                        )

                        StatisticCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.LocalShipping,
                            iconBackground = Color(0xFFE0D1E5),
                            iconColor = Color(0xFF72547E),
                            number = getStatistic("totalCollectors"),
                            title = "Collectors",
                            subtitle = "Registered collectors"
                        )

                        StatisticCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.AccessTime,
                            iconBackground = Color(0xFFF3E9B9),
                            iconColor = Color(0xFFC49B22),
                            number = getStatistic("pendingReports"),
                            title = "Pending",
                            subtitle = "Needs Assignment"
                        )
                    }
                }

                // -------------------------------------------------
                // SECOND ROW OF STATISTICS
                // -------------------------------------------------

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        StatisticCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.AccessTime,
                            iconBackground = Color(0xFFBDEFE8),
                            iconColor = Color(0xFF26B8A8),
                            number = getStatistic("inProgressReports"),
                            title = "In Progress",
                            subtitle = "Currently being handled"
                        )

                        StatisticCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.Visibility,
                            iconBackground = Color(0xFFF2D3C9),
                            iconColor = Color(0xFFE27B5F),
                            number = getStatistic("forVerificationReports"),
                            title = "For Verification",
                            subtitle = "Awaiting review"
                        )

                        StatisticCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.CheckCircle,
                            iconBackground = Color(0xFFC8DAC8),
                            iconColor = EcoGreen,
                            number = getStatistic("resolvedReports"),
                            title = "Resolved",
                            subtitle = "Resolved reports"
                        )
                    }
                }

                // -------------------------------------------------
                // RECENT WASTE REPORTS
                // Existing sample layout preserved.
                // -------------------------------------------------

                item {
                    // RECENT WASTE REPORTS
                    RecentWasteReports(
                        reports = recentWasteReports.take(5),
                        loading = reportsLoading,
                        error = reportsError,
                        onViewAllClick = onViewAllClick,
                        onViewClick = { report ->
                            selectedReport = report
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }

    // ---------------------------------------------------------
    // VIEW REPORT POPUP
    // ---------------------------------------------------------

    selectedReport?.let { report ->
        ViewReportScreen(
            report = report,
            onDismiss = {
                selectedReport = null
            }
        )
    }
}

// ---------------------------------------------------------
// SIDEBAR
// ---------------------------------------------------------

@Composable
private fun DashboardSidebar(
    admin: AdminInfo,
    onWasteReportClick: () -> Unit,
    onCollectorsClick: () -> Unit,
    onResidentsClick: () -> Unit,
    onSchedulesClick: () -> Unit,
    onAnalyticsClick: () -> Unit,
    onWasteGuideClick: () -> Unit = {}
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
                text = admin.role,
                color = Color(0xFF9FD6A5),
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = admin.name,
                color = White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = admin.email,
                color = Color(0xFFB9DDBD),
                fontSize = 10.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        SidebarItem(
            icon = Icons.Default.GridView,
            title = "Dashboard",
            selected = true
        )

        SidebarItem(
            icon = Icons.Default.Description,
            title = "Waste Report",
            onClick = onWasteReportClick
        )

        SidebarItem(
            icon = Icons.Default.LocalShipping,
            title = "Collectors",
            onClick = onCollectorsClick
        )

        SidebarItem(
            icon = Icons.Default.Groups,
            title = "Residents",
            onClick = onResidentsClick
        )

        SidebarItem(
            icon = Icons.Default.CalendarMonth,
            title = "Schedules",
            onClick = onSchedulesClick
        )

        SidebarItem(
            icon = Icons.Default.BarChart,
            title = "Analytics",
            onClick = onAnalyticsClick
        )

        SidebarItem(
            icon = Icons.Default.MenuBook,
            title = "Waste Guide",
            onClick = onWasteGuideClick
        )
    }
}

// ---------------------------------------------------------
// SIDEBAR ITEM
// ---------------------------------------------------------

@Composable
private fun SidebarItem(
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

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = title,
            color = White,
            fontSize = 13.sp
        )
    }
}

// ---------------------------------------------------------
// TOP BAR
// ---------------------------------------------------------

@Composable
private fun DashboardTopBar(
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp)
            .background(Color(0xFFE0F2E5))
            .padding(horizontal = 25.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(modifier = Modifier.width(15.dp))

        Text(
            text = "Dashboard",
            color = EcoDarkGreen,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = onNotificationClick) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = Color.Black,
                modifier = Modifier.size(23.dp)
            )
        }

        IconButton(onClick = onProfileClick) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile",
                tint = EcoDarkGreen,
                modifier = Modifier.size(31.dp)
            )
        }
    }
}

// ---------------------------------------------------------
// STATISTIC CARD
// ---------------------------------------------------------

@Composable
private fun StatisticCard(
    modifier: Modifier,
    icon: ImageVector,
    iconBackground: Color,
    iconColor: Color,
    number: String,
    title: String,
    subtitle: String
) {
    Card(
        modifier = modifier
            .height(175.dp)
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(14.dp)
            )
            .border(
                width = 1.dp,
                color = EcoBorder,
                shape = RoundedCornerShape(14.dp)
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = iconBackground,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(17.dp))

            Text(
                text = number,
                color = EcoTextGreen,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = title,
                color = EcoTextGreen,
                fontSize = 12.sp
            )

            Text(
                text = subtitle,
                color = Color(0xFF4AA35A),
                fontSize = 11.sp
            )
        }
    }
}

// ---------------------------------------------------------
// RECENT WASTE REPORTS
// Existing sample data preserved.
// ---------------------------------------------------------


@Composable
private fun RecentWasteReports(
    reports: List<WasteReport>,
    loading: Boolean,
    error: String,
    onViewAllClick: () -> Unit,
    onViewClick: (WasteReport) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(14.dp)
            )
            .border(
                width = 1.dp,
                color = EcoBorder,
                shape = RoundedCornerShape(14.dp)
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 14.dp,
                        bottom = 12.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Waste Reports",
                    color = EcoTextGreen,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "View All",
                    color = EcoGreen,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .clickable { onViewAllClick() }
                        .padding(5.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF2FAF4))
                    .border(
                        width = 1.dp,
                        color = Color(0xFFD6F0DC)
                    )
                    .padding(
                        horizontal = 20.dp,
                        vertical = 9.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "REPORT ID",
                    modifier = Modifier.weight(0.8f),
                    color = EcoGreen,
                    fontSize = 11.sp
                )

                Text(
                    text = "RESIDENT",
                    modifier = Modifier.weight(1.8f),
                    color = EcoGreen,
                    fontSize = 11.sp
                )

                Text(
                    text = "CATEGORY",
                    modifier = Modifier.weight(1.7f),
                    color = EcoGreen,
                    fontSize = 11.sp
                )

                Text(
                    text = "STATUS",
                    modifier = Modifier.weight(1.4f),
                    color = EcoGreen,
                    fontSize = 11.sp
                )

                Text(
                    text = "ACTION",
                    modifier = Modifier.weight(0.8f),
                    color = EcoGreen,
                    fontSize = 11.sp
                )
            }

            when {
                loading -> {
                    Text(
                        text = "Loading recent waste reports...",
                        color = EcoTextGreen,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    )
                }

                error.isNotBlank() -> {
                    Text(
                        text = "Unable to load reports: $error",
                        color = Color(0xFFB3261E),
                        fontSize = 12.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    )
                }

                reports.isEmpty() -> {
                    Text(
                        text = "No waste reports available.",
                        color = Color.Gray,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    )
                }

                else -> {
                    reports.forEach { report ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 20.dp,
                                    vertical = 13.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = report.reportId,
                                modifier = Modifier.weight(0.8f),
                                color = EcoTextGreen,
                                fontSize = 11.sp
                            )

                            Column(
                                modifier = Modifier.weight(1.8f)
                            ) {
                                Text(
                                    text = report.resident,
                                    color = EcoTextGreen,
                                    fontSize = 11.sp
                                )

                                Text(
                                    text = report.location,
                                    color = Color(0xFF58BE78),
                                    fontSize = 10.sp,
                                    maxLines = 2
                                )
                            }

                            Text(
                                text = report.category,
                                modifier = Modifier.weight(1.7f),
                                color = EcoTextGreen,
                                fontSize = 11.sp,
                                maxLines = 2
                            )

                            Box(
                                modifier = Modifier.weight(1.4f)
                            ) {
                                val statusColor = when (
                                    report.status.trim().lowercase()
                                ) {
                                    "resolved", "completed" ->
                                        Color(0xFF286B32)

                                    "for verification",
                                    "waiting for verification" ->
                                        Color(0xFFE97838)

                                    "in progress" ->
                                        Color(0xFF287DB5)

                                    else ->
                                        Color(0xFFC49B22)
                                }

                                Text(
                                    text = report.status,
                                    color = statusColor,
                                    fontSize = 10.sp,
                                    maxLines = 2,
                                    modifier = Modifier
                                        .background(
                                            color = statusColor.copy(
                                                alpha = 0.12f
                                            ),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .padding(
                                            horizontal = 7.dp,
                                            vertical = 4.dp
                                        )
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .weight(0.8f)
                                    .clickable {
                                        onViewClick(report)
                                    },
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = "View",
                                    color = EcoGreen,
                                    fontSize = 11.sp,
                                    modifier = Modifier
                                        .border(
                                            width = 1.dp,
                                            color = EcoGreen,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .padding(
                                            horizontal = 9.dp,
                                            vertical = 4.dp
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

