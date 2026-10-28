package edp.app.ecotrack.screens.residents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.Report
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edp.app.ecotrack.components.residents.QuickAction
import edp.app.ecotrack.data.model.ResidentSchedule
import edp.app.ecotrack.data.repository.ResidentScheduleRepository
import edp.app.ecotrack.network.ApiClient
import edp.app.ecotrack.ui.theme.EcoDarkGreen
import edp.app.ecotrack.ui.theme.EcoGreen
import edp.app.ecotrack.ui.theme.EcoLightGreen
import edp.app.ecotrack.ui.theme.EcoResolvedGreen
import edp.app.ecotrack.viewmodel.ResidentDashboardViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    accessToken: String = "",
    onScheduleClick: () -> Unit = {},
    onReportClick: () -> Unit = {},
    onGuideClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onViewAllReportsClick: () -> Unit = {},
    dashboardViewModel: ResidentDashboardViewModel = viewModel(),
    onNotificationsClick: () -> Unit = onNotificationClick,
    onViewSchedule: () -> Unit,
    onReportDetailsClick: (Long) -> Unit = {},
    onQuickActionClick: (String) -> Unit = { action ->
        when (action) {
            "schedule" -> onScheduleClick()
            "report" -> onReportClick()
            "guide" -> onGuideClick()
            "profile" -> onProfileClick()
        }
    }
) {
    val dashboard by dashboardViewModel.dashboard.collectAsState()
    val isLoading by dashboardViewModel.isLoading.collectAsState()
    val errorMessage by dashboardViewModel.errorMessage.collectAsState()

    var schedules by remember {
        mutableStateOf<List<ResidentSchedule>>(emptyList())
    }
    var schedulesLoading by remember { mutableStateOf(true) }
    var schedulesError by remember { mutableStateOf<String?>(null) }

    var recentReports by remember {
        mutableStateOf<List<RecentReportItem>>(emptyList())
    }
    var reportsLoading by remember { mutableStateOf(true) }
    var reportsError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(accessToken) {
        if (accessToken.isBlank()) {
            schedules = emptyList()
            schedulesError = "Please log in to view collection schedules."
            schedulesLoading = false

            recentReports = emptyList()
            reportsError = "Please log in to view your reports."
            reportsLoading = false
        } else {
            dashboardViewModel.loadDashboard(accessToken)

            schedulesLoading = true
            schedulesError = null

            try {
                schedules = ResidentScheduleRepository.getSchedules(accessToken)
            } catch (exception: Exception) {
                schedules = emptyList()
                schedulesError = exception.message
                    ?: "Unable to load collection schedules."
            } finally {
                schedulesLoading = false
            }

            reportsLoading = true
            reportsError = null

            try {
                val result = withContext(Dispatchers.IO) {
                    ApiClient.getResidentReports(accessToken)
                }

                val reportArray = result.optJSONArray("reports") ?: JSONArray()

                recentReports = (0 until reportArray.length())
                    .mapNotNull { index ->
                        val item = reportArray.optJSONObject(index)
                            ?: return@mapNotNull null

                        val reportId = item.optLong("report_id", 0L)

                        if (reportId <= 0L) {
                            return@mapNotNull null
                        }

                        val createdAt = item.optString("created_at")
                            .takeIf { it.isNotBlank() && it != "null" }
                            .orEmpty()

                        val parsedDate = try {
                            LocalDate.parse(createdAt.take(10))
                        } catch (_: Exception) {
                            null
                        }

                        val formattedDate = parsedDate?.format(
                            DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)
                        ) ?: "Date unavailable"

                        RecentReportItem(
                            title = item.optString("report_type")
                                .ifBlank { "Waste Report" },
                            location = item.optString("location_details")
                                .ifBlank { "Location not provided" },
                            status = item.optString("status")
                                .ifBlank { "Pending" },
                            createdAt = createdAt,
                            date = formattedDate,
                            reportId = reportId
                        )
                    }
                    .sortedByDescending { it.createdAt }
                    .take(3)
            } catch (exception: Exception) {
                recentReports = emptyList()
                reportsError = exception.message
                    ?: "Unable to load recent reports."
            } finally {
                reportsLoading = false
            }
        }
    }

    val residentName = dashboard?.user?.full_name
        ?.takeIf { it.isNotBlank() }
        ?: "Resident"

    val residentAddress = dashboard?.user?.address
        ?.takeIf { it.isNotBlank() }
        ?: "Address not provided"

    val residentBarangay = dashboard?.user?.barangay
        ?.takeIf { it.isNotBlank() }
        ?: "Your barangay"

    val nextCollection = remember(schedules) {
        findNextCollection(schedules, LocalDate.now())
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Welcome to EcoTrack 👋",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = residentName,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = residentBarangay,
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = residentAddress,
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

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = EcoGreen)
            }
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage.orEmpty(),
                modifier = Modifier.padding(
                    horizontal = 24.dp,
                    vertical = 8.dp
                ),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "NEXT COLLECTION",
            modifier = Modifier.padding(horizontal = 24.dp),
            color = EcoDarkGreen,
            style = MaterialTheme.typography.labelMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clickable(onClick = onViewSchedule),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(17.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(EcoLightGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Recycling,
                        contentDescription = "Collection",
                        tint = EcoGreen,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    when {
                        schedulesLoading -> {
                            Text(
                                text = "Loading next collection...",
                                color = EcoDarkGreen,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        schedulesError != null -> {
                            Text(
                                text = "Schedule unavailable",
                                color = EcoDarkGreen,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = schedulesError.orEmpty(),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        nextCollection == null -> {
                            Text(
                                text = "No upcoming collection",
                                color = EcoDarkGreen,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "No collection schedule is available.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        else -> {
                            Text(
                                text = nextCollection.date.format(
                                    DateTimeFormatter.ofPattern(
                                        "EEEE, MMM d",
                                        Locale.ENGLISH
                                    )
                                ),
                                color = EcoDarkGreen,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = nextCollection.schedule.wasteType,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall
                            )

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = formatCollectionTime(
                                    nextCollection.schedule.collectionTime
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "View schedule",
                    tint = EcoGreen,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = "QUICK ACTIONS",
            modifier = Modifier.padding(horizontal = 24.dp),
            color = EcoDarkGreen,
            style = MaterialTheme.typography.labelMedium
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickAction(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.CalendarMonth,
                label = "Schedule",
                onClick = { onQuickActionClick("schedule") }
            )

            QuickAction(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Report,
                label = "Report",
                onClick = { onQuickActionClick("report") }
            )

            QuickAction(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Recycling,
                label = "Guide",
                onClick = { onQuickActionClick("guide") }
            )

            QuickAction(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.PersonOutline,
                label = "Profile",
                onClick = { onQuickActionClick("profile") }
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "RECENT REPORTS",
                color = EcoDarkGreen,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "View all",
                color = EcoGreen,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.clickable {
                    onViewAllReportsClick()
                }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (dashboard != null) {
            Text(
                text = "Total reports: ${dashboard!!.dashboard.total_reports}  •  Pending: ${dashboard!!.dashboard.pending_reports}",
                modifier = Modifier.padding(
                    horizontal = 24.dp,
                    vertical = 4.dp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
        }

        when {
            reportsLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = EcoGreen)
                }
            }

            reportsError != null -> {
                Text(
                    text = reportsError.orEmpty(),
                    modifier = Modifier.padding(
                        horizontal = 24.dp,
                        vertical = 8.dp
                    ),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            recentReports.isEmpty() -> {
                Text(
                    text = "No recent reports to display.",
                    modifier = Modifier.padding(
                        horizontal = 24.dp,
                        vertical = 8.dp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            else -> {
                recentReports.forEach { report ->
                    val statusColor = when (report.status.lowercase()) {
                        "resolved" -> EcoResolvedGreen
                        "pending" -> Color(0xFFE89A32)
                        "in progress" -> Color(0xFF2878B5)
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }

                    ReportItem(
                        icon = if (
                            report.title.contains("overflow", ignoreCase = true)
                        ) {
                            Icons.Default.DeleteOutline
                        } else {
                            Icons.Default.Report
                        },
                        title = report.title,
                        location = "${report.location} • ${report.date}",
                        status = report.status,
                        statusColor = statusColor,
                        onClick = {
                            onReportDetailsClick(report.reportId)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(25.dp))
    }
}

private data class RecentReportItem(
    val title: String,
    val location: String,
    val status: String,
    val createdAt: String,
    val date: String,
    val reportId: Long
)

private data class UpcomingCollection(
    val schedule: ResidentSchedule,
    val date: LocalDate
)

private fun findNextCollection(
    schedules: List<ResidentSchedule>,
    today: LocalDate
): UpcomingCollection? {
    val upcomingCollections = schedules.mapNotNull { schedule ->
        val collectionDate = try {
            LocalDate.parse(schedule.collectionDay.trim())
        } catch (_: Exception) {
            try {
                val dayOfWeek = DayOfWeek.valueOf(
                    schedule.collectionDay.trim().uppercase(Locale.ENGLISH)
                )
                var daysUntil = dayOfWeek.value - today.dayOfWeek.value
                if (daysUntil < 0) daysUntil += 7
                today.plusDays(daysUntil.toLong())
            } catch (_: Exception) {
                return@mapNotNull null
            }
        }

        val collectionTime = try {
            LocalTime.parse(schedule.collectionTime)
        } catch (_: Exception) {
            LocalTime.MIDNIGHT
        }

        if (collectionDate.isBefore(today) || (collectionDate == today && collectionTime.isBefore(LocalTime.now()))) {
            return@mapNotNull null
        }

        UpcomingCollection(
            schedule = schedule,
            date = collectionDate
        )
    }

    return upcomingCollections.minWithOrNull(
        compareBy<UpcomingCollection> { it.date }
            .thenBy {
                try {
                    LocalTime.parse(it.schedule.collectionTime)
                } catch (_: Exception) {
                    LocalTime.MIDNIGHT
                }
            }
    )
}

fun formatCollectionTime(time: String): String {
    return try {
        LocalTime.parse(time).format(
            DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)
        )
    } catch (_: Exception) {
        time
    }
}

@Composable
private fun ReportItem(
    icon: ImageVector,
    title: String,
    location: String,
    status: String,
    statusColor: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 5.dp),
        shape = RoundedCornerShape(11.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(EcoLightGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = EcoGreen,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(11.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = EcoDarkGreen,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = location,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Text(
                text = status,
                color = statusColor,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}