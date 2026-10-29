
package edp.app.ecotrack.screens.analytics

import android.util.Log
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edp.app.ecotrack.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

// ============================================================
// COLORS - SAME AS ORIGINAL
// ============================================================

private val EcoDarkGreen = Color(0xFF216B2A)
private val EcoGreen = Color(0xFF2E7D32)
private val EcoLightGreen = Color(0xFFE6F5EA)
private val EcoTopBar = Color(0xFFE0F2E5)
private val EcoBorder = Color(0xFF8BE5A7)
private val EcoTextGreen = Color(0xFF286B32)
private val White = Color.White

private val SelectedGreen = Color(0xFF719873)

// ============================================================
// ANALYTICS COLORS - SAME AS ORIGINAL
// ============================================================

private val CardWhite = Color.White
private val MutedGreen = Color(0xFF6D9672)
private val ProgressBackground = Color(0xFFE2E2E2)

// ============================================================
// DATA MODELS
// ============================================================

private data class CategoryReport(
    val name: String,
    val reports: Int,
    val percentage: Int
)

private data class MonthlyReport(
    val month: String,
    val reports: Int
)

// ============================================================
// MAIN SCREEN
// ============================================================

@Composable
fun AnalyticsScreen(
    accessToken: String,
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onDashboardClick: () -> Unit = {},
    onWasteReportClick: () -> Unit = {},
    onCollectorsClick: () -> Unit = {},
    onResidentsClick: () -> Unit = {},
    onScheduleClick: () -> Unit = {},
    onWasteGuideClick: () -> Unit = {}
) {

    // ========================================================
    // LOAD ACTUAL REPORTS FROM BACKEND
    // ========================================================

    val loadedReports = remember {
        mutableStateOf<List<JSONObject>>(emptyList())
    }

    LaunchedEffect(accessToken) {
        if (accessToken.isBlank()) {
            return@LaunchedEffect
        }

        try {
            val response = withContext(Dispatchers.IO) {
                ApiClient.getWasteReports(accessToken)
            }

            if (response.optString("status") == "OK") {
                val reportArray = response.optJSONArray("reports")
                val result = mutableListOf<JSONObject>()

                if (reportArray != null) {
                    for (i in 0 until reportArray.length()) {
                        val report = reportArray.optJSONObject(i)

                        if (report != null) {
                            result.add(report)
                        }
                    }
                }

                loadedReports.value = result
            } else {
                Log.e(
                    "AnalyticsScreen",
                    "Backend returned an unsuccessful response"
                )
            }
        } catch (e: Exception) {
            Log.e(
                "AnalyticsScreen",
                "Failed to load analytics reports",
                e
            )
        }
    }

    val reports = loadedReports.value
    val currentMonth = YearMonth.now()

    // ========================================================
    // ACTUAL REPORTS BY CATEGORY
    // ========================================================

    val categoryReports = remember(reports) {
        val total = reports.size

        reports
            .groupBy {
                it.optString("report_type")
                    .trim()
                    .ifBlank { "Other Waste Concern" }
            }
            .map { (name, items) ->
                CategoryReport(
                    name = name,
                    reports = items.size,
                    percentage = if (total > 0) {
                        items.size * 100 / total
                    } else {
                        0
                    }
                )
            }
            .sortedByDescending { it.reports }
            .take(5)
    }

    // ========================================================
    // ACTUAL REPORT COUNTS FOR THE LAST SIX MONTHS
    // ========================================================

    val monthlyReports = remember(reports) {
        (5 downTo 0).map { offset ->
            val month = currentMonth.minusMonths(offset.toLong())

            MonthlyReport(
                month = month.format(
                    DateTimeFormatter.ofPattern(
                        "MMM",
                        Locale.ENGLISH
                    )
                ),
                reports = reports.count {
                    it.optString("created_at")
                        .take(7) == month.toString()
                }
            )
        }
    }

    // ========================================================
    // ACTUAL SUMMARY VALUES
    // ========================================================

    val totalReports = reports.size

    // Counts reports created this month that currently have
    // a resolved status. The API does not provide resolved_at.
    val resolvedThisMonth = reports.count {
        it.optString("status")
            .equals("resolved", ignoreCase = true) &&
                it.optString("created_at")
                    .take(7) == currentMonth.toString()
    }

    val mostCommonIssue =
        categoryReports.firstOrNull()?.name ?: "—"

    // ========================================================
    // ORIGINAL SCREEN LAYOUT - UNCHANGED
    // ========================================================

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoLightGreen)
    ) {

        // ====================================================
        // SIDEBAR
        // ====================================================

        AnalyticsSidebar(
            onDashboardClick = onDashboardClick,
            onWasteReportClick = onWasteReportClick,
            onCollectorsClick = onCollectorsClick,
            onResidentsClick = onResidentsClick,
            onScheduleClick = onScheduleClick,
            onWasteGuideClick = onWasteGuideClick
        )

        // ====================================================
        // MAIN AREA
        // ====================================================

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {

            AnalyticsTopBar(
                onNotificationClick = onNotificationClick,
                onProfileClick = onProfileClick
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 30.dp,
                        vertical = 18.dp
                    )
            ) {

                // =================================================
                // TITLE
                // =================================================

                Text(
                    text = "Analytics & Statistics",
                    color = EcoTextGreen,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                // =================================================
                // TOP CARDS
                // =================================================

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    ReportsByCategoryCard(
                        modifier = Modifier.weight(1f),
                        categoryReports = categoryReports
                    )

                    ReportStatusSummaryCard(
                        modifier = Modifier.weight(1f),
                        totalReports = totalReports,
                        resolvedThisMonth = resolvedThisMonth,
                        mostCommonIssue = mostCommonIssue
                    )
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                // =================================================
                // MONTHLY TREND
                // =================================================

                MonthlyTrendCard(
                    modifier = Modifier.fillMaxWidth(),
                    monthlyReports = monthlyReports,
                    currentMonth = currentMonth
                )
            }
        }
    }
}

// ============================================================
// TOP BAR - SAME AS ORIGINAL
// ============================================================

@Composable
private fun AnalyticsTopBar(
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

        Text(
            text = "Analytics",
            color = EcoDarkGreen,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = onNotificationClick
        ) {

            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = Color.Black,
                modifier = Modifier.size(23.dp)
            )
        }

        IconButton(
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

// ============================================================
// SIDEBAR - SAME AS ORIGINAL
// ============================================================

@Composable
private fun AnalyticsSidebar(
    onDashboardClick: () -> Unit,
    onWasteReportClick: () -> Unit,
    onCollectorsClick: () -> Unit,
    onResidentsClick: () -> Unit,
    onScheduleClick: () -> Unit,
    onWasteGuideClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .width(250.dp)
            .fillMaxHeight()
            .background(EcoDarkGreen)
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
                text = "ADMINISTRATOR",
                color = Color(0xFF9FD6A5),
                fontSize = 11.sp
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "System Admin",
                color = White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "admin@ecotrack.gov.ph",
                color = Color(0xFFB9DDBD),
                fontSize = 10.sp
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        AnalyticsSidebarItem(
            icon = Icons.Default.GridView,
            text = "Dashboard",
            selected = false,
            onClick = onDashboardClick
        )

        AnalyticsSidebarItem(
            icon = Icons.Default.Description,
            text = "Waste Report",
            selected = false,
            onClick = onWasteReportClick
        )

        AnalyticsSidebarItem(
            icon = Icons.Default.LocalShipping,
            text = "Collectors",
            selected = false,
            onClick = onCollectorsClick
        )

        AnalyticsSidebarItem(
            icon = Icons.Default.Groups,
            text = "Residents",
            selected = false,
            onClick = onResidentsClick
        )

        AnalyticsSidebarItem(
            icon = Icons.Default.CalendarMonth,
            text = "Schedules",
            selected = false,
            onClick = onScheduleClick
        )

        AnalyticsSidebarItem(
            icon = Icons.Default.BarChart,
            text = "Analytics",
            selected = true,
            onClick = {}
        )

        AnalyticsSidebarItem(
            icon = Icons.Default.MenuBook,
            text = "Waste Guide",
            selected = false,
            onClick = onWasteGuideClick
        )
    }
}

// ============================================================
// SIDEBAR ITEM - SAME AS ORIGINAL
// ============================================================

@Composable
private fun AnalyticsSidebarItem(
    icon: ImageVector,
    text: String,
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
            .clip(
                RoundedCornerShape(22.dp)
            )
            .background(
                if (selected) {
                    SelectedGreen
                } else {
                    Color.Transparent
                }
            )
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 18.dp
            ),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = White,
            modifier = Modifier.size(19.dp)
        )

        Spacer(
            modifier = Modifier.width(14.dp)
        )

        Text(
            text = text,
            color = White,
            fontSize = 13.sp
        )
    }
}

// ============================================================
// REPORTS BY CATEGORY - SAME DESIGN, REAL DATA
// ============================================================

@Composable
private fun ReportsByCategoryCard(
    modifier: Modifier = Modifier,
    categoryReports: List<CategoryReport>
) {

    Surface(
        modifier = modifier.height(180.dp),
        shape = RoundedCornerShape(7.dp),
        color = CardWhite,
        shadowElevation = 4.dp
    ) {

        Column(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 12.dp
            )
        ) {

            Text(
                text = "Reports by Category",
                color = Color(0xFF159348),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            categoryReports.forEach { item ->

                CategoryProgressRow(
                    category = item
                )

                Spacer(
                    modifier = Modifier.height(7.dp)
                )
            }
        }
    }
}

// ============================================================
// CATEGORY ROW - SAME DESIGN
// ============================================================

@Composable
private fun CategoryProgressRow(
    category: CategoryReport
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = category.name,
                color = EcoTextGreen,
                fontSize = 10.sp,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "${category.reports} reports (${category.percentage}%)",
                color = MutedGreen,
                fontSize = 9.sp
            )
        }

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(
                    RoundedCornerShape(5.dp)
                )
                .background(ProgressBackground)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth(
                        category.percentage / 100f
                    )
                    .height(6.dp)
                    .clip(
                        RoundedCornerShape(5.dp)
                    )
                    .background(Color(0xFF2EA55F))
            )
        }
    }
}

// ============================================================
// REPORT STATUS SUMMARY - SAME DESIGN, REAL DATA
// ============================================================

@Composable
private fun ReportStatusSummaryCard(
    modifier: Modifier = Modifier,
    totalReports: Int,
    resolvedThisMonth: Int,
    mostCommonIssue: String
) {

    Surface(
        modifier = modifier.height(180.dp),
        shape = RoundedCornerShape(7.dp),
        color = CardWhite,
        shadowElevation = 4.dp
    ) {

        Column(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 12.dp
            )
        ) {

            Text(
                text = "Report Status Summary",
                color = Color(0xFF159348),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            AnalyticsSummaryRow(
                label = "Total Reports Submitted",
                value = totalReports.toString()
            )

            AnalyticsSummaryRow(
                label = "Resolved This Month",
                value = resolvedThisMonth.toString()
            )

            AnalyticsSummaryRow(
                label = "Average Resolution Time",
                value = "—"
            )

            AnalyticsSummaryRow(
                label = "Collector Efficiency Rate",
                value = "—"
            )

            AnalyticsSummaryRow(
                label = "Most Active Barangay",
                value = "—"
            )

            AnalyticsSummaryRow(
                label = "Most Common Issue",
                value = mostCommonIssue
            )
        }
    }
}

// ============================================================
// SUMMARY ROW - SAME AS ORIGINAL
// ============================================================

@Composable
private fun AnalyticsSummaryRow(
    label: String,
    value: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 4.dp
            ),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = label,
            color = EcoTextGreen,
            fontSize = 9.sp,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            color = EcoTextGreen,
            fontSize = 9.sp
        )
    }
}

// ============================================================
// MONTHLY TREND - SAME DESIGN, REAL DATA
// ============================================================

@Composable
private fun MonthlyTrendCard(
    modifier: Modifier = Modifier,
    monthlyReports: List<MonthlyReport>,
    currentMonth: YearMonth
) {

    Surface(
        modifier = modifier.height(230.dp),
        shape = RoundedCornerShape(7.dp),
        color = CardWhite,
        shadowElevation = 4.dp
    ) {

        Column(
            modifier = Modifier.padding(
                start = 18.dp,
                end = 18.dp,
                top = 16.dp,
                bottom = 12.dp
            )
        ) {

            Text(
                text = "Monthly Report Trend (${currentMonth.format(DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH))})",
                color = Color(0xFF159348),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {

                MonthlyLineChart(
                    monthlyReports = monthlyReports
                )
            }

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Monthly report submissions — last six months",
                color = MutedGreen,
                fontSize = 9.sp
            )
        }
    }
}

// ============================================================
// MONTHLY LINE CHART - SAME STYLE, REAL DATA
// ============================================================

@Composable
private fun MonthlyLineChart(
    monthlyReports: List<MonthlyReport>
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            if (monthlyReports.isEmpty()) {
                return@Canvas
            }

            val leftPadding = 35f
            val rightPadding = 12f
            val topPadding = 10f
            val bottomPadding = 14f

            val chartWidth =
                size.width -
                        leftPadding -
                        rightPadding

            val chartHeight =
                size.height -
                        topPadding -
                        bottomPadding

            // GRID LINES

            val gridCount = 4

            for (i in 0..gridCount) {

                val y =
                    topPadding +
                            chartHeight *
                            (i.toFloat() / gridCount)

                drawLine(
                    color = Color(0xFFE5EDE6),

                    start = Offset(
                        leftPadding,
                        y
                    ),

                    end = Offset(
                        size.width - rightPadding,
                        y
                    ),

                    strokeWidth = 1f
                )
            }

            // ACTUAL MONTHLY VALUES

            val values = monthlyReports.map {
                it.reports
            }

            val maxValue = (values.maxOrNull() ?: 1)
                .coerceAtLeast(1)
                .toFloat()

            val points = values.mapIndexed { index, value ->

                val x =
                    if (values.size == 1) {
                        leftPadding + chartWidth / 2f
                    } else {
                        leftPadding +
                                chartWidth *
                                (index.toFloat() / (values.size - 1))
                    }

                val y =
                    topPadding +
                            chartHeight -
                            (value / maxValue) * chartHeight

                Offset(
                    x,
                    y
                )
            }

            // AREA

            val areaPath = Path().apply {

                moveTo(
                    points.first().x,
                    points.first().y
                )

                points.drop(1).forEach {

                    lineTo(
                        it.x,
                        it.y
                    )
                }

                lineTo(
                    points.last().x,
                    topPadding + chartHeight
                )

                lineTo(
                    points.first().x,
                    topPadding + chartHeight
                )

                close()
            }

            drawPath(
                path = areaPath,
                color = Color(0xFFE7F6EA)
            )

            // LINE

            val linePath = Path().apply {

                moveTo(
                    points.first().x,
                    points.first().y
                )

                points.drop(1).forEach {

                    lineTo(
                        it.x,
                        it.y
                    )
                }
            }

            drawPath(
                path = linePath,
                color = Color(0xFF2EA55F),
                style = Stroke(
                    width = 2f,
                    cap = StrokeCap.Round
                )
            )

            // POINTS

            points.forEach { point ->

                drawCircle(
                    color = Color(0xFF2EA55F),
                    radius = 3f,
                    center = point
                )

                drawCircle(
                    color = Color.White,
                    radius = 1.3f,
                    center = point
                )
            }
        }

        // MONTH LABELS

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 35.dp,
                    end = 12.dp
                ),

            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            monthlyReports.forEach { item ->

                Text(
                    text = item.month,
                    color = MutedGreen,
                    fontSize = 9.sp
                )
            }
        }
    }
}
