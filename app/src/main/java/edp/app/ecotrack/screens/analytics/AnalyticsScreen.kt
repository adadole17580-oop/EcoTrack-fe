package edp.app.ecotrack.screens.analytics

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

// ============================================================
// COLORS - SAME AS DASHBOARD
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
// ANALYTICS COLORS
// ============================================================

private val CardWhite = Color.White
private val MutedGreen = Color(0xFF6D9672)
private val ProgressBackground = Color(0xFFE2E2E2)

// ============================================================
// DATA
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

private val categoryReports = listOf(
    CategoryReport(
        name = "Uncollected Garbage",
        reports = 48,
        percentage = 34
    ),
    CategoryReport(
        name = "Overflowing Garbage",
        reports = 37,
        percentage = 26
    ),
    CategoryReport(
        name = "Illegal Dumping",
        reports = 28,
        percentage = 20
    ),
    CategoryReport(
        name = "Improper Waste Disposal",
        reports = 18,
        percentage = 13
    ),
    CategoryReport(
        name = "Other Waste Concern",
        reports = 10,
        percentage = 7
    )
)

private val monthlyReports = listOf(
    MonthlyReport("Apr", 25),
    MonthlyReport("May", 34),
    MonthlyReport("Jun", 30),
    MonthlyReport("Jul", 42),
    MonthlyReport("Aug", 36),
    MonthlyReport("Sep", 48)
)

// ============================================================
// MAIN SCREEN
// ============================================================

@Composable
fun AnalyticsScreen(
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},

    onDashboardClick: () -> Unit = {},
    onWasteReportClick: () -> Unit = {},
    onCollectorsClick: () -> Unit = {},
    onResidentsClick: () -> Unit = {},
    onScheduleClick: () -> Unit = {},
    onWasteGuideClick: () -> Unit = {}
) {

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
                        modifier = Modifier.weight(1f)
                    )

                    ReportStatusSummaryCard(
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                // =================================================
                // MONTHLY TREND
                // =================================================

                MonthlyTrendCard(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// ============================================================
// TOP BAR - SAME AS DASHBOARD
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

        // =====================================================
        // MENU ICON
        // =====================================================

        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            tint = EcoDarkGreen,
            modifier = Modifier.size(23.dp)
        )

        Spacer(
            modifier = Modifier.width(15.dp)
        )

        // =====================================================
        // TITLE
        // =====================================================

        Text(
            text = "Analytics",
            color = EcoDarkGreen,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        // =====================================================
        // NOTIFICATION
        // SAME AS DASHBOARD
        // =====================================================

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

        // =====================================================
        // PROFILE
        // SAME AS DASHBOARD
        // =====================================================

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
// SIDEBAR - SAME AS DASHBOARD
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

        // =====================================================
        // ECOTRACK LOGO - SAME AS DASHBOARD
        // =====================================================

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

        // =====================================================
        // ADMINISTRATOR - SAME AS DASHBOARD
        // =====================================================

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

        // =====================================================
        // DASHBOARD
        // =====================================================

        AnalyticsSidebarItem(
            icon = Icons.Default.GridView,
            text = "Dashboard",
            selected = false,
            onClick = onDashboardClick
        )

        // =====================================================
        // WASTE REPORT
        // =====================================================

        AnalyticsSidebarItem(
            icon = Icons.Default.Description,
            text = "Waste Report",
            selected = false,
            onClick = onWasteReportClick
        )

        // =====================================================
        // COLLECTORS
        // =====================================================

        AnalyticsSidebarItem(
            icon = Icons.Default.LocalShipping,
            text = "Collectors",
            selected = false,
            onClick = onCollectorsClick
        )

        // =====================================================
        // RESIDENTS
        // =====================================================

        AnalyticsSidebarItem(
            icon = Icons.Default.Groups,
            text = "Residents",
            selected = false,
            onClick = onResidentsClick
        )

        // =====================================================
        // SCHEDULES
        // =====================================================

        AnalyticsSidebarItem(
            icon = Icons.Default.CalendarMonth,
            text = "Schedules",
            selected = false,
            onClick = onScheduleClick
        )

        // =====================================================
        // ANALYTICS - SELECTED
        // =====================================================

        AnalyticsSidebarItem(
            icon = Icons.Default.BarChart,
            text = "Analytics",
            selected = true,
            onClick = {}
        )

        // =====================================================
        // WASTE GUIDE
        // =====================================================

        AnalyticsSidebarItem(
            icon = Icons.Default.MenuBook,
            text = "Waste Guide",
            selected = false,
            onClick = onWasteGuideClick
        )
    }
}

// ============================================================
// SIDEBAR ITEM - SAME AS DASHBOARD
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
// REPORTS BY CATEGORY
// ============================================================

@Composable
private fun ReportsByCategoryCard(
    modifier: Modifier = Modifier
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
// CATEGORY ROW
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
// REPORT STATUS SUMMARY
// ============================================================

@Composable
private fun ReportStatusSummaryCard(
    modifier: Modifier = Modifier
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
                value = "441"
            )

            AnalyticsSummaryRow(
                label = "Resolved This Month",
                value = "28"
            )

            AnalyticsSummaryRow(
                label = "Average Resolution Time",
                value = "2.4 days"
            )

            AnalyticsSummaryRow(
                label = "Collector Efficiency Rate",
                value = "91%"
            )

            AnalyticsSummaryRow(
                label = "Most Active Barangay",
                value = "Area A (38%)"
            )

            AnalyticsSummaryRow(
                label = "Most Common Issue",
                value = "Uncollected Garbage"
            )
        }
    }
}

// ============================================================
// SUMMARY ROW
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
// MONTHLY TREND
// ============================================================

@Composable
private fun MonthlyTrendCard(
    modifier: Modifier = Modifier
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
                text = "Monthly Report Trend (Sep 2026)",
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

                MonthlyLineChart()
            }

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Daily report submissions — September 2026",
                color = MutedGreen,
                fontSize = 9.sp
            )
        }
    }
}

// ============================================================
// MONTHLY LINE CHART
// ============================================================

@Composable
private fun MonthlyLineChart() {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

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

            // =================================================
            // GRID LINES
            // =================================================

            val gridCount = 4

            for (i in 0..gridCount) {

                val y =
                    topPadding +
                            chartHeight *
                            (
                                    i.toFloat() /
                                            gridCount
                                    )

                drawLine(
                    color = Color(0xFFE5EDE6),

                    start = Offset(
                        leftPadding,
                        y
                    ),

                    end = Offset(
                        size.width -
                                rightPadding,
                        y
                    ),

                    strokeWidth = 1f
                )
            }

            // =================================================
            // VALUES
            // =================================================

            val values =
                monthlyReports.map {
                    it.reports
                }

            val maxValue = 50f

            val points =
                values.mapIndexed { index, value ->

                    val x =
                        leftPadding +
                                chartWidth *
                                (
                                        index.toFloat() /
                                                (values.size - 1)
                                        )

                    val y =
                        topPadding +
                                chartHeight -
                                (
                                        value /
                                                maxValue
                                        ) *
                                chartHeight

                    Offset(
                        x,
                        y
                    )
                }

            // =================================================
            // AREA
            // =================================================

            val areaPath =
                Path().apply {

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
                        topPadding +
                                chartHeight
                    )

                    lineTo(
                        points.first().x,
                        topPadding +
                                chartHeight
                    )

                    close()
                }

            drawPath(
                path = areaPath,
                color = Color(0xFFE7F6EA)
            )

            // =================================================
            // LINE
            // =================================================

            val linePath =
                Path().apply {

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

            // =================================================
            // POINTS
            // =================================================

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

        // =====================================================
        // MONTH LABELS
        // =====================================================

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