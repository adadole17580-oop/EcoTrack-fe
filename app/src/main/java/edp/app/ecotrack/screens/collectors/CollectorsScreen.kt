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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

            // -------------------------------------------------
            // ECOTRACK LOGO
            // -------------------------------------------------

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

            // -------------------------------------------------
            // ADMINISTRATOR INFORMATION
            // SAME BORDER/LINE AS DASHBOARD
            // -------------------------------------------------

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

            // -------------------------------------------------
            // SIDEBAR MENU
            // -------------------------------------------------

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

            // -------------------------------------------------
            // TOP BAR
            // EXACT SAME SIZE/DESIGN AS DASHBOARD
            // -------------------------------------------------

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

                Spacer(
                    modifier = Modifier.width(15.dp)
                )

                Text(
                    text = "Collectors",
                    color = EcoDarkGreen,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                // -------------------------------------------------
                // NOTIFICATION
                // -------------------------------------------------

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

                // -------------------------------------------------
                // PROFILE
                // -------------------------------------------------

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

                // -------------------------------------------------
                // PAGE TITLE + ADD BUTTON
                // -------------------------------------------------

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

                    Spacer(
                        modifier = Modifier.weight(1f)
                    )

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

                        Spacer(
                            modifier = Modifier.width(4.dp)
                        )

                        Text(
                            text = "Add Collector",
                            fontSize = 10.sp
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(31.dp)
                )

                // -------------------------------------------------
                // TABLE
                // -------------------------------------------------

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

                        // TABLE HEADER

                        CollectorTableHeader()

                        // TABLE ROWS

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(190.dp)
                        ) {

                            items(
                                items = collectors,
                                key = {
                                    it.collectorId
                                }
                            ) { collector ->

                                CollectorRow(
                                    collector = collector,

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

                val index =
                    collectors.indexOfFirst {
                        it.collectorId ==
                                updatedCollector.collectorId
                    }

                if (index != -1) {

                    collectors[index] =
                        updatedCollector
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
                    it.collectorId ==
                            collector.collectorId
                }

                deletingCollector = null
            }
        )
    }
}

// ============================================================
// SIDEBAR ITEM
// SAME AS DASHBOARD
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
                color =
                    if (selected) {
                        SelectedGreen
                    } else {
                        Color.Transparent
                    },
                shape = RoundedCornerShape(22.dp)
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
// TABLE HEADER
// =========================================================

@Composable
private fun CollectorTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color(0xFFF1F9F3), RoundedCornerShape(10.dp))
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
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .padding(
                horizontal = 10.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        TableText(
            collector.collectorId,
            0.14f
        )

        TableText(
            collector.fullName,
            0.15f
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

        // -------------------------------------------------
        // STATUS
        // -------------------------------------------------

        Box(
            modifier = Modifier
                .weight(0.10f),
            contentAlignment = Alignment.CenterStart
        ) {

            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(8.dp)
                    )
                    .background(
                        if (collector.status == "Active")
                            ActiveGreen
                        else
                            InactiveGreen
                    )
                    .padding(
                        horizontal = 7.dp,
                        vertical = 3.dp
                    )
            ) {

                Text(
                    text = collector.status,
                    color =
                        if (collector.status == "Active")
                            ActiveText
                        else
                            Color(0xFF355C3D),
                    fontSize = 10.sp
                )
            }
        }

        // -------------------------------------------------
        // ACTIONS
        // -------------------------------------------------

        Row(
            modifier = Modifier.weight(0.12f),
            horizontalArrangement =
                Arrangement.Start,
            verticalAlignment =
                Alignment.CenterVertically
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
            .padding(
                end = 4.dp
            )
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

        Spacer(
            modifier = Modifier.width(9.dp)
        )

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

    val canAdd =
        fullName.isNotBlank() &&
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

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = "Add New Collector",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextGreen
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

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

                // FULL NAME
                AddCollectorTextField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                    },
                    label = "Full Name",
                    icon = Icons.Default.Person
                )

                // PHONE NUMBER
                AddCollectorTextField(
                    value = phone,
                    onValueChange = {
                        phone = it
                    },
                    label = "Phone Number",
                    icon = Icons.Default.Phone
                )

                // ASSIGNED AREA
                AddCollectorTextField(
                    value = assignedArea,
                    onValueChange = {
                        assignedArea = it
                    },
                    label = "Assigned Area / Barangay",
                    icon = Icons.Default.LocationOn
                )

                // =================================================
                // STATUS DROPDOWN
                // =================================================

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

                        Spacer(
                            modifier = Modifier.weight(1f)
                        )

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
                            text = {
                                Text("Active")
                            },
                            onClick = {
                                status = "Active"
                                statusExpanded = false
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                Text("Inactive")
                            },
                            onClick = {
                                status = "Inactive"
                                statusExpanded = false
                            }
                        )
                    }
                }

                // =================================================
                // SYSTEM INFORMATION
                // =================================================

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

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

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

        // =====================================================
        // BUTTONS
        // =====================================================

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

                // CANCEL
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

                // ADD COLLECTOR
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

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

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

                // FULL NAME
                AddCollectorTextField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                    },
                    label = "Full Name",
                    icon = Icons.Default.Person
                )

                // PHONE
                AddCollectorTextField(
                    value = phone,
                    onValueChange = {
                        phone = it
                    },
                    label = "Phone Number",
                    icon = Icons.Default.Phone
                )

                // ASSIGNED AREA
                AddCollectorTextField(
                    value = assignedArea,
                    onValueChange = {
                        assignedArea = it
                    },
                    label = "Assigned Area / Barangay",
                    icon = Icons.Default.LocationOn
                )

                // =================================================
                // STATUS DROPDOWN
                // =================================================

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

                        Spacer(
                            modifier = Modifier.weight(1f)
                        )

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
                            text = {
                                Text("Active")
                            },
                            onClick = {
                                status = "Active"
                                statusExpanded = false
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                Text("Inactive")
                            },
                            onClick = {
                                status = "Inactive"
                                statusExpanded = false
                            }
                        )
                    }
                }
            }
        },

        // =====================================================
        // BUTTONS
        // =====================================================

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

                // CANCEL
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

                // SAVE CHANGES
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

                    enabled =
                        fullName.isNotBlank() &&
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
                    text = "${collector.collectorId} - DELETE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Red
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

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
                color = TextGreen
            )
        },

        // =====================================================
        // BUTTONS
        // =====================================================

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

                // CANCEL
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

                // DELETE
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
                        fontSize = 10.sp,
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