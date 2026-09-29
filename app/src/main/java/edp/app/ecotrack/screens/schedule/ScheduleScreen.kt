package edp.app.ecotrack.screens.schedule

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog


// ============================================================
// COLORS
// SAME AS DASHBOARD / COLLECTORS / RESIDENTS
// ============================================================

private val EcoDarkGreen = Color(0xFF216B2A)
private val EcoGreen = Color(0xFF2E7D32)
private val EcoLightGreen = Color(0xFFE6F5EA)
private val EcoTopBar = Color(0xFFE0F2E5)

private val SelectedGreen = Color(0xFF719873)

private val TextGreen = Color(0xFF286B32)
private val White = Color.White

private val LightBorder = Color(0xFFD5E8D9)
private val DeleteRed = Color(0xFFE05D69)


// ============================================================
// SCHEDULE MODEL
// ============================================================

data class CollectionSchedule(
    val id: String,
    val barangayArea: String,
    val collectionDays: List<String>,
    val collectionStartTime: String,
    val collectorId: String
)


// ============================================================
// MAIN SCHEDULE SCREEN
// ============================================================

@Composable
fun ScheduleScreen(
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},

    onDashboardClick: () -> Unit = {},
    onWasteReportClick: () -> Unit = {},
    onCollectorsClick: () -> Unit = {},
    onResidentsClick: () -> Unit = {},
    onAnalyticsClick: () -> Unit = {},
    onWasteGuideClick: () -> Unit = {},

    hasUnreadNotifications: Boolean = true
) {

    var schedules by remember {
        mutableStateOf(
            listOf(
                CollectionSchedule(
                    id = "SCH-001",
                    barangayArea = "Brgy. Area A",
                    collectionDays = listOf(
                        "Mon",
                        "Wed",
                        "Fri"
                    ),
                    collectionStartTime = "08:00 AM",
                    collectorId = "COL-001"
                ),

                CollectionSchedule(
                    id = "SCH-002",
                    barangayArea = "Brgy. Area B",
                    collectionDays = listOf(
                        "Tue",
                        "Thu",
                        "Sat"
                    ),
                    collectionStartTime = "09:00 AM",
                    collectorId = "COL-002"
                ),

                CollectionSchedule(
                    id = "SCH-003",
                    barangayArea = "Brgy. Area C",
                    collectionDays = listOf(
                        "Mon",
                        "Tue",
                        "Thu"
                    ),
                    collectionStartTime = "07:30 AM",
                    collectorId = "COL-003"
                )
            )
        )
    }

    var showAddSchedule by remember {
        mutableStateOf(false)
    }

    var scheduleToEdit by remember {
        mutableStateOf<CollectionSchedule?>(null)
    }

    var scheduleToDelete by remember {
        mutableStateOf<CollectionSchedule?>(null)
    }


    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoLightGreen)
    ) {

        // ====================================================
        // SIDEBAR
        // ====================================================

        ScheduleSidebar(
            onDashboardClick = onDashboardClick,
            onWasteReportClick = onWasteReportClick,
            onCollectorsClick = onCollectorsClick,
            onResidentsClick = onResidentsClick,
            onAnalyticsClick = onAnalyticsClick,
            onWasteGuideClick = onWasteGuideClick
        )


        // ====================================================
        // MAIN CONTENT
        // ====================================================

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {

            // =================================================
            // TOP BAR
            // =================================================

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
                    text = "Schedules",
                    color = EcoDarkGreen,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(
                    modifier = Modifier.weight(1f)
                )


                // NOTIFICATION

                Box(
                    contentAlignment = Alignment.TopEnd
                ) {

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

                    if (hasUnreadNotifications) {

                        Box(
                            modifier = Modifier
                                .padding(
                                    top = 8.dp,
                                    end = 8.dp
                                )
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(EcoGreen)
                        )
                    }
                }


                // PROFILE

                IconButton(
                    onClick = onProfileClick
                ) {

                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "My Profile",
                        tint = EcoDarkGreen,
                        modifier = Modifier.size(31.dp)
                    )
                }
            }


            // =================================================
            // CONTENT
            // =================================================

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
                        text = "Collection Schedules",
                        color = TextGreen,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(
                        modifier = Modifier.weight(1f)
                    )


                    // ADD SCHEDULE BUTTON

                    Button(
                        onClick = {
                            showAddSchedule = true
                        },

                        colors = ButtonDefaults.buttonColors(
                            containerColor = EcoGreen
                        ),

                        shape = RoundedCornerShape(8.dp),

                        modifier = Modifier.height(34.dp),

                        contentPadding = PaddingValues(
                            horizontal = 12.dp
                        )
                    ) {

                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp)
                        )

                        Spacer(
                            modifier = Modifier.width(4.dp)
                        )

                        Text(
                            text = "Add Schedule",
                            fontSize = 10.sp
                        )
                    }
                }


                Spacer(
                    modifier = Modifier.height(18.dp)
                )


                // =================================================
                // TABLE
                // =================================================

                Surface(
                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(10.dp),

                    color = White,

                    shadowElevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        ScheduleTableHeader()

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                        ) {

                            items(
                                items = schedules,
                                key = {
                                    it.id
                                }
                            ) { schedule ->

                                ScheduleRow(
                                    schedule = schedule,

                                    onEdit = {
                                        scheduleToEdit = schedule
                                    },

                                    onDelete = {
                                        scheduleToDelete = schedule
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    // ========================================================
    // ADD SCHEDULE POPUP
    // ========================================================

    if (showAddSchedule) {

        AddSchedulePopup(

            onDismiss = {
                showAddSchedule = false
            },

            onCreate = { newSchedule ->

                schedules = schedules + newSchedule

                showAddSchedule = false
            }
        )
    }


    // ========================================================
    // EDIT SCHEDULE POPUP
    // ========================================================

    scheduleToEdit?.let { schedule ->

        EditSchedulePopup(

            schedule = schedule,

            onDismiss = {
                scheduleToEdit = null
            },

            onSave = { updatedSchedule ->

                schedules = schedules.map {

                    if (it.id == updatedSchedule.id) {
                        updatedSchedule
                    } else {
                        it
                    }
                }

                scheduleToEdit = null
            }
        )
    }


    // ========================================================
    // DELETE CONFIRMATION
    // ========================================================

    scheduleToDelete?.let { schedule ->

        DeleteScheduleConfirmation(

            schedule = schedule,

            onDismiss = {
                scheduleToDelete = null
            },

            onConfirm = {

                schedules = schedules.filter {
                    it.id != schedule.id
                }

                scheduleToDelete = null
            }
        )
    }
}


// ============================================================
// SIDEBAR
// ============================================================

@Composable
private fun ScheduleSidebar(
    onDashboardClick: () -> Unit,
    onWasteReportClick: () -> Unit,
    onCollectorsClick: () -> Unit,
    onResidentsClick: () -> Unit,
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


        ScheduleSidebarItem(
            icon = Icons.Default.GridView,
            text = "Dashboard",
            selected = false,
            onClick = onDashboardClick
        )

        ScheduleSidebarItem(
            icon = Icons.Default.Description,
            text = "Waste Report",
            selected = false,
            onClick = onWasteReportClick
        )

        ScheduleSidebarItem(
            icon = Icons.Default.LocalShipping,
            text = "Collectors",
            selected = false,
            onClick = onCollectorsClick
        )

        ScheduleSidebarItem(
            icon = Icons.Default.Groups,
            text = "Residents",
            selected = false,
            onClick = onResidentsClick
        )

        ScheduleSidebarItem(
            icon = Icons.Default.CalendarMonth,
            text = "Schedules",
            selected = true,
            onClick = {}
        )

        ScheduleSidebarItem(
            icon = Icons.Default.BarChart,
            text = "Analytics",
            selected = false,
            onClick = onAnalyticsClick
        )

        ScheduleSidebarItem(
            icon = Icons.Default.MenuBook,
            text = "Waste Guide",
            selected = false,
            onClick = onWasteGuideClick
        )
    }
}


// ============================================================
// SIDEBAR ITEM
// ============================================================

@Composable
private fun ScheduleSidebarItem(
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
            .background(
                color =
                    if (selected)
                        SelectedGreen
                    else
                        Color.Transparent,

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
// TABLE HEADER
// ============================================================

@Composable
private fun ScheduleTableHeader() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color(0xFFF3F9F4))
            .padding(horizontal = 12.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {

        ScheduleHeaderCell(
            text = "SCHEDULE ID",
            weight = 0.12f
        )

        ScheduleHeaderCell(
            text = "BARANGAY / AREA",
            weight = 0.22f
        )

        ScheduleHeaderCell(
            text = "COLLECTION DAYS",
            weight = 0.25f
        )

        ScheduleHeaderCell(
            text = "START TIME",
            weight = 0.16f
        )

        ScheduleHeaderCell(
            text = "COLLECTOR ID",
            weight = 0.13f
        )

        ScheduleHeaderCell(
            text = "ACTION",
            weight = 0.12f
        )
    }
}


// ============================================================
// HEADER CELL
// ============================================================

@Composable
private fun RowScope.ScheduleHeaderCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        color = EcoDarkGreen,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .weight(weight)
            .padding(end = 4.dp)
    )
}


// ============================================================
// SCHEDULE ROW
// ============================================================

@Composable
private fun ScheduleRow(
    schedule: CollectionSchedule,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 12.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = schedule.id,
            color = TextGreen,
            fontSize = 12.sp,
            modifier = Modifier.weight(0.12f)
        )

        Text(
            text = schedule.barangayArea,
            color = TextGreen,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(0.22f)
        )


        Row(
            modifier = Modifier.weight(0.25f),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {

            schedule.collectionDays.forEach { day ->

                DayMiniBadge(
                    text = day
                )
            }
        }


        Text(
            text = schedule.collectionStartTime,
            color = TextGreen,
            fontSize = 12.sp,
            modifier = Modifier.weight(0.16f)
        )


        Text(
            text = schedule.collectorId,
            color = TextGreen,
            fontSize = 12.sp,
            modifier = Modifier.weight(0.13f)
        )


        Row(
            modifier = Modifier.weight(0.12f),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {

            IconButton(
                onClick = onEdit,
                modifier = Modifier.size(28.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Schedule",
                    tint = EcoGreen,
                    modifier = Modifier.size(14.dp)
                )
            }


            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(28.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Schedule",
                    tint = DeleteRed,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}


// ============================================================
// DAY MINI BADGE
// ============================================================

@Composable
private fun DayMiniBadge(
    text: String
) {

    Box(
        modifier = Modifier
            .clip(
                RoundedCornerShape(6.dp)
            )
            .background(
                Color(0xFFE5F4E8)
            )
            .padding(
                horizontal = 5.dp,
                vertical = 3.dp
            )
    ) {

        Text(
            text = text,
            color = EcoGreen,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


// ============================================================
// ADD SCHEDULE POPUP
// ============================================================

@Composable
private fun AddSchedulePopup(
    onDismiss: () -> Unit,
    onCreate: (CollectionSchedule) -> Unit
) {

    var barangay by remember {
        mutableStateOf("Brgy. Area A")
    }

    var selectedDays by remember {
        mutableStateOf(setOf("Mon"))
    }

    var startTime by remember {
        mutableStateOf("08:00 AM")
    }

    var collectorId by remember {
        mutableStateOf("COL-001")
    }


    val days = listOf(
        "Mon",
        "Tue",
        "Wed",
        "Thu",
        "Fri",
        "Sat",
        "Sun"
    )

    val times = listOf(
        "07:00 AM",
        "07:30 AM",
        "08:00 AM",
        "08:30 AM",
        "09:00 AM",
        "09:30 AM"
    )


    Dialog(
        onDismissRequest = onDismiss
    ) {

        Surface(
            modifier = Modifier
                .width(475.dp)
                .height(515.dp),

            shape = RoundedCornerShape(18.dp),

            color = White,

            shadowElevation = 10.dp
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 15.dp
                    )
            ) {

                // =================================================
                // TOP ROW + X BUTTON
                // =================================================

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "SCHEDULE",
                            color = EcoGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = "Add New Schedule",
                            color = TextGreen,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(34.dp)
                    ) {

                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextGreen,
                            modifier = Modifier.size(21.dp)
                        )
                    }
                }


                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = "Create a new collection schedule.",
                    color = Color(0xFF6F9875),
                    fontSize = 12.sp
                )

                // =================================================
                // BARANGAY / AREA
                // =================================================

                ScheduleFieldLabel(
                    text = "Barangay / Area"
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                ScheduleValueBox(
                    value = barangay,
                    onClick = {}
                )


                Spacer(
                    modifier = Modifier.height(12.dp)
                )


                // =================================================
                // COLLECTION DAYS
                // =================================================

                ScheduleFieldLabel(
                    text = "Collection Days"
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    days.forEach { day ->

                        DaySelectionButton(
                            text = day,

                            selected = day in selectedDays,

                            onClick = {

                                selectedDays =
                                    if (day in selectedDays) {
                                        selectedDays - day
                                    } else {
                                        selectedDays + day
                                    }
                            }
                        )
                    }
                }


                Spacer(
                    modifier = Modifier.height(12.dp)
                )


                // =================================================
                // COLLECTION START TIME
                // =================================================

                ScheduleFieldLabel(
                    text = "Collection Start Time"
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                ScheduleDropdown(
                    value = startTime,

                    options = times,

                    onSelected = {
                        startTime = it
                    }
                )


                Spacer(
                    modifier = Modifier.height(12.dp)
                )


                // =================================================
                // COLLECTOR ID
                // QUICK-ADD ACTIVE COLLECTORS
                // =================================================

                ScheduleFieldLabel(
                    text = "Collector ID"
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                QuickAddCollectors(
                    selectedCollectorId = collectorId,

                    onCollectorSelected = {
                        collectorId = it
                    }
                )
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                // =================================================
                // BOTTOM BUTTONS
                // CANCEL + CREATE SCHEDULE
                // =================================================

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(9.dp)
                ) {

                    Button(
                        onClick = onDismiss,

                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = White,
                            contentColor = EcoGreen
                        ),

                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.dp,
                            color = EcoGreen
                        ),

                        shape = RoundedCornerShape(20.dp),

                        contentPadding = PaddingValues(0.dp)
                    ) {

                        Text(
                            text = "Cancel",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }


                    Button(
                        onClick = {

                            if (selectedDays.isNotEmpty()) {

                                val newSchedule =
                                    CollectionSchedule(
                                        id = generateScheduleId(),

                                        barangayArea = barangay,

                                        collectionDays =
                                            days.filter {
                                                it in selectedDays
                                            },

                                        collectionStartTime = startTime,

                                        collectorId = collectorId
                                    )

                                onCreate(newSchedule)
                            }
                        },

                        enabled = selectedDays.isNotEmpty(),

                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = EcoGreen,

                            disabledContainerColor =
                                Color(0xFFE3E3E3),

                            disabledContentColor =
                                Color(0xFFB4B4B4)
                        ),

                        shape = RoundedCornerShape(20.dp),

                        contentPadding = PaddingValues(0.dp)
                    ) {

                        Text(
                            text = "Create Schedule",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


// ============================================================
// EDIT SCHEDULE POPUP
// ============================================================

@Composable
private fun EditSchedulePopup(
    schedule: CollectionSchedule,
    onDismiss: () -> Unit,
    onSave: (CollectionSchedule) -> Unit
) {

    var barangay by remember {
        mutableStateOf(schedule.barangayArea)
    }

    var selectedDays by remember {
        mutableStateOf(schedule.collectionDays.toSet())
    }

    var startTime by remember {
        mutableStateOf(schedule.collectionStartTime)
    }

    var collectorId by remember {
        mutableStateOf(schedule.collectorId)
    }


    val days = listOf(
        "Mon",
        "Tue",
        "Wed",
        "Thu",
        "Fri",
        "Sat",
        "Sun"
    )

    val times = listOf(
        "07:00 AM",
        "07:30 AM",
        "08:00 AM",
        "08:30 AM",
        "09:00 AM",
        "09:30 AM"
    )


    Dialog(
        onDismissRequest = onDismiss
    ) {

        Surface(
            modifier = Modifier
                .width(475.dp)
                .height(515.dp),

            shape = RoundedCornerShape(18.dp),

            color = White,

            shadowElevation = 10.dp
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 15.dp
                    )
            ) {

                // =================================================
                // TOP ROW + X BUTTON
                // =================================================

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = schedule.id,
                            color = EcoGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = "Edit Schedule",
                            color = TextGreen,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(34.dp)
                    ) {

                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextGreen,
                            modifier = Modifier.size(21.dp)
                        )
                    }
                }


                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = "Update the collection schedule details.",
                    color = Color(0xFF6F9875),
                    fontSize = 12.sp
                )

                // =================================================
                // BARANGAY / AREA
                // =================================================

                ScheduleFieldLabel(
                    text = "Barangay / Area"
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                ScheduleValueBox(
                    value = barangay,
                    onClick = {}
                )


                Spacer(
                    modifier = Modifier.height(12.dp)
                )


                // =================================================
                // COLLECTION DAYS
                // =================================================

                ScheduleFieldLabel(
                    text = "Collection Days"
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    days.forEach { day ->

                        DaySelectionButton(
                            text = day,

                            selected = day in selectedDays,

                            onClick = {

                                selectedDays =
                                    if (day in selectedDays) {
                                        selectedDays - day
                                    } else {
                                        selectedDays + day
                                    }
                            }
                        )
                    }
                }


                Spacer(
                    modifier = Modifier.height(12.dp)
                )


                // =================================================
                // COLLECTION START TIME
                // =================================================

                ScheduleFieldLabel(
                    text = "Collection Start Time"
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                ScheduleDropdown(
                    value = startTime,

                    options = times,

                    onSelected = {
                        startTime = it
                    }
                )


                Spacer(
                    modifier = Modifier.height(12.dp)
                )


                // =================================================
                // COLLECTOR ID
                // QUICK-ADD ACTIVE COLLECTORS
                // =================================================

                ScheduleFieldLabel(
                    text = "Collector ID"
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                QuickAddCollectors(
                    selectedCollectorId = collectorId,

                    onCollectorSelected = {
                        collectorId = it
                    }
                )
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                // =================================================
                // BOTTOM BUTTONS
                // CANCEL + SAVE CHANGES
                // =================================================

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(9.dp)
                ) {

                    Button(
                        onClick = onDismiss,

                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = White,
                            contentColor = EcoGreen
                        ),

                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.dp,
                            color = EcoGreen
                        ),

                        shape = RoundedCornerShape(20.dp),

                        contentPadding = PaddingValues(0.dp)
                    ) {

                        Text(
                            text = "Cancel",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }


                    Button(
                        onClick = {

                            if (selectedDays.isNotEmpty()) {

                                val updated =
                                    schedule.copy(

                                        barangayArea = barangay,

                                        collectionDays =
                                            days.filter {
                                                it in selectedDays
                                            },

                                        collectionStartTime =
                                            startTime,

                                        collectorId =
                                            collectorId
                                    )

                                onSave(updated)
                            }
                        },

                        enabled = selectedDays.isNotEmpty(),

                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = EcoGreen,

                            disabledContainerColor =
                                Color(0xFFE3E3E3),

                            disabledContentColor =
                                Color(0xFFB4B4B4)
                        ),

                        shape = RoundedCornerShape(20.dp),

                        contentPadding = PaddingValues(0.dp)
                    ) {

                        Text(
                            text = "Save Changes",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


// ============================================================
// QUICK-ADD ACTIVE COLLECTORS
// ============================================================

@Composable
private fun QuickAddCollectors(
    selectedCollectorId: String,
    onCollectorSelected: (String) -> Unit
) {

    val collectors = listOf(
        "COL-001" to "Lito",
        "COL-002" to "Carlos",
        "COL-003" to "Ramon"
    )


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(15.dp)
            )
            .background(
                Color(0xFFF0FAF2)
            )
            .border(
                width = 1.dp,
                color = Color(0xFFCFEFD5),
                shape = RoundedCornerShape(15.dp)
            )
            .padding(
                horizontal = 11.dp,
                vertical = 9.dp
            )
    ) {

        Text(
            text = "Quick-add active collectors",
            color = EcoGreen,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(7.dp)
        )


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            collectors.forEach { (id, name) ->

                val isSelected =
                    selectedCollectorId == id


                Box(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(50.dp)
                        )
                        .background(
                            if (isSelected) {
                                Color(0xFF08A849)
                            } else {
                                White
                            }
                        )
                        .border(
                            width = 1.dp,

                            color =
                                if (isSelected) {
                                    Color(0xFF08A849)
                                } else {
                                    Color(0xFFA8DDB4)
                                },

                            shape = RoundedCornerShape(50.dp)
                        )
                        .clickable {
                            onCollectorSelected(id)
                        }
                        .padding(
                            horizontal = 9.dp,
                            vertical = 5.dp
                        ),

                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "$id · $name",

                        color =
                            if (isSelected) {
                                White
                            } else {
                                EcoGreen
                            },

                        fontSize = 9.sp,

                        fontWeight =
                            if (isSelected) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            }
                    )
                }
            }
        }
    }
}


// ============================================================
// DELETE SCHEDULE CONFIRMATION
// ============================================================

@Composable
private fun DeleteScheduleConfirmation(
    schedule: CollectionSchedule,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {

    AlertDialog(

        onDismissRequest = onDismiss,

        containerColor = White,

        shape = RoundedCornerShape(18.dp),

        title = {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = DeleteRed,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                Text(
                    text = "Delete Schedule",
                    color = TextGreen,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },

        text = {

            Column {

                Text(
                    text = "Are you sure you want to delete this schedule?",
                    color = Color(0xFF5D6F61),
                    fontSize = 11.sp
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = schedule.id,
                    color = EcoGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = schedule.barangayArea,
                    color = TextGreen,
                    fontSize = 10.sp
                )
            }
        },

        confirmButton = {

            Button(
                onClick = onConfirm,

                colors = ButtonDefaults.buttonColors(
                    containerColor = DeleteRed
                ),

                shape = RoundedCornerShape(20.dp),

                modifier = Modifier.height(36.dp),

                contentPadding = PaddingValues(
                    horizontal = 18.dp
                )
            ) {

                Text(
                    text = "Delete",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },

        dismissButton = {

            Button(
                onClick = onDismiss,

                colors = ButtonDefaults.buttonColors(
                    containerColor = White,
                    contentColor = EcoGreen
                ),

                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = EcoGreen
                ),

                shape = RoundedCornerShape(20.dp),

                modifier = Modifier.height(36.dp),

                contentPadding = PaddingValues(
                    horizontal = 18.dp
                )
            ) {

                Text(
                    text = "Cancel",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}


// ============================================================
// SCHEDULE FIELD LABEL
// ============================================================

@Composable
private fun ScheduleFieldLabel(
    text: String
) {

    Text(
        text = text,
        color = TextGreen,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    )
}


// ============================================================
// SCHEDULE VALUE BOX
// ============================================================

@Composable
private fun ScheduleValueBox(
    value: String,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .clip(
                RoundedCornerShape(8.dp)
            )
            .background(
                Color(0xFFF8FBF8)
            )
            .border(
                width = 1.dp,
                color = LightBorder,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 11.dp
            ),

        contentAlignment = Alignment.CenterStart
    ) {

        Text(
            text = value,
            color = TextGreen,
            fontSize = 10.sp
        )
    }
}


// ============================================================
// DAY SELECTION BUTTON
// ============================================================

@Composable
private fun RowScope.DaySelectionButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .weight(1f)
            .height(32.dp)
            .clip(
                RoundedCornerShape(7.dp)
            )
            .background(
                if (selected) {
                    EcoGreen
                } else {
                    Color(0xFFF8FBF8)
                }
            )
            .border(
                width = 1.dp,

                color =
                    if (selected) {
                        EcoGreen
                    } else {
                        LightBorder
                    },

                shape = RoundedCornerShape(7.dp)
            )
            .clickable {
                onClick()
            },

        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,

            color =
                if (selected) {
                    White
                } else {
                    TextGreen
                },

            fontSize = 9.sp,

            fontWeight = FontWeight.Bold
        )
    }
}


// ============================================================
// START TIME DROPDOWN
// ============================================================

@Composable
private fun ScheduleDropdown(
    value: String,
    options: List<String>,
    onSelected: (String) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }


    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .clip(
                    RoundedCornerShape(8.dp)
                )
                .background(
                    Color(0xFFF8FBF8)
                )
                .border(
                    width = 1.dp,
                    color = LightBorder,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable {
                    expanded = !expanded
                }
                .padding(
                    horizontal = 11.dp
                ),

            contentAlignment = Alignment.CenterStart
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = value,
                    color = TextGreen,
                    fontSize = 10.sp,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text =
                        if (expanded) {
                            "▲"
                        } else {
                            "▼"
                        },

                    color = EcoGreen,
                    fontSize = 8.sp
                )
            }
        }


        if (expanded) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(8.dp)
                    )
                    .background(White)
                    .border(
                        width = 1.dp,
                        color = LightBorder,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {

                options.forEach { option ->

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                            .clickable {

                                onSelected(option)

                                expanded = false
                            }
                            .padding(
                                horizontal = 11.dp
                            ),

                        contentAlignment = Alignment.CenterStart
                    ) {

                        Text(
                            text = option,
                            color = TextGreen,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}


// ============================================================
// GENERATE SCHEDULE ID
// ============================================================

private fun generateScheduleId(): String {

    return "SCH-${
        System.currentTimeMillis()
            .toString()
            .takeLast(3)
    }"
}