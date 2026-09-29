package edp.app.ecotrack.screens.waste_report

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.draw.shadow
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
private val EcoBorder = Color(0xFF8BE5A7)
private val EcoTextGreen = Color(0xFF286B32)
private val White = Color.White
private val SearchBackground = Color(0xFFE9F5EC)

// =========================================================
// WASTE REPORT DATA
// =========================================================

data class WasteReport(
    val reportId: String,
    val resident: String,
    val category: String,
    val location: String,
    val date: String,
    val assignedTo: String,
    val status: String
)

// =========================================================
// SAMPLE FRONTEND DATA
// =========================================================

private val sampleWasteReports = listOf(

    WasteReport(
        reportId = "#1024",
        resident = "Angel Dadole",
        category = "Overflowing Garbage",
        location = "Campu, CDOC",
        date = "December 28, 2005",
        assignedTo = "Carlos Mendoza",
        status = "For Verification"
    ),

    WasteReport(
        reportId = "#1023",
        resident = "Azzie Umbay",
        category = "Illegal Dumping",
        location = "Bulua, CDOC",
        date = "June 29, 2004",
        assignedTo = "Ronald Mendoza",
        status = "In Progress"
    ),

    WasteReport(
        reportId = "#1022",
        resident = "Michelle Sy",
        category = "Improper Disposal",
        location = "Kauswagan, CDOC",
        date = "September 22, 2003",
        assignedTo = "—",
        status = "Pending"
    ),

    WasteReport(
        reportId = "#1021",
        resident = "Chloe Monteza",
        category = "Other Waste Concern",
        location = "Bulua, CDOC",
        date = "August 16, 2006",
        assignedTo = "Lito Bautista",
        status = "Resolved"
    )
)

// =========================================================
// MAIN WASTE REPORT SCREEN
// =========================================================

@Composable
fun WasteReportScreen(
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onReviewClick: (WasteReport) -> Unit = {},
    onAssignClick: (WasteReport) -> Unit = {},
    onViewClick: (WasteReport) -> Unit = {},
    onWasteGuideClick: () -> Unit = {},
    onDashboardClick: () -> Unit = {},
    onCollectorsClick: () -> Unit = {},
    onResidentsClick: () -> Unit = {},
    onScheduleClick: () -> Unit = {},
    onAnalyticsClick: () -> Unit = {}
) {

    var selectedFilter by remember {
        mutableStateOf("All")
    }

    var searchText by remember {
        mutableStateOf("")
    }

    var selectedReviewReport by remember {
        mutableStateOf<WasteReport?>(null)
    }

    var selectedAssignReport by remember {
        mutableStateOf<WasteReport?>(null)
    }

    var selectedViewReport by remember {
        mutableStateOf<WasteReport?>(null)
    }

    // =====================================================
    // FILTER REPORTS
    // =====================================================

    val filteredReports = sampleWasteReports.filter { report ->

        val matchesFilter =
            selectedFilter == "All" ||
                    report.status == selectedFilter

        val matchesSearch =
            searchText.isBlank() ||
                    report.reportId.contains(
                        searchText,
                        ignoreCase = true
                    ) ||
                    report.resident.contains(
                        searchText,
                        ignoreCase = true
                    ) ||
                    report.category.contains(
                        searchText,
                        ignoreCase = true
                    ) ||
                    report.location.contains(
                        searchText,
                        ignoreCase = true
                    )

        matchesFilter && matchesSearch
    }

    // =====================================================
    // SAME OUTER FRAME AS DASHBOARD
    // =====================================================

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoLightGreen)
    ) {

        // =====================================================
        // SIDEBAR
        // =====================================================

        WasteReportSidebar(
            onDashboardClick = onDashboardClick,
            onCollectorsClick = onCollectorsClick,
            onResidentsClick = onResidentsClick,
            onScheduleClick = onScheduleClick,
            onAnalyticsClick = onAnalyticsClick,
            onWasteGuideClick = onWasteGuideClick
        )

        // =====================================================
        // MAIN AREA
        // SAME STRUCTURE AS DASHBOARD
        // =====================================================

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {

            // =================================================
            // TOP BAR
            // SAME SIZE AS DASHBOARD
            // =================================================

            WasteReportTopBar(
                onNotificationClick = onNotificationClick,
                onProfileClick = onProfileClick
            )

            // =================================================
            // PAGE CONTENT
            // SAME CONTENT PADDING AS DASHBOARD
            // =================================================

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 30.dp,
                        vertical = 18.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // =================================================
                // PAGE TITLE
                // =================================================

                item {

                    Text(
                        text = "All Waste Reports",
                        color = EcoTextGreen,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // =================================================
                // FILTERS + SEARCH
                // =================================================

                item {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {

                            WasteFilterButton(
                                text = "All",
                                selected = selectedFilter == "All"
                            ) {
                                selectedFilter = "All"
                            }

                            WasteFilterButton(
                                text = "Pending",
                                selected = selectedFilter == "Pending"
                            ) {
                                selectedFilter = "Pending"
                            }

                            WasteFilterButton(
                                text = "In Progress",
                                selected = selectedFilter == "In Progress"
                            ) {
                                selectedFilter = "In Progress"
                            }

                            WasteFilterButton(
                                text = "For Verification",
                                selected = selectedFilter == "For Verification"
                            ) {
                                selectedFilter = "For Verification"
                            }

                            WasteFilterButton(
                                text = "Resolved",
                                selected = selectedFilter == "Resolved"
                            ) {
                                selectedFilter = "Resolved"
                            }
                        }

                        Spacer(
                            modifier = Modifier.weight(1f)
                        )

                        SearchBar(
                            searchText = searchText,
                            onSearchTextChange = {
                                searchText = it
                            }
                        )
                    }
                }

                // =================================================
                // REPORT TABLE
                // =================================================

                item {

                    WasteReportTable(
                        reports = filteredReports,
                        onReviewClick = { report ->
                            selectedReviewReport = report
                        },
                        onAssignClick = { report ->
                            selectedAssignReport = report
                        },
                        onViewClick = { report ->
                            selectedViewReport = report
                        }
                    )
                }

                item {

                    Spacer(
                        modifier = Modifier.height(30.dp)
                    )
                }
            }
        }
    }
    // =================================================
    // REVIEW POPUP
    // =================================================

    selectedReviewReport?.let { report ->

        ReviewReportScreen(
            report = report,

            onDismiss = {
                selectedReviewReport = null
            },

            onRejectProof = {
                selectedReviewReport = null
            },

            onApproveReport = {
                selectedReviewReport = null
            }
        )
    }

    // =================================================
    // ASSIGN POPUP
    // =================================================

    selectedAssignReport?.let { report ->

        AssignReportScreen(
            report = report,

            onDismiss = {
                selectedAssignReport = null
            },

            onAssignCollector = { collector ->
                selectedAssignReport = null
            }
        )
    }

    // =================================================
    // VIEW POPUP
    // =================================================

    selectedViewReport?.let { report ->

        ViewReportScreen(
            report = report,

            onDismiss = {
                selectedViewReport = null
            }
        )
    }
}
// =========================================================
// SIDEBAR
// MATCHED TO DASHBOARD SIZE
// =========================================================

@Composable
private fun WasteReportSidebar(
    onDashboardClick: () -> Unit,
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
            .padding(bottom = 20.dp)
    ) {

        // =================================================
        // ECOTRACK LOGO
        // SAME AS DASHBOARD
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
        // ADMIN INFORMATION
        // SAME AS DASHBOARD
        // =================================================

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

        // =================================================
        // DASHBOARD
        // =================================================

        WasteSidebarItem(
            icon = Icons.Default.GridView,
            title = "Dashboard",
            selected = false,
            onClick = onDashboardClick
        )

        // =================================================
        // WASTE REPORT
        // =================================================

        WasteSidebarItem(
            icon = Icons.Default.Description,
            title = "Waste Report",
            selected = true
        )

        // =================================================
        // COLLECTORS
        // =================================================

        WasteSidebarItem(
            icon = Icons.Default.LocalShipping,
            title = "Collectors",
            selected = false,
            onClick = onCollectorsClick
        )

        // =================================================
        // RESIDENTS
        // =================================================

        WasteSidebarItem(
            icon = Icons.Default.Groups,
            title = "Residents",
            selected = false,
            onClick = onResidentsClick
        )

        // =================================================
        // SCHEDULES
        // =================================================

        WasteSidebarItem(
            icon = Icons.Default.CalendarMonth,
            title = "Schedules",
            selected = false,
            onClick = onScheduleClick
        )

        // =================================================
        // ANALYTICS
        // =================================================

        WasteSidebarItem(
            icon = Icons.Default.BarChart,
            title = "Analytics",
            selected = false,
            onClick = onAnalyticsClick
        )

        // =================================================
        // WASTE GUIDE
        // =================================================

        WasteSidebarItem(
            icon = Icons.Default.MenuBook,
            title = "Waste Guide",
            selected = false,
            onClick = onWasteGuideClick
        )
    }
}

// =========================================================
// SIDEBAR ITEM
// SAME DIMENSIONS AS DASHBOARD
// =========================================================

@Composable
private fun WasteSidebarItem(
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
// SAME DIMENSIONS AS DASHBOARD
// =========================================================

@Composable
private fun WasteReportTopBar(
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp)
            .background(
                Color(0xFFE0F2E5)
            )
            .padding(horizontal = 25.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            tint = EcoDarkGreen,
            modifier = Modifier.size(23.dp)
        )

        Spacer(
            modifier = Modifier.width(15.dp)
        )

        Text(
            text = "Waste Reports",
            color = EcoDarkGreen,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        // =================================================
        // NOTIFICATION
        // =================================================

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

        // =================================================
        // PROFILE
        // =================================================

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

// =========================================================
// FILTER BUTTON
// =========================================================

@Composable
private fun WasteFilterButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .height(24.dp)
            .background(
                color = if (selected) {
                    EcoGreen
                } else {
                    White
                },
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = if (selected) {
                    EcoGreen
                } else {
                    Color(0xFFE0E0E0)
                },
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 12.dp
            ),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            color = if (selected) {
                White
            } else {
                EcoGreen
            },
            fontSize = 12.sp
        )
    }
}

// =========================================================
// SEARCH BAR
// =========================================================

@Composable
private fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit
) {

    Row(
        modifier = Modifier
            .width(180.dp)
            .height(28.dp)
            .background(
                color = SearchBackground,
                shape = RoundedCornerShape(15.dp)
            )
            .border(
                width = 1.dp,
                color = EcoBorder,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(horizontal = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = EcoGreen,
            modifier = Modifier.size(14.dp)
        )

        Spacer(
            modifier = Modifier.width(5.dp)
        )

        androidx.compose.foundation.text.BasicTextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            singleLine = true,
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 12.sp,
                color = EcoTextGreen
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// =========================================================
// TABLE
// =========================================================

@Composable
private fun WasteReportTable(
    reports: List<WasteReport>,
    onReviewClick: (WasteReport) -> Unit,
    onAssignClick: (WasteReport) -> Unit,
    onViewClick: (WasteReport) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(13.dp)
            )
            .background(
                color = White,
                shape = RoundedCornerShape(13.dp)
            )
    ) {
        // =================================================
        // TABLE HEADER
        // =================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(0xFFF2FAF4)
                )
                .padding(
                    horizontal = 12.dp,
                    vertical = 10.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            TableHeader(
                text = "REPORT\nID",
                modifier = Modifier.weight(0.8f)
            )

            TableHeader(
                text = "RESIDENT",
                modifier = Modifier.weight(1.15f)
            )

            TableHeader(
                text = "CATEGORY",
                modifier = Modifier.weight(1.55f)
            )

            TableHeader(
                text = "LOCATION",
                modifier = Modifier.weight(1.2f)
            )

            TableHeader(
                text = "DATE",
                modifier = Modifier.weight(1.25f)
            )

            TableHeader(
                text = "ASSIGNED TO",
                modifier = Modifier.weight(1.3f)
            )

            TableHeader(
                text = "STATUS",
                modifier = Modifier.weight(1f)
            )

            TableHeader(
                text = "ACTION",
                modifier = Modifier.weight(0.9f)
            )
        }

        // =================================================
        // REPORT ROWS
        // =================================================

        reports.forEach { report ->

            WasteReportRow(
                report = report,
                onReviewClick = {
                    onReviewClick(report)
                },
                onAssignClick = {
                    onAssignClick(report)
                },
                onViewClick = {
                    onViewClick(report)
                }
            )
        }

        // =================================================
        // NO RESULTS
        // =================================================

        if (reports.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "No waste reports found.",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

// =========================================================
// TABLE HEADER
// =========================================================

@Composable
private fun TableHeader(
    text: String,
    modifier: Modifier
) {

    Text(
        text = text,
        modifier = modifier,
        color = EcoGreen,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    )
}

// =========================================================
// TABLE ROW
// =========================================================

@Composable
private fun WasteReportRow(
    report: WasteReport,
    onReviewClick: () -> Unit,
    onAssignClick: () -> Unit,
    onViewClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 12.dp,
                vertical = 10.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // REPORT ID

        TableCell(
            text = report.reportId,
            modifier = Modifier.weight(0.8f)
        )

        // RESIDENT

        TableCell(
            text = report.resident,
            modifier = Modifier.weight(1.15f)
        )

        // CATEGORY

        TableCell(
            text = report.category,
            modifier = Modifier.weight(1.55f)
        )

        // LOCATION

        TableCell(
            text = report.location,
            modifier = Modifier.weight(1.2f)
        )

        // DATE

        TableCell(
            text = report.date,
            modifier = Modifier.weight(1.25f)
        )

        // ASSIGNED TO

        TableCell(
            text = report.assignedTo,
            modifier = Modifier.weight(1.3f)
        )

        // STATUS

        Box(
            modifier = Modifier.weight(1f)
        ) {

            StatusBadge(
                status = report.status
            )
        }

        // ACTION

        Box(
            modifier = Modifier.weight(0.9f)
        ) {

            when (report.status) {

                "For Verification" -> {

                    ActionButton(
                        text = "Review",
                        backgroundColor = Color(0xFFFF5B0A),
                        textColor = White,
                        onClick = onReviewClick
                    )
                }

                "In Progress" -> {

                    ActionButton(
                        text = "Assign",
                        backgroundColor = EcoGreen,
                        textColor = White,
                        onClick = onAssignClick
                    )
                }

                else -> {

                    ActionButton(
                        text = "View",
                        backgroundColor = White,
                        textColor = EcoGreen,
                        borderColor = EcoGreen,
                        onClick = onViewClick
                    )
                }
            }
        }
    }
}

// =========================================================
// TABLE CELL
// =========================================================

@Composable
private fun TableCell(
    text: String,
    modifier: Modifier
) {

    Text(
        text = text,
        modifier = modifier,
        color = EcoTextGreen,
        fontSize = 12.sp
    )
}

// =========================================================
// STATUS BADGE
// =========================================================

@Composable
private fun StatusBadge(
    status: String
) {

    val backgroundColor: Color
    val textColor: Color

    when (status) {

        "For Verification" -> {
            backgroundColor = Color(0xFFFFD0D0)
            textColor = Color(0xFFE56A6A)
        }

        "In Progress" -> {
            backgroundColor = Color(0xFFD0F1D7)
            textColor = Color(0xFF39924A)
        }

        "Pending" -> {
            backgroundColor = Color(0xFFF1DFAF)
            textColor = Color(0xFFC0922D)
        }

        "Resolved" -> {
            backgroundColor = Color(0xFFC5DEC9)
            textColor = Color(0xFF3C8550)
        }

        else -> {
            backgroundColor = Color.LightGray
            textColor = Color.DarkGray
        }
    }

    Text(
        text = status,
        color = textColor,
        fontSize = 12.sp,
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(
                horizontal = 7.dp,
                vertical = 4.dp
            )
    )
}

// =========================================================
// ACTION BUTTON
// =========================================================

@Composable
private fun ActionButton(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    borderColor: Color = Color.Transparent,
    onClick: () -> Unit
) {

    Text(
        text = text,
        color = textColor,
        fontSize = 12.sp,
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 9.dp,
                vertical = 4.dp
            )
    )
}