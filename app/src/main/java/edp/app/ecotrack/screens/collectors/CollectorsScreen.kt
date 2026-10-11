package edp.app.ecotrack.screens.collectors

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.BorderStroke
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

// ---------------------------------------------------------
// COLORS - SAME AS DASHBOARD
// ---------------------------------------------------------

private val EcoDarkGreen = Color(0xFF216B2A)
private val EcoGreen = Color(0xFF2E7D32)
private val EcoLightGreen = Color(0xFFE6F5EA)
private val EcoTopBar = Color(0xFFE0F2E5)
private val SelectedGreen = Color(0xFF719873)
private val TextGreen = Color(0xFF286B32)
private val LightBorder = Color(0xFFE1E8E2)
private val ActiveGreen = Color(0xFFCFF7C7)
private val ActiveText = Color(0xFF4C9143)
private val InactiveGreen = Color(0xFF8BAF91)
private val Red = Color(0xFFD6323C)
private val Gray = Color(0xFFE1E1E1)
private val White = Color.White

// ---------------------------------------------------------
// DATA MODEL
// ---------------------------------------------------------

data class Collector(
    val collectorId: String,
    val fullName: String,
    val assignedArea: String,
    val phoneNumber: String,
    val tasks: Int,
    val completed: Int,
    val status: String
)

// ---------------------------------------------------------
// TASK HISTORY DATA
// Each collector has different assigned reports.
// Frontend sample data only.
// ---------------------------------------------------------

private data class TaskHistoryReport(
    val reportId: String,
    val status: String,
    val issue: String,
    val location: String,
    val date: String
)

private data class CollectorTaskHistory(
    val totalTasks: Int,
    val completedTasks: Int,
    val inProgressTasks: Int,
    val reports: List<TaskHistoryReport>
)

private fun getCollectorTaskHistory(
    collectorId: String
): CollectorTaskHistory {
    return when (collectorId) {

        "COL-001" -> CollectorTaskHistory(
            totalTasks = 12,
            completedTasks = 10,
            inProgressTasks = 2,
            reports = listOf(
                TaskHistoryReport(
                    "#1021",
                    "Resolved",
                    "Improper Waste Disposal",
                    "Quezon Blvd. · Brgy. Area A",
                    "Sep 13, 2026"
                ),
                TaskHistoryReport(
                    "#1019",
                    "Resolved",
                    "Overflowing Garbage",
                    "Magsaysay St. · Brgy. Area B",
                    "Sep 12, 2026"
                ),
                TaskHistoryReport(
                    "#1017",
                    "In Progress",
                    "Uncollected Garbage",
                    "Rizal Ave. · Brgy. Area A",
                    "Sep 10, 2026"
                )
            )
        )

        "COL-002" -> CollectorTaskHistory(
            totalTasks = 8,
            completedTasks = 6,
            inProgressTasks = 2,
            reports = listOf(
                TaskHistoryReport(
                    "#1025",
                    "In Progress",
                    "Missed Waste Collection",
                    " Mabini St. · Brgy. Area D",
                    "Sep 15, 2026"
                ),
                TaskHistoryReport(
                    "#1020",
                    "Resolved",
                    "Uncollected Household Waste",
                    "Bonifacio St. · Brgy. Area A",
                    "Sep 12, 2026"
                ),
                TaskHistoryReport(
                    "#1015",
                    "Resolved",
                    "Overflowing Garbage Bin",
                    "Del Pilar St. · Brgy. Area D",
                    "Sep 09, 2026"
                )
            )
        )

        "COL-003" -> CollectorTaskHistory(
            totalTasks = 5,
            completedTasks = 3,
            inProgressTasks = 2,
            reports = listOf(
                TaskHistoryReport(
                    "#1026",
                    "In Progress",
                    "Uncollected Biodegradable Waste",
                    "Andres St. · Brgy. Area C",
                    "Sep 16, 2026"
                ),
                TaskHistoryReport(
                    "#1018",
                    "Resolved",
                    "Improper Garbage Disposal",
                    "Rizal St. · Brgy. Area C",
                    "Sep 11, 2026"
                ),
                TaskHistoryReport(
                    "#1012",
                    "Resolved",
                    "Overflowing Community Bin",
                    "Del Rosario St. · Brgy. Area C",
                    "Sep 07, 2026"
                )
            )
        )

        "COL-004" -> CollectorTaskHistory(
            totalTasks = 3,
            completedTasks = 3,
            inProgressTasks = 0,
            reports = listOf(
                TaskHistoryReport(
                    "#1016",
                    "Resolved",
                    "Scattered Garbage",
                    "Osmena St. · Brgy. Area B",
                    "Sep 10, 2026"
                ),
                TaskHistoryReport(
                    "#1009",
                    "Resolved",
                    "Uncollected Residual Waste",
                    "Burgos St. · Brgy. Area C",
                    "Sep 05, 2026"
                ),
                TaskHistoryReport(
                    "#1004",
                    "Resolved",
                    "Overflowing Waste Container",
                    "Quezon St. · Brgy. Area B",
                    "Sep 02, 2026"
                )
            )
        )

        else -> CollectorTaskHistory(
            totalTasks = 0,
            completedTasks = 0,
            inProgressTasks = 0,
            reports = emptyList()
        )
    }
}

// ---------------------------------------------------------
// MAIN COLLECTORS SCREEN
// ---------------------------------------------------------

@Composable
fun CollectorsScreen(
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onDashboardClick: () -> Unit = {},
    onWasteReportClick: () -> Unit = {},
    onCollectorsClick: () -> Unit = {},
    onResidentsClick: () -> Unit = {},
    onScheduleClick: () -> Unit = {},
    onAnalyticsClick: () -> Unit = {},
    onWasteGuideClick: () -> Unit = {}
) {

    val collectors = remember {
        mutableStateListOf(
            Collector(
                "COL-001",
                "Lito Bautista",
                "Brgy. Area A & B",
                "0912-345-6789",
                12,
                3,
                "Active"
            ),
            Collector(
                "COL-002",
                "Carlos Mendoza",
                "Brgy. Area A & D",
                "0912-345-6789",
                8,
                3,
                "Active"
            ),
            Collector(
                "COL-003",
                "Ramon Dela Cruz",
                "Brgy. Area C",
                "0912-345-6789",
                5,
                3,
                "Active"
            ),
            Collector(
                "COL-004",
                "Felix Ramos",
                "Brgy. Area B & C",
                "0912-345-6789",
                3,
                3,
                "Inactive"
            )
        )
    }

    var showAddDialog by remember {
        mutableStateOf(false)
    }

    var editingCollector by remember {
        mutableStateOf<Collector?>(null)
    }

    var deletingCollector by remember {
        mutableStateOf<Collector?>(null)
    }

    // ADDED: selected collector for task history
    var viewingTaskHistory by remember {
        mutableStateOf<Collector?>(null)
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoLightGreen)
    ) {

        // =================================================
        // SIDEBAR
        // SAME SIZE/DESIGN AS DASHBOARD
        // =================================================

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

            SidebarItem(
                icon = Icons.Default.GridView,
                title = "Dashboard",
                selected = false,
                onClick = onDashboardClick
            )

            SidebarItem(
                icon = Icons.Default.Description,
                title = "Waste Report",
                selected = false,
                onClick = onWasteReportClick
            )

            SidebarItem(
                icon = Icons.Default.LocalShipping,
                title = "Collectors",
                selected = true,
                onClick = onCollectorsClick
            )

            SidebarItem(
                icon = Icons.Default.Groups,
                title = "Residents",
                selected = false,
                onClick = onResidentsClick
            )

            SidebarItem(
                icon = Icons.Default.CalendarMonth,
                title = "Schedules",
                selected = false,
                onClick = onScheduleClick
            )

            SidebarItem(
                icon = Icons.Default.BarChart,
                title = "Analytics",
                selected = false,
                onClick = onAnalyticsClick
            )

            SidebarItem(
                icon = Icons.Default.BarChart,
                title = "Waste Guide",
                selected = false,
                onClick = onWasteGuideClick
            )
        }

        // =================================================
        // MAIN CONTENT
        // =================================================

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {

            // TOP BAR

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp)
                    .background(EcoTopBar)
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
                    text = "Collectors",
                    color = EcoDarkGreen,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.weight(1f))

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

            // PAGE CONTENT

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 30.dp,
                        vertical = 18.dp
                    )
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Garbage Collector Management",
                        color = TextGreen,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            showAddDialog = true
                        },
                        modifier = Modifier
                            .height(31.dp)
                            .width(113.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EcoGreen
                        ),
                        contentPadding = PaddingValues(
                            horizontal = 8.dp
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            modifier = Modifier.size(15.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "Add Collector",
                            fontSize = 10.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(31.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(10.dp),
                            clip = false
                        ),
                    shape = RoundedCornerShape(10.dp),
                    color = Color.White
                ) {

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        CollectorTableHeader()

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(190.dp)
                        ) {

                            items(
                                items = collectors,
                                key = { it.collectorId }
                            ) { collector ->

                                CollectorRow(
                                    collector = collector,

                                    onNameClick = {
                                        viewingTaskHistory = collector
                                    },

                                    onEdit = {
                                        editingCollector = collector
                                    },

                                    onDelete = {
                                        deletingCollector = collector
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // =================================================
    // ADD COLLECTOR DIALOG
    // =================================================

    if (showAddDialog) {
        AddCollectorDialog(
            nextCollectorId = generateNextCollectorId(collectors),

            onDismiss = {
                showAddDialog = false
            },

            onAdd = { fullName, phone, assignedArea, status ->

                collectors.add(
                    Collector(
                        collectorId = generateNextCollectorId(collectors),
                        fullName = fullName,
                        assignedArea = assignedArea,
                        phoneNumber = phone,
                        tasks = 0,
                        completed = 0,
                        status = status
                    )
                )

                showAddDialog = false
            }
        )
    }

    // =================================================
    // EDIT COLLECTOR DIALOG
    // =================================================

    editingCollector?.let { collector ->
        EditCollectorDialog(
            collector = collector,

            onDismiss = {
                editingCollector = null
            },

            onSave = { updatedCollector ->

                val index = collectors.indexOfFirst {
                    it.collectorId == updatedCollector.collectorId
                }

                if (index != -1) {
                    collectors[index] = updatedCollector
                }

                editingCollector = null
            }
        )
    }

    // =================================================
    // DELETE CONFIRMATION
    // =================================================

    deletingCollector?.let { collector ->
        DeleteCollectorDialog(
            collector = collector,

            onDismiss = {
                deletingCollector = null
            },

            onConfirm = {
                collectors.removeAll {
                    it.collectorId == collector.collectorId
                }

                deletingCollector = null
            }
        )
    }

    // =================================================
    // TASK HISTORY DIALOG - ADDED
    // =================================================

    viewingTaskHistory?.let { collector ->
        CollectorTaskHistoryDialog(
            collector = collector,
            onDismiss = {
                viewingTaskHistory = null
            }
        )
    }
}

// ============================================================
// SIDEBAR ITEM
// ============================================================

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
                color = if (selected) SelectedGreen else Color.Transparent,
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

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = title,
            color = White,
            fontSize = 13.sp
        )
    }
}

// =========================================================
// TABLE HEADER
// =========================================================

@Composable
private fun CollectorTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(
                Color(0xFFF1F9F3),
                RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableHeaderText("COLLECTOR ID", 0.14f)
        TableHeaderText("NAME", 0.15f)
        TableHeaderText("ASSIGNED AREA", 0.15f)
        TableHeaderText("PHONE", 0.13f)
        TableHeaderText("TASKS", 0.07f)
        TableHeaderText("COMPLETED", 0.09f)
        TableHeaderText("STATUS", 0.10f)
        TableHeaderText("ACTIONS", 0.12f)
    }
}

// =========================================================
// TABLE ROW
// =========================================================

@Composable
private fun CollectorRow(
    collector: Collector,
    onNameClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        TableText(
            collector.collectorId,
            0.14f
        )

        // CLICKABLE NAME - OPENS THIS COLLECTOR'S TASK HISTORY
        TableNameText(
            text = collector.fullName,
            weight = 0.15f,
            onClick = onNameClick
        )

        TableText(
            collector.assignedArea,
            0.15f
        )

        TableText(
            collector.phoneNumber,
            0.13f
        )

        TableText(
            collector.tasks.toString(),
            0.07f
        )

        TableText(
            collector.completed.toString(),
            0.09f
        )

        // STATUS

        Box(
            modifier = Modifier.weight(0.10f),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (collector.status == "Active") {
                            ActiveGreen
                        } else {
                            InactiveGreen
                        }
                    )
                    .padding(
                        horizontal = 7.dp,
                        vertical = 3.dp
                    )
            ) {
                Text(
                    text = collector.status,
                    color = if (collector.status == "Active") {
                        ActiveText
                    } else {
                        Color(0xFF355C3D)
                    },
                    fontSize = 10.sp
                )
            }
        }

        // ACTIONS

        Row(
            modifier = Modifier.weight(0.12f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onEdit,
                modifier = Modifier.size(29.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Collector",
                    tint = Color(0xFF555555),
                    modifier = Modifier.size(14.dp)
                )
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(29.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Collector",
                    tint = Red,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

// =========================================================
// TABLE TEXT
// =========================================================

@Composable
private fun RowScope.TableText(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        color = TextGreen,
        fontSize = 12.sp,
        modifier = Modifier
            .weight(weight)
            .padding(end = 4.dp)
    )
}

// =========================================================
// CLICKABLE COLLECTOR NAME
// =========================================================

@Composable
private fun RowScope.TableNameText(
    text: String,
    weight: Float,
    onClick: () -> Unit
) {
    Text(
        text = text,
        color = EcoGreen,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .weight(weight)
            .padding(end = 4.dp)
            .clickable(onClick = onClick)
    )
}

@Composable
private fun RowScope.TableHeaderText(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        color = TextGreen,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .weight(weight)
            .padding(end = 4.dp)
    )
}

// ============================================================
// TASK HISTORY DIALOG - ADDED
// ============================================================

@Composable
private fun CollectorTaskHistoryDialog(
    collector: Collector,
    onDismiss: () -> Unit
) {
    val history = remember(collector.collectorId) {
        getCollectorTaskHistory(collector.collectorId)
    }

    val initials = collector.fullName
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.40f)
                .widthIn(max = 620.dp)
                .heightIn(max = 680.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(18.dp)
                ),
            shape = RoundedCornerShape(18.dp),
            color = White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 720.dp)
                    .padding(24.dp)
            ) {

                // HEADER + X CLOSE BUTTON

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "${collector.collectorId}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Red
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            text = "Task History — ${collector.fullName}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // COLLECTOR INFORMATION

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            EcoLightGreen,
                            RoundedCornerShape(12.dp)
                        )
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(EcoDarkGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initials,
                            color = White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = collector.fullName,
                            color = TextGreen,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(0.dp))

                        Text(
                            text = collector.assignedArea,
                            color = TextGreen,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(0.dp))

                        Text(
                            text = collector.phoneNumber,
                            color = Color(0xFF66756A),
                            fontSize = 12.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (collector.status == "Active") {
                                    ActiveGreen
                                } else {
                                    InactiveGreen
                                }
                            )
                            .padding(
                                horizontal = 9.dp,
                                vertical = 5.dp
                            )
                    ) {
                        Text(
                            text = collector.status,
                            color = if (collector.status == "Active") {
                                ActiveText
                            } else {
                                Color(0xFF355C3D)
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // TASK SUMMARY

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TaskSummaryCard(
                        label = "Total Tasks",
                        count = history.totalTasks,
                        modifier = Modifier.weight(1f),
                        background = Color(0xFFF1F9F3),
                        countColor = TextGreen
                    )

                    TaskSummaryCard(
                        label = "Completed",
                        count = history.completedTasks,
                        modifier = Modifier.weight(1f),
                        background = Color(0xFFE1F4E5),
                        countColor = EcoGreen
                    )

                    TaskSummaryCard(
                        label = "In Progress",
                        count = history.inProgressTasks,
                        modifier = Modifier.weight(1f),
                        background = Color(0xFFFFF1D6),
                        countColor = Color(0xFF9A6700)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Assigned Reports",
                    color = TextGreen,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                // DIFFERENT REPORTS FOR EACH COLLECTOR

                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .fillMaxWidth()
                        .heightIn(max = 320.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    if (history.reports.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color(0xFFF8FAF8),
                                    RoundedCornerShape(10.dp)
                                )
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No assigned report history available.",
                                color = Color(0xFF758078),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        history.reports.forEach { report ->
                            TaskHistoryReportCard(report = report)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Showing ${history.reports.size} of ${history.totalTasks} total tasks",
                    color = Color(0xFF758078),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // CLOSE BUTTON

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EcoGreen,
                        contentColor = White
                    )
                ) {
                    Text(
                        text = "Close",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ============================================================
// TASK SUMMARY CARD - ADDED
// ============================================================

@Composable
private fun TaskSummaryCard(
    label: String,
    count: Int,
    modifier: Modifier = Modifier,
    background: Color,
    countColor: Color
) {
    Column(
        modifier = modifier
            .background(
                background,
                RoundedCornerShape(10.dp)
            )
            .padding(
                horizontal = 6.dp,
                vertical = 12.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count.toString(),
            color = countColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            color = TextGreen,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
    }
}

// ============================================================
// TASK HISTORY REPORT CARD - ADDED
// ============================================================

@Composable
private fun TaskHistoryReportCard(
    report: TaskHistoryReport
) {
    val resolved = report.status == "Resolved"

    val statusBackground = if (resolved) {
        Color(0xFFE1F4E5)
    } else {
        Color(0xFFFFF1D6)
    }

    val statusText = if (resolved) {
        EcoGreen
    } else {
        Color(0xFF9A6700)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = LightBorder,
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                White,
                RoundedCornerShape(10.dp)
            )
            .padding(12.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = report.reportId,
                color = TextGreen,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(7.dp))
                    .background(statusBackground)
                    .padding(
                        horizontal = 8.dp,
                        vertical = 4.dp
                    )
            ) {
                Text(
                    text = report.status,
                    color = statusText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(0.dp))

        Text(
            text = report.issue,
            color = TextGreen,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(0.dp))

        Text(
            text = report.location,
            color = Color(0xFF66756A),
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(0.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = if (resolved) EcoGreen else Color(0xFFB7811B),
                modifier = Modifier.size(12.dp)
            )

            Text(
                text = report.date,
                color = Color(0xFF758078),
                fontSize = 12.sp
            )
        }
    }
}

// ============================================================
// ADD COLLECTOR TEXT FIELD
// ============================================================

@Composable
private fun AddCollectorTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .border(
                width = 1.dp,
                color = LightBorder,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = EcoDarkGreen,
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.width(9.dp))

        androidx.compose.foundation.text.BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 12.sp,
                color = TextGreen
            ),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        color = TextGreen
                    )
                }

                innerTextField()
            }
        )
    }
}

// ============================================================
// ADD COLLECTOR DIALOG
// ============================================================

@Composable
private fun AddCollectorDialog(
    nextCollectorId: String,
    onDismiss: () -> Unit,
    onAdd: (
        fullName: String,
        phone: String,
        assignedArea: String,
        status: String
    ) -> Unit
) {
    var fullName by remember {
        mutableStateOf("")
    }

    var phone by remember {
        mutableStateOf("")
    }

    var assignedArea by remember {
        mutableStateOf("")
    }

    var status by remember {
        mutableStateOf("Active")
    }

    var statusExpanded by remember {
        mutableStateOf(false)
    }

    val todayDate = SimpleDateFormat(
        "MMM dd, yyyy",
        Locale.getDefault()
    ).format(Date())

    val canAdd = fullName.isNotBlank() &&
            phone.isNotBlank() &&
            assignedArea.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = White,

        title = {
            Column {
                Text(
                    text = "COLLECTORS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = EcoDarkGreen
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Add New Collector",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextGreen
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Create a new garbage collector account.",
                    fontSize = 11.sp,
                    color = TextGreen
                )
            }
        },

        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AddCollectorTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = "Full Name",
                    icon = Icons.Default.Person
                )

                AddCollectorTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = "Phone Number",
                    icon = Icons.Default.Phone
                )

                AddCollectorTextField(
                    value = assignedArea,
                    onValueChange = { assignedArea = it },
                    label = "Assigned Area / Barangay",
                    icon = Icons.Default.LocationOn
                )

                Box {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .border(
                                width = 1.dp,
                                color = LightBorder,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable {
                                statusExpanded = true
                            }
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Account Status",
                            fontSize = 11.sp,
                            color = TextGreen
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = status,
                            fontSize = 12.sp,
                            color = TextGreen
                        )

                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Status",
                            tint = Gray,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = statusExpanded,
                        onDismissRequest = {
                            statusExpanded = false
                        }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Active") },
                            onClick = {
                                status = "Active"
                                statusExpanded = false
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Inactive") },
                            onClick = {
                                status = "Inactive"
                                statusExpanded = false
                            }
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = EcoLightGreen,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = "System Information",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = EcoDarkGreen
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Assign a unique Collector ID ($nextCollectorId)",
                            fontSize = 11.sp,
                            color = TextGreen
                        )

                        Text(
                            text = "Set join date to today ($todayDate)",
                            fontSize = 11.sp,
                            color = TextGreen
                        )

                        Text(
                            text = "Send login credentials to the provider email",
                            fontSize = 11.sp,
                            color = TextGreen
                        )
                    }
                }
            }
        },

        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 8.dp,
                        end = 8.dp,
                        bottom = 8.dp
                    ),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = EcoGreen
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = EcoGreen
                    )
                ) {
                    Text(
                        text = "Cancel",
                        fontSize = 10.sp
                    )
                }

                Button(
                    onClick = {
                        onAdd(
                            fullName,
                            phone,
                            assignedArea,
                            status
                        )
                    },
                    enabled = canAdd,
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EcoGreen,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Add Collector",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },

        dismissButton = {}
    )
}

// ============================================================
// EDIT COLLECTOR DIALOG
// ============================================================

@Composable
private fun EditCollectorDialog(
    collector: Collector,
    onDismiss: () -> Unit,
    onSave: (Collector) -> Unit
) {
    var fullName by remember {
        mutableStateOf(collector.fullName)
    }

    var phone by remember {
        mutableStateOf(collector.phoneNumber)
    }

    var assignedArea by remember {
        mutableStateOf(collector.assignedArea)
    }

    var status by remember {
        mutableStateOf(collector.status)
    }

    var statusExpanded by remember {
        mutableStateOf(false)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = White,

        title = {
            Column {
                Text(
                    text = "${collector.collectorId} - EDIT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = EcoDarkGreen
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Edit Collector",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextGreen
                )
            }
        },

        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AddCollectorTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = "Full Name",
                    icon = Icons.Default.Person
                )

                AddCollectorTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = "Phone Number",
                    icon = Icons.Default.Phone
                )

                AddCollectorTextField(
                    value = assignedArea,
                    onValueChange = { assignedArea = it },
                    label = "Assigned Area / Barangay",
                    icon = Icons.Default.LocationOn
                )

                Box {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .border(
                                width = 1.dp,
                                color = LightBorder,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable {
                                statusExpanded = true
                            }
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Account Status",
                            fontSize = 11.sp,
                            color = TextGreen
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = status,
                            fontSize = 12.sp,
                            color = TextGreen
                        )

                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Status",
                            tint = Gray,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = statusExpanded,
                        onDismissRequest = {
                            statusExpanded = false
                        }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Active") },
                            onClick = {
                                status = "Active"
                                statusExpanded = false
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Inactive") },
                            onClick = {
                                status = "Inactive"
                                statusExpanded = false
                            }
                        )
                    }
                }
            }
        },

        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 8.dp,
                        end = 8.dp,
                        bottom = 8.dp
                    ),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = EcoGreen
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = EcoGreen
                    )
                ) {
                    Text(
                        text = "Cancel",
                        fontSize = 10.sp
                    )
                }

                Button(
                    onClick = {
                        onSave(
                            collector.copy(
                                fullName = fullName,
                                phoneNumber = phone,
                                assignedArea = assignedArea,
                                status = status
                            )
                        )
                    },
                    enabled = fullName.isNotBlank() &&
                            phone.isNotBlank() &&
                            assignedArea.isNotBlank(),
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EcoGreen,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Save Changes",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },

        dismissButton = {}
    )
}

// ============================================================
// DELETE COLLECTOR DIALOG
// ============================================================

@Composable
private fun DeleteCollectorDialog(
    collector: Collector,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = White,

        title = {
            Column {
                Text(
                    text = "${collector.collectorId}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Red
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Delete Collector",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextGreen
                )
            }
        },

        text = {
            Text(
                text = "Are you sure you want to delete ${collector.fullName}? This action cannot be undone.",
                fontSize = 12.sp,
                color = EcoGreen
            )
        },

        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 8.dp,
                        end = 8.dp,
                        bottom = 8.dp
                    ),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = EcoGreen
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = EcoGreen
                    )
                ) {
                    Text(
                        text = "Cancel",
                        fontSize = 12.sp
                    )
                }

                Button(
                    onClick = onConfirm,
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Red,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Delete",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },

        dismissButton = {}
    )
}

// =========================================================
// GENERATE NEXT COLLECTOR ID
// =========================================================

private fun generateNextCollectorId(
    collectors: List<Collector>
): String {

    if (collectors.isEmpty()) {
        return "COL-001"
    }

    val highestNumber = collectors
        .mapNotNull {
            it.collectorId
                .removePrefix("COL-")
                .toIntOrNull()
        }
        .maxOrNull()
        ?: 0

    return "COL-${(highestNumber + 1)
        .toString()
        .padStart(3, '0')}"
}