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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import edp.app.ecotrack.network.ApiClient
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import org.json.JSONObject

// COLORS

private val EcoDarkGreen = Color(0xFF216B2A)
private val EcoGreen = Color(0xFF2E7D32)
private val EcoLightGreen = Color(0xFFE6F5EA)
private val EcoBorder = Color(0xFF8BE5A7)
private val EcoTextGreen = Color(0xFF286B32)
private val White = Color.White
private val SearchBackground = Color(0xFFE9F5EC)
private val RejectRed = Color(0xFFDC3545)

// WASTE REPORT DATA

data class WasteReport(
    val reportId: String,
    val resident: String,
    val category: String,
    val location: String,
    val date: String,
    val assignedTo: String,
    val status: String,
    val photoUrl: String? = null,
    val description: String = "",
    val barangayId: String = ""
)

// GARBAGE COLLECTOR OPTION

data class GarbageCollectorOption(
    val collectorId: String,
    val name: String,
    val assignedArea: String = "Unassigned Area"
)

// PARSE REPORTS FROM BACKEND

private fun parseWasteReports(
    response: JSONObject
): List<WasteReport> {
    val reportsArray = response.optJSONArray("reports")
        ?: return emptyList()

    val loadedReports = mutableListOf<WasteReport>()

    for (i in 0 until reportsArray.length()) {
        val item = reportsArray.optJSONObject(i) ?: continue

        val reportNumber = item.optString("report_id", "")
        val createdAt = item.optString("created_at", "")
        val reportType = item.optString("report_type", "Other")

        val locationDetails =
            item.optString("location_details", "")
                .takeUnless {
                    it.isBlank() || it == "null"
                } ?: "—"

        val rawStatus = item.optString("status", "Pending")

        android.util.Log.d(
            "EcoTrackReports",
            "Report ID: $reportNumber | Raw status: $rawStatus"
        )

        val formattedStatus = when (
            rawStatus.trim()
                .replace("_", " ")
                .replace("-", " ")
                .lowercase()
        ) {
            "pending" -> "Pending"
            "in progress" -> "In Progress"
            "for verification",
            "waiting for verification" -> "For Verification"
            "resolved", "completed" -> "Resolved"
            "cancelled", "canceled" -> "Cancelled"
            "rejected" -> "Rejected"
            else -> rawStatus
        }

        val residentName =
            item.optString("resident_name", "")
                .takeUnless {
                    it.isBlank() || it == "null"
                }
                ?: item.optString("resident_full_name", "")
                    .takeUnless {
                        it.isBlank() || it == "null"
                    }
                ?: "—"

        val collectorName = if (
            formattedStatus == "Pending"
        ) {
            "-"
        } else {
            item.optString("assigned_collector_name", "")
                .takeUnless {
                    it.isBlank() ||
                            it == "null" ||
                            it == "—"
                } ?: "-"
        }

        loadedReports.add(
            WasteReport(
                reportId = if (reportNumber.isBlank()) {
                    "—"
                } else {
                    "#$reportNumber"
                },
                resident = residentName,
                category = reportType,
                location = locationDetails,
                date = if (createdAt.isBlank()) {
                    "—"
                } else {
                    createdAt.take(10)
                },
                assignedTo = collectorName,
                status = formattedStatus,
                photoUrl = item.optString("photo_url", "")
                    .takeUnless {
                        it.isBlank() || it == "null"
                    },
                description = item.optString("description", "")
                    .takeUnless { it == "null" } ?: "",
                barangayId = item.optString("barangay_id", "")
                    .takeUnless { it == "null" } ?: ""
            )
        )
    }

    return loadedReports
}

// MAIN WASTE REPORT SCREEN

@Composable
fun WasteReportScreen(
    accessToken: String,
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

    var reports by remember {
        mutableStateOf<List<WasteReport>>(emptyList())
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
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

    var showRejectConfirmation by remember {
        mutableStateOf(false)
    }

    // REASSIGN STATES

    var showEditReport by remember {
        mutableStateOf(false)
    }

    var showReassignConfirmation by remember {
        mutableStateOf(false)
    }

    var selectedReassignReport by remember {
        mutableStateOf<WasteReport?>(null)
    }

    var selectedReassignCollector by remember {
        mutableStateOf<GarbageCollectorOption?>(null)
    }

    var rejectError by remember {
        mutableStateOf<String?>(null)
    }

    var reassignError by remember {
        mutableStateOf<String?>(null)
    }

    var refreshVersion by remember {
        mutableLongStateOf(0L)
    }

    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    // LOAD REPORTS

    LaunchedEffect(accessToken, refreshVersion) {
        if (accessToken.isBlank()) {
            reports = emptyList()
            errorMessage = "Please log in as an admin."
            isLoading = false
            return@LaunchedEffect
        }

        isLoading = true
        errorMessage = null

        try {
            reports = parseWasteReports(
                ApiClient.getWasteReports(accessToken)
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            errorMessage =
                e.message ?: "Failed to load waste reports."
        } finally {
            isLoading = false
        }
    }

    // REFRESH WHEN APP RETURNS TO FOREGROUND

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                refreshVersion++
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // FILTER REPORTS

    val filteredReports = reports.filter { report ->
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
                    ) ||
                    report.assignedTo.contains(
                        searchText,
                        ignoreCase = true
                    )

        matchesFilter && matchesSearch
    }

    // MAIN LAYOUT

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoLightGreen)
    ) {
        WasteReportSidebar(
            onDashboardClick = onDashboardClick,
            onCollectorsClick = onCollectorsClick,
            onResidentsClick = onResidentsClick,
            onScheduleClick = onScheduleClick,
            onAnalyticsClick = onAnalyticsClick,
            onWasteGuideClick = onWasteGuideClick
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            WasteReportTopBar(
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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "All Waste Reports",
                        color = EcoTextGreen,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // FILTER BUTTONS
                // "For Verification" is intentionally NOT a filter button.
                // It remains a report status displayed in the table.

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(
                                "All",
                                "Pending",
                                "In Progress",
                                "For Verification",
                                "Resolved",
                                "Cancelled",
                                "Rejected"
                            ).forEach { filter ->
                                WasteFilterButton(
                                    text = filter,
                                    selected = selectedFilter == filter
                                ) {
                                    selectedFilter = filter
                                }
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        SearchBar(
                            searchText = searchText,
                            onSearchTextChange = {
                                searchText = it
                            }
                        )
                    }
                }

                item {
                    when {
                        isLoading -> {
                            Text(
                                text = "Loading waste reports...",
                                color = EcoTextGreen,
                                modifier = Modifier.padding(16.dp)
                            )
                        }

                        errorMessage != null -> {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = errorMessage ?: "",
                                    color = Color.Red
                                )

                                ActionButton(
                                    text = "Retry",
                                    backgroundColor = EcoGreen,
                                    textColor = White,
                                    onClick = {
                                        refreshVersion++
                                    }
                                )
                            }
                        }

                        else -> {
                            WasteReportTable(
                                reports = filteredReports,
                                onReviewClick = { report ->
                                    selectedReviewReport = report
                                    onReviewClick(report)
                                },
                                onAssignClick = { report ->
                                    selectedAssignReport = report
                                    onAssignClick(report)
                                },
                                onViewClick = { report ->
                                    selectedViewReport = report
                                    onViewClick(report)
                                }
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }

    // REVIEW POPUP

    selectedReviewReport?.let { report ->
        ReviewReportScreen(
            report = report,
            onDismiss = {
                selectedReviewReport = null
            },
            onApproveReport = {
                coroutineScope.launch {
                    isLoading = true
                    errorMessage = null

                    try {
                        ApiClient.resolveReport(
                            accessToken = accessToken,
                            reportId = report.reportId.removePrefix("#")
                        )

                        selectedReviewReport = null
                        refreshVersion++
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        errorMessage =
                            e.message ?: "Failed to approve the report."
                    } finally {
                        isLoading = false
                    }
                }
            }
        )
    }

    // ASSIGN POPUP

    selectedAssignReport?.let { report ->
        AssignReportScreen(
            report = report,
            accessToken = accessToken,
            onDismiss = {
                selectedAssignReport = null
            },
            onAssignCollector = { collector ->
                coroutineScope.launch {
                    isLoading = true
                    errorMessage = null

                    try {
                        val numericReportId =
                            report.reportId.removePrefix("#")

                        require(numericReportId.toIntOrNull() != null) {
                            "Invalid report ID. Please reload the reports."
                        }

                        require(collector.collectorId.toIntOrNull() != null) {
                            "Invalid collector ID. Please reload the collectors."
                        }

                        ApiClient.assignCollector(
                            accessToken = accessToken,
                            reportId = numericReportId,
                            collectorId = collector.collectorId
                        )

                        reports = parseWasteReports(
                            ApiClient.getWasteReports(accessToken)
                        )

                        selectedAssignReport = null
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        errorMessage =
                            e.message ?: "Failed to assign collector."
                    } finally {
                        isLoading = false
                    }
                }
            }
        )
    }

    // VIEW POPUP

    selectedViewReport?.let { report ->
        ViewReportScreen(
            report = report,
            onDismiss = {
                selectedViewReport = null
            },
            onAssign = {
                selectedViewReport = null
                selectedAssignReport = report
            },
            onReject = {
                rejectError = null
                showRejectConfirmation = true
            },
            onEdit = {
                if (report.status.equals("In Progress", ignoreCase = true)) {
                    selectedReassignReport = report
                    selectedReassignCollector = null
                    reassignError = null
                    showEditReport = true

                    // Close the View popup before opening Edit.
                    selectedViewReport = null
                }
            },
            onAcceptResolve = {
                coroutineScope.launch {
                    isLoading = true
                    errorMessage = null

                    try {
                        ApiClient.resolveReport(
                            accessToken = accessToken,
                            reportId = report.reportId.removePrefix("#")
                        )

                        selectedViewReport = null
                        refreshVersion++
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        errorMessage =
                            e.message ?: "Failed to resolve the report."
                    } finally {
                        isLoading = false
                    }
                }
            }
        )
    }

    // REJECT CONFIRMATION

    if (showRejectConfirmation) {
        AlertDialog(
            onDismissRequest = {
                if (!isLoading) {
                    showRejectConfirmation = false
                    rejectError = null
                }
            },
            containerColor = White,
            shape = RoundedCornerShape(12.dp),
            title = {
                Text(
                    text = "Confirm Rejection",
                    color = EcoDarkGreen,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Are you sure you want to reject this waste report?",
                        color = EcoTextGreen
                    )

                    rejectError?.let {
                        Text(
                            text = it,
                            color = RejectRed,
                            fontSize = 12.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    enabled = !isLoading,
                    onClick = {
                        val report = selectedViewReport
                            ?: return@Button

                        if (!report.status.equals(
                                "For Verification",
                                ignoreCase = true
                            )
                        ) {
                            rejectError =
                                "Only For Verification reports can be rejected."
                            return@Button
                        }

                        coroutineScope.launch {
                            isLoading = true
                            rejectError = null

                            try {
                                ApiClient.rejectWasteReport(
                                    accessToken = accessToken,
                                    reportId = report.reportId.removePrefix("#")
                                )

                                showRejectConfirmation = false
                                selectedViewReport = null
                                refreshVersion++
                            } catch (e: CancellationException) {
                                throw e
                            } catch (e: Exception) {
                                rejectError =
                                    e.message
                                        ?: "Failed to reject the report."
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RejectRed
                    )
                ) {
                    Text(
                        if (isLoading) {
                            "Rejecting..."
                        } else {
                            "Confirm Reject"
                        }
                    )
                }
            },
            dismissButton = {
                TextButton(
                    enabled = !isLoading,
                    onClick = {
                        showRejectConfirmation = false
                        rejectError = null
                    }
                ) {
                    Text("Cancel", color = EcoGreen)
                }
            }
        )
    }

    // EDIT POPUP: IN PROGRESS REPORTS ONLY
    // This popup contains only garbage collector selection.

    if (showEditReport) {
        val report = selectedReassignReport

        if (report != null) {
            ReassignCollectorDialog(
                report = report,
                accessToken = accessToken,
                onDismiss = {
                    if (!isLoading) {
                        showEditReport = false
                        selectedReassignReport = null
                        selectedReassignCollector = null
                        reassignError = null
                    }
                },
                onRequestReassign = { collector ->
                    selectedReassignCollector = collector
                    reassignError = null
                    showEditReport = false
                    showReassignConfirmation = true
                }
            )
        }
    }

    // CONFIRM REASSIGNMENT

    if (showReassignConfirmation) {
        val report = selectedReassignReport
        val collector = selectedReassignCollector

        AlertDialog(
            onDismissRequest = {
                if (!isLoading) {
                    showReassignConfirmation = false
                    selectedReassignReport = null
                    selectedReassignCollector = null
                    reassignError = null
                }
            },
            containerColor = White,
            shape = RoundedCornerShape(12.dp),
            title = {
                Text(
                    text = "Confirm Re-assignment",
                    color = EcoDarkGreen,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (report != null && collector != null) {
                            "Are you sure you want to reassign report " +
                                    "${report.reportId} to ${collector.name}?"
                        } else {
                            "Please select a garbage collector."
                        },
                        color = EcoTextGreen
                    )

                    reassignError?.let {
                        Text(
                            text = it,
                            color = RejectRed,
                            fontSize = 12.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    enabled = !isLoading &&
                            report != null &&
                            collector != null,
                    onClick = {
                        val currentReport = report ?: return@Button
                        val selectedCollector = collector ?: return@Button

                        coroutineScope.launch {
                            isLoading = true
                            reassignError = null

                            try {
                                val numericReportId =
                                    currentReport.reportId.removePrefix("#")

                                require(numericReportId.toIntOrNull() != null) {
                                    "Invalid report ID. Please refresh the reports."
                                }

                                require(
                                    selectedCollector.collectorId.toIntOrNull() != null
                                ) {
                                    "Invalid collector ID. Please reload the collectors."
                                }

                                // Save the new collector assignment.

                                ApiClient.assignCollector(
                                    accessToken = accessToken,
                                    reportId = numericReportId,
                                    collectorId = selectedCollector.collectorId
                                )

                                // Reload reports after reassignment.

                                reports = parseWasteReports(
                                    ApiClient.getWasteReports(accessToken)
                                )

                                showReassignConfirmation = false
                                showEditReport = false
                                selectedReassignReport = null
                                selectedReassignCollector = null
                                selectedViewReport = null
                                refreshVersion++
                            } catch (e: CancellationException) {
                                throw e
                            } catch (e: Exception) {
                                reassignError =
                                    e.message
                                        ?: "Failed to reassign the garbage collector."
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EcoGreen
                    )
                ) {
                    Text(
                        if (isLoading) {
                            "Re-assigning..."
                        } else {
                            "Confirm Re-assign"
                        }
                    )
                }
            },
            dismissButton = {
                TextButton(
                    enabled = !isLoading,
                    onClick = {
                        showReassignConfirmation = false
                        selectedReassignReport = null
                        selectedReassignCollector = null
                        reassignError = null
                    }
                ) {
                    Text("Cancel", color = EcoGreen)
                }
            }
        )
    }
}

// REASSIGN GARBAGE COLLECTOR DIALOG

@Composable
private fun ReassignCollectorDialog(
    report: WasteReport,
    accessToken: String,
    onDismiss: () -> Unit,
    onRequestReassign: (GarbageCollectorOption) -> Unit
) {
    var availableCollectors by remember {
        mutableStateOf<List<GarbageCollectorOption>>(emptyList())
    }

    var selectedCollector by remember {
        mutableStateOf<GarbageCollectorOption?>(null)
    }

    var isLoadingCollectors by remember {
        mutableStateOf(true)
    }

    var collectorError by remember {
        mutableStateOf<String?>(null)
    }

    // LOAD ACTIVE GARBAGE COLLECTORS

    LaunchedEffect(accessToken) {
        isLoadingCollectors = true
        collectorError = null

        try {
            val response = ApiClient.getCollectors(accessToken)
            val collectorArray = response.optJSONArray("collectors")

            val loadedCollectors = mutableListOf<GarbageCollectorOption>()

            if (collectorArray != null) {
                for (i in 0 until collectorArray.length()) {
                    val item = collectorArray.optJSONObject(i)
                        ?: continue

                    if (!item.optBoolean("is_active", false)) {
                        continue
                    }

                    val userId = item.optString("user_id", "")

                    if (userId.toIntOrNull() == null) {
                        continue
                    }

                    val fullName = item.optString(
                        "full_name",
                        "Unknown Collector"
                    ).takeUnless {
                        it.isBlank() || it == "null"
                    } ?: "Unknown Collector"

                    val barangayName =
                        item.optJSONObject("barangays")
                            ?.optString("barangay_name")
                            ?.takeIf {
                                !it.isNullOrBlank() && it != "null"
                            } ?: "Unassigned Area"

                    loadedCollectors.add(
                        GarbageCollectorOption(
                            collectorId = userId,
                            name = fullName,
                            assignedArea = barangayName
                        )
                    )
                }
            }

            availableCollectors = loadedCollectors
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            collectorError =
                e.message ?: "Could not load collectors. Please try again."
        } finally {
            isLoadingCollectors = false
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = White,
        shape = RoundedCornerShape(12.dp),
        title = {
            Text(
                text = "Edit Assigned Garbage Collector",
                color = EcoDarkGreen,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Report ${report.reportId}",
                    color = EcoTextGreen,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Assigned Garbage Collector",
                    color = EcoTextGreen,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Currently assigned: ${report.assignedTo}",
                    color = Color.Gray,
                    fontSize = 12.sp
                )

                when {
                    isLoadingCollectors -> {
                        Text(
                            text = "Loading garbage collectors...",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }

                    collectorError != null -> {
                        Text(
                            text = collectorError ?: "",
                            color = RejectRed,
                            fontSize = 12.sp
                        )
                    }

                    availableCollectors.isEmpty() -> {
                        Text(
                            text = "No active garbage collectors found.",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            items(
                                items = availableCollectors,
                                key = { it.collectorId }
                            ) { collector ->

                                val isCurrentCollector =
                                    collector.name.equals(
                                        report.assignedTo,
                                        ignoreCase = true
                                    )

                                val isSelected =
                                    selectedCollector == collector

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color = if (isSelected) {
                                                EcoLightGreen
                                            } else {
                                                White
                                            },
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = if (isSelected) {
                                                EcoGreen
                                            } else {
                                                Color(0xFFE0E7E1)
                                            },
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable {
                                            selectedCollector = collector
                                        }
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = collector.name,
                                            color = EcoTextGreen,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium
                                        )

                                        Text(
                                            text = collector.assignedArea +
                                                    if (isCurrentCollector) {
                                                        " · Currently assigned"
                                                    } else {
                                                        ""
                                                    },
                                            color = Color(0xFF58A86B),
                                            fontSize = 11.sp
                                        )
                                    }

                                    if (isSelected) {
                                        Text(
                                            text = "Selected",
                                            color = EcoGreen,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedCollector?.let { collector ->
                        onRequestReassign(collector)
                    }
                },
                enabled = selectedCollector != null &&
                        !isLoadingCollectors &&
                        collectorError == null &&
                        selectedCollector?.name?.equals(
                            report.assignedTo,
                            ignoreCase = true
                        ) != true,
                colors = ButtonDefaults.buttonColors(
                    containerColor = EcoGreen,
                    disabledContainerColor = Color(0xFFB7CDBA)
                )
            ) {
                Text("Re-assign")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = EcoGreen)
            }
        }
    )
}

// SIDEBAR

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
                .border(1.dp, Color(0xFF4A8A50))
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

            Spacer(modifier = Modifier.height(6.dp))

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

        Spacer(modifier = Modifier.height(20.dp))

        WasteSidebarItem(
            icon = Icons.Default.GridView,
            title = "Dashboard",
            onClick = onDashboardClick
        )

        WasteSidebarItem(
            icon = Icons.Default.Description,
            title = "Waste Report",
            selected = true
        )

        WasteSidebarItem(
            icon = Icons.Default.LocalShipping,
            title = "Collectors",
            onClick = onCollectorsClick
        )

        WasteSidebarItem(
            icon = Icons.Default.Groups,
            title = "Residents",
            onClick = onResidentsClick
        )

        WasteSidebarItem(
            icon = Icons.Default.CalendarMonth,
            title = "Schedules",
            onClick = onScheduleClick
        )

        WasteSidebarItem(
            icon = Icons.Default.BarChart,
            title = "Analytics",
            onClick = onAnalyticsClick
        )

        WasteSidebarItem(
            icon = Icons.Default.MenuBook,
            title = "Waste Guide",
            onClick = onWasteGuideClick
        )
    }
}

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

// TOP BAR

@Composable
private fun WasteReportTopBar(
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
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            tint = EcoDarkGreen,
            modifier = Modifier.size(23.dp)
        )

        Spacer(modifier = Modifier.width(15.dp))

        Text(
            text = "Waste Reports",
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

// FILTER BUTTON

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
                color = if (selected) EcoGreen else White,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = if (selected) EcoGreen else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) White else EcoGreen,
            fontSize = 12.sp
        )
    }
}

// SEARCH BAR

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
                SearchBackground,
                RoundedCornerShape(15.dp)
            )
            .border(
                1.dp,
                EcoBorder,
                RoundedCornerShape(15.dp)
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

        Spacer(modifier = Modifier.width(5.dp))

        BasicTextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 12.sp,
                color = EcoTextGreen
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// REPORT TABLE

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
                4.dp,
                RoundedCornerShape(13.dp)
            )
            .background(
                White,
                RoundedCornerShape(13.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF2FAF4))
                .padding(
                    horizontal = 12.dp,
                    vertical = 10.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TableHeader("REPORT\nID", Modifier.weight(0.8f))
            TableHeader("RESIDENT", Modifier.weight(1.15f))
            TableHeader("CATEGORY", Modifier.weight(1.55f))
            TableHeader("LOCATION", Modifier.weight(1.2f))
            TableHeader("DATE", Modifier.weight(1.25f))
            TableHeader("ASSIGNED TO", Modifier.weight(1.3f))
            TableHeader("STATUS", Modifier.weight(1f))
            TableHeader("ACTION", Modifier.weight(0.9f))
        }

        reports.forEach { report ->
            WasteReportRow(
                report = report,
                onViewClick = {
                    onViewClick(report)
                }
            )
        }

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

@Composable
private fun WasteReportRow(
    report: WasteReport,
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
        TableCell(report.reportId, Modifier.weight(0.8f))
        TableCell(report.resident, Modifier.weight(1.15f))
        TableCell(report.category, Modifier.weight(1.55f))
        TableCell(report.location, Modifier.weight(1.2f))
        TableCell(report.date, Modifier.weight(1.25f))
        TableCell(report.assignedTo, Modifier.weight(1.3f))

        Box(modifier = Modifier.weight(1f)) {
            StatusBadge(report.status)
        }

        Box(modifier = Modifier.weight(0.9f)) {
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

// STATUS BADGE
// "For Verification" is a status, not a filter button.

@Composable
private fun StatusBadge(status: String) {
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

        "Rejected" -> {
            backgroundColor = Color(0xFFFFDADA)
            textColor = RejectRed
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
                backgroundColor,
                RoundedCornerShape(10.dp)
            )
            .padding(
                horizontal = 7.dp,
                vertical = 4.dp
            )
    )
}

// ACTION BUTTON

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
                backgroundColor,
                RoundedCornerShape(10.dp)
            )
            .border(
                1.dp,
                borderColor,
                RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(
                horizontal = 9.dp,
                vertical = 4.dp
            )
    )
}