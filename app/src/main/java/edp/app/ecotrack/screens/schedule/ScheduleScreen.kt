
package edp.app.ecotrack.screens.schedule

import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import edp.app.ecotrack.network.ApiClient
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle as DateTextStyle
import java.util.Calendar
import java.util.Locale

// ============================================================
// COLORS
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
// MODEL
// Keep collectorId temporarily for existing ApiClient signatures.
// ============================================================

data class CollectionSchedule(
    val id: String,
    val barangayArea: String,
    val collectionDays: List<String>,
    val collectionStartTime: String,
    val collectorId: String = "",
    val groupId: String = ""
)

// ============================================================
// MAIN SCHEDULE SCREEN
// ============================================================

@Composable
fun ScheduleScreen(
    accessToken: String,
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
    val scope = rememberCoroutineScope()

    var schedules by remember {
        mutableStateOf<List<CollectionSchedule>>(emptyList())
    }
    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }
    var scheduleMessage by remember { mutableStateOf("") }

    var showAddSchedule by remember { mutableStateOf(false) }
    var scheduleToEdit by remember {
        mutableStateOf<CollectionSchedule?>(null)
    }
    var scheduleToDelete by remember {
        mutableStateOf<CollectionSchedule?>(null)
    }

    suspend fun loadSchedules() {
        if (accessToken.isBlank()) {
            scheduleMessage = "Please log in again to load schedules."
            isLoading = false
            return
        }

        isLoading = true

        try {
            val response = ApiClient.getSchedules(accessToken)
            val array = response.optJSONArray("schedules")
                ?: response.optJSONArray("data")
                ?: JSONArray()

            val loaded = mutableListOf<CollectionSchedule>()

            for (index in 0 until array.length()) {
                val item = array.optJSONObject(index) ?: continue

                val groupId = item.optString("schedule_group_id", "")
                    .takeIf { it.isNotBlank() }
                    ?: item.optString("group_id", "")
                        .takeIf { it.isNotBlank() }
                    ?: item.optString("id", "")

                val zone = item.optString(
                    "barangay_name",
                    item.optString(
                        "barangay",
                        item.optString("barangay_area", "")
                    )
                )

                val rawDays = item.optJSONArray("collection_days")
                val days = if (rawDays != null) {
                    (0 until rawDays.length())
                        .map { rawDays.optString(it) }
                        .filter { it.isNotBlank() }
                } else {
                    item.optString("collection_days", "")
                        .split(",")
                        .map { it.trim() }
                        .filter { it.isNotBlank() }
                }

                val time = item.optString(
                    "collection_time",
                    item.optString("collection_start_time", "08:00")
                )

                loaded.add(
                    CollectionSchedule(
                        id = "SCH-${(index + 1).toString().padStart(3, '0')}",
                        barangayArea = zone,
                        collectionDays = days,
                        collectionStartTime = time.toDisplayTime(),
                        collectorId = item.optString("collector_id", ""),
                        groupId = groupId
                    )
                )
            }

            schedules = loaded
            scheduleMessage = ""
        } catch (e: Exception) {
            scheduleMessage = e.message ?: "Unable to load schedules."
        } finally {
            isLoading = false
        }
    }

    LaunchedEffect(accessToken) {
        loadSchedules()
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoLightGreen)
    ) {
        ScheduleSidebar(
            onDashboardClick = onDashboardClick,
            onWasteReportClick = onWasteReportClick,
            onCollectorsClick = onCollectorsClick,
            onResidentsClick = onResidentsClick,
            onAnalyticsClick = onAnalyticsClick,
            onWasteGuideClick = onWasteGuideClick
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp)
                    .background(EcoTopBar)
                    .padding(horizontal = 25.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Spacer(Modifier.width(15.dp))

                Text(
                    "Schedules",
                    color = EcoDarkGreen,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.weight(1f))

                Box(contentAlignment = Alignment.TopEnd) {
                    IconButton(onClick = onNotificationClick) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.Black,
                            modifier = Modifier.size(23.dp)
                        )
                    }

                    if (hasUnreadNotifications) {
                        Box(
                            Modifier
                                .padding(top = 8.dp, end = 8.dp)
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(EcoGreen)
                        )
                    }
                }

                IconButton(onClick = onProfileClick) {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = "My Profile",
                        tint = EcoDarkGreen,
                        modifier = Modifier.size(31.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp, vertical = 18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Collection Schedules",
                        color = TextGreen,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(Modifier.weight(1f))

                    Button(
                        onClick = { showAddSchedule = true },
                        enabled = !isSaving,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EcoGreen
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Add Schedule", fontSize = 10.sp)
                    }
                }

                Spacer(Modifier.height(10.dp))

                if (scheduleMessage.isNotBlank()) {
                    Text(
                        scheduleMessage,
                        color = DeleteRed,
                        fontSize = 12.sp
                    )
                    Spacer(Modifier.height(8.dp))
                }

                if (isLoading || isSaving) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = EcoGreen,
                            strokeWidth = 2.dp
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            if (isLoading) "Loading schedules..." else "Saving schedule...",
                            color = TextGreen,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = White,
                    shadowElevation = 4.dp
                ) {
                    Column(Modifier.fillMaxWidth()) {
                        ScheduleTableHeader()

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                        ) {
                            items(schedules, key = { it.id }) { schedule ->
                                ScheduleRow(
                                    schedule = schedule,
                                    onEdit = { scheduleToEdit = schedule },
                                    onDelete = { scheduleToDelete = schedule }
                                )
                            }
                        }
                    }
                }

                if (!isLoading && schedules.isEmpty() &&
                    scheduleMessage.isBlank()
                ) {
                    Text(
                        "No schedules found.",
                        color = TextGreen,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    if (showAddSchedule) {
        AddSchedulePopup(
            onDismiss = {
                if (!isSaving) showAddSchedule = false
            },
            onCreate = { newSchedule ->
                scope.launch {
                    isSaving = true
                    scheduleMessage = ""

                    try {
                        ApiClient.addScheduleGroup(
                            accessToken = accessToken,
                            barangayName = newSchedule.barangayArea,
                            collectionDays = newSchedule.collectionDays,
                            collectionTime = newSchedule.collectionStartTime,
                            collectorId = ""
                        )

                        loadSchedules()
                        showAddSchedule = false
                    } catch (e: Exception) {
                        scheduleMessage =
                            e.message ?: "Unable to create schedule."
                    } finally {
                        isSaving = false
                    }
                }
            }
        )
    }

    scheduleToEdit?.let { schedule ->
        EditSchedulePopup(
            schedule = schedule,
            onDismiss = {
                if (!isSaving) scheduleToEdit = null
            },
            onSave = { updated ->
                scope.launch {
                    if (schedule.groupId.isBlank()) {
                        scheduleMessage =
                            "Missing schedule group ID. Reload the schedules first."
                        return@launch
                    }

                    isSaving = true
                    scheduleMessage = ""

                    try {
                        ApiClient.updateScheduleGroup(
                            accessToken = accessToken,
                            scheduleGroupId = schedule.groupId,
                            barangayName = updated.barangayArea,
                            collectionDays = updated.collectionDays,
                            collectionTime = updated.collectionStartTime,
                            collectorId = ""
                        )

                        loadSchedules()
                        scheduleToEdit = null
                    } catch (e: Exception) {
                        scheduleMessage =
                            e.message ?: "Unable to update schedule."
                    } finally {
                        isSaving = false
                    }
                }
            }
        )
    }

    scheduleToDelete?.let { schedule ->
        DeleteScheduleConfirmation(
            schedule = schedule,
            onDismiss = {
                if (!isSaving) scheduleToDelete = null
            },
            onConfirm = {
                scope.launch {
                    if (schedule.groupId.isBlank()) {
                        scheduleMessage =
                            "Missing schedule group ID. Reload the schedules first."
                        scheduleToDelete = null
                        return@launch
                    }

                    isSaving = true
                    scheduleMessage = ""

                    try {
                        ApiClient.deleteScheduleGroup(
                            accessToken = accessToken,
                            scheduleGroupId = schedule.groupId
                        )

                        loadSchedules()
                        scheduleToDelete = null
                    } catch (e: Exception) {
                        scheduleMessage =
                            e.message ?: "Unable to delete schedule."
                    } finally {
                        isSaving = false
                    }
                }
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
                "♧EcoTrack",
                color = White,
                fontSize = 19.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFF4A8A50))
                .padding(horizontal = 18.dp, vertical = 12.dp)
        ) {
            Text("ADMINISTRATOR", color = Color(0xFF9FD6A5), fontSize = 11.sp)
            Spacer(Modifier.height(6.dp))
            Text(
                "System Admin",
                color = White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                "admin@ecotrack.gov.ph",
                color = Color(0xFFB9DDBD),
                fontSize = 10.sp
            )
        }

        Spacer(Modifier.height(20.dp))
        ScheduleSidebarItem(Icons.Default.GridView, "Dashboard", false, onDashboardClick)
        ScheduleSidebarItem(Icons.Default.Description, "Waste Report", false, onWasteReportClick)
        ScheduleSidebarItem(Icons.Default.LocalShipping, "Collectors", false, onCollectorsClick)
        ScheduleSidebarItem(Icons.Default.Groups, "Residents", false, onResidentsClick)
        ScheduleSidebarItem(Icons.Default.CalendarMonth, "Schedules", true, {})
        ScheduleSidebarItem(Icons.Default.BarChart, "Analytics", false, onAnalyticsClick)
        ScheduleSidebarItem(Icons.Default.MenuBook, "Waste Guide", false, onWasteGuideClick)
    }
}

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
            .padding(horizontal = 15.dp, vertical = 4.dp)
            .height(42.dp)
            .background(
                if (selected) SelectedGreen else Color.Transparent,
                RoundedCornerShape(22.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = text, tint = White, modifier = Modifier.size(19.dp))
        Spacer(Modifier.width(14.dp))
        Text(text, color = White, fontSize = 13.sp)
    }
}

// ============================================================
// TABLE - COLLECTOR ID REMOVED
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
        ScheduleHeaderCell("SCHEDULE ID", 0.15f)
        ScheduleHeaderCell("ZONE", 0.20f)
        ScheduleHeaderCell("COLLECTION DAYS", 0.29f)
        ScheduleHeaderCell("START TIME", 0.18f)
        ScheduleHeaderCell("ACTION", 0.18f)
    }
}

@Composable
private fun RowScope.ScheduleHeaderCell(text: String, weight: Float) {
    Text(
        text = text,
        color = EcoDarkGreen,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.weight(weight).padding(end = 4.dp)
    )
}

@Composable
private fun ScheduleRow(
    schedule: CollectionSchedule,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showMoreDates by remember(schedule.id) { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            schedule.id,
            color = TextGreen,
            fontSize = 12.sp,
            modifier = Modifier.weight(0.15f)
        )

        Text(
            schedule.barangayArea,
            color = TextGreen,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(0.20f)
        )

        Row(
            modifier = Modifier.weight(0.29f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            schedule.collectionDays.take(4).forEach {
                DayMiniBadge(it.toCalendarBadge())
            }

            val remaining = schedule.collectionDays.drop(4)
            if (remaining.isNotEmpty()) {
                Box {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFE5F4E8))
                            .clickable { showMoreDates = true }
                            .padding(horizontal = 6.dp, vertical = 4.dp)
                    ) {
                        Text(
                            "+${remaining.size}",
                            color = EcoGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    DropdownMenu(
                        expanded = showMoreDates,
                        onDismissRequest = { showMoreDates = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Collection Dates",
                                    fontWeight = FontWeight.Bold,
                                    color = EcoDarkGreen
                                )
                            },
                            onClick = { showMoreDates = false }
                        )

                        remaining.forEach { date ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        date.toCalendarBadge(),
                                        color = TextGreen,
                                        fontSize = 13.sp
                                    )
                                },
                                onClick = { showMoreDates = false }
                            )
                        }
                    }
                }
            }
        }

        Text(
            schedule.collectionStartTime,
            color = TextGreen,
            fontSize = 12.sp,
            modifier = Modifier.weight(0.18f)
        )

        Row(
            modifier = Modifier.weight(0.18f),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            IconButton(onClick = onEdit, modifier = Modifier.size(28.dp)) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit Schedule",
                    tint = EcoGreen,
                    modifier = Modifier.size(14.dp)
                )
            }

            IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete Schedule",
                    tint = DeleteRed,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
private fun DayMiniBadge(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFFE5F4E8))
            .padding(horizontal = 5.dp, vertical = 3.dp)
    ) {
        Text(
            text,
            color = EcoGreen,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// ============================================================
// CALENDAR - MULTI-DATE SELECTION + CLEAR MONTH
// ============================================================

@Composable
private fun CompactCollectionCalendar(
    selectedDates: Set<String>,
    onDatesChange: (Set<String>) -> Unit
) {
    val firstSelectedDate = selectedDates
        .mapNotNull { runCatching { LocalDate.parse(it) }.getOrNull() }
        .minOrNull()

    var displayedMonth by remember {
        mutableStateOf(firstSelectedDate?.let { YearMonth.from(it) } ?: YearMonth.now())
    }

    LaunchedEffect(selectedDates) {
        if (firstSelectedDate != null) {
            displayedMonth = YearMonth.from(firstSelectedDate)
        }
    }

    val weekdays = listOf("S", "M", "T", "W", "T", "F", "S")
    val startOffset = displayedMonth.atDay(1).dayOfWeek.value % 7
    val daysInMonth = displayedMonth.lengthOfMonth()
    val totalCells = ((startOffset + daysInMonth + 6) / 7) * 7

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF8FBF8))
            .border(1.dp, LightBorder, RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                displayedMonth.month.getDisplayName(DateTextStyle.FULL, Locale.ENGLISH) +
                        " ${displayedMonth.year}",
                color = EcoDarkGreen,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = { displayedMonth = displayedMonth.minusMonths(1) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(Icons.Default.ChevronLeft, "Previous month", tint = EcoGreen)
            }

            IconButton(
                onClick = { displayedMonth = displayedMonth.plusMonths(1) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(Icons.Default.ChevronRight, "Next month", tint = EcoGreen)
            }
        }

        Row(Modifier.fillMaxWidth()) {
            weekdays.forEachIndexed { index, day ->
                Box(
                    modifier = Modifier.weight(1f).height(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        day,
                        color = if (index == 0 || index == 6) EcoGreen else Color.Gray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        for (week in 0 until totalCells / 7) {
            Row(Modifier.fillMaxWidth()) {
                for (column in 0..6) {
                    val cellIndex = week * 7 + column
                    val dayNumber = cellIndex - startOffset + 1

                    Box(
                        modifier = Modifier.weight(1f).height(34.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (dayNumber in 1..daysInMonth) {
                            val dateKey = displayedMonth.atDay(dayNumber).toString()
                            val isSelected = dateKey in selectedDates

                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) EcoGreen else Color.Transparent
                                    )
                                    .clickable {
                                        val updated = selectedDates.toMutableSet()
                                        if (isSelected) {
                                            updated.remove(dateKey)
                                        } else {
                                            updated.add(dateKey)
                                        }
                                        onDatesChange(updated)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    dayNumber.toString(),
                                    color = if (isSelected) White else Color(0xFF263238),
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) {
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
        }

        Spacer(Modifier.height(7.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "${selectedDates.size} dates selected",
                color = Color(0xFF455A64),
                fontSize = 12.sp,
                modifier = Modifier.weight(1f)
            )

            TextButton(
                onClick = {
                    onDatesChange(
                        selectedDates.filterNot { value ->
                            runCatching {
                                YearMonth.from(LocalDate.parse(value)) == displayedMonth
                            }.getOrDefault(false)
                        }.toSet()
                    )
                },
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
            ) {
                Text("Clear Month", color = EcoGreen, fontSize = 12.sp)
            }
        }
    }
}

// ============================================================
// ADD SCHEDULE POPUP - NO COLLECTOR SELECTION
// ============================================================

@Composable
private fun AddSchedulePopup(
    onDismiss: () -> Unit,
    onCreate: (CollectionSchedule) -> Unit
) {
    var zone by remember { mutableStateOf("") }
    var selectedDates by remember { mutableStateOf<Set<String>>(emptySet()) }
    var startTime by remember { mutableStateOf("08:00 AM") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.width(475.dp).heightIn(max = 720.dp),
            shape = RoundedCornerShape(18.dp),
            color = White,
            shadowElevation = 10.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 720.dp)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                PopupTitle(
                    title = "Add New Schedule",
                    subtitle = "Create a new collection schedule.",
                    onDismiss = onDismiss
                )

                Spacer(Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState())
                ) {
                    ScheduleFieldLabel("Zone")
                    Spacer(Modifier.height(6.dp))
                    ZoneDropdown(zone) { zone = it }

                    Spacer(Modifier.height(10.dp))
                    ScheduleFieldLabel("Collection Days")
                    Spacer(Modifier.height(6.dp))
                    CompactCollectionCalendar(
                        selectedDates = selectedDates,
                        onDatesChange = { selectedDates = it }
                    )

                    Spacer(Modifier.height(10.dp))
                    ScheduleFieldLabel("Collection Start Time")
                    Spacer(Modifier.height(6.dp))
                    ScheduleTimePicker(startTime) { startTime = it }
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    FormButton(
                        text = "Cancel",
                        outlined = true,
                        modifier = Modifier.weight(1f)
                    ) { onDismiss() }

                    FormButton(
                        text = "Create Schedule",
                        modifier = Modifier.weight(1f),
                        enabled = zone.isNotBlank() && selectedDates.isNotEmpty()
                    ) {
                        onCreate(
                            CollectionSchedule(
                                id = generateScheduleId(),
                                barangayArea = zone,
                                collectionDays = selectedDates.sorted(),
                                collectionStartTime = startTime,
                                collectorId = ""
                            )
                        )
                    }
                }
            }
        }
    }
}

// ============================================================
// EDIT SCHEDULE POPUP - NO COLLECTOR SELECTION
// ============================================================

@Composable
private fun EditSchedulePopup(
    schedule: CollectionSchedule,
    onDismiss: () -> Unit,
    onSave: (CollectionSchedule) -> Unit
) {
    var zone by remember(schedule.id) {
        mutableStateOf(schedule.barangayArea)
    }

    var selectedDates by remember(schedule.groupId) {
        mutableStateOf(
            schedule.collectionDays
                .map { it.trim() }
                .filter { it.isIsoDate() }
                .toSet()
        )
    }

    var startTime by remember(schedule.id) {
        mutableStateOf(schedule.collectionStartTime)
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.width(475.dp).heightIn(max = 720.dp),
            shape = RoundedCornerShape(18.dp),
            color = White,
            shadowElevation = 10.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 720.dp)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                PopupTitle(
                    title = "Edit Schedule",
                    subtitle = "Update the collection schedule details.",
                    onDismiss = onDismiss,
                    label = schedule.id
                )

                Spacer(Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState())
                ) {
                    ScheduleFieldLabel("Zone")
                    Spacer(Modifier.height(6.dp))
                    ZoneDropdown(zone) { zone = it }

                    Spacer(Modifier.height(10.dp))
                    ScheduleFieldLabel("Collection Days")
                    Spacer(Modifier.height(6.dp))
                    CompactCollectionCalendar(
                        selectedDates = selectedDates,
                        onDatesChange = { selectedDates = it }
                    )

                    Spacer(Modifier.height(10.dp))
                    ScheduleFieldLabel("Collection Start Time")
                    Spacer(Modifier.height(6.dp))
                    ScheduleTimePicker(startTime) { startTime = it }
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    FormButton(
                        text = "Cancel",
                        outlined = true,
                        modifier = Modifier.weight(1f)
                    ) { onDismiss() }

                    FormButton(
                        text = "Save Changes",
                        modifier = Modifier.weight(1f),
                        enabled = zone.isNotBlank() && selectedDates.isNotEmpty()
                    ) {
                        onSave(
                            schedule.copy(
                                barangayArea = zone.trim(),
                                collectionDays = selectedDates.sorted(),
                                collectionStartTime = startTime
                            )
                        )
                    }
                }
            }
        }
    }
}

// ============================================================
// SHARED POPUP HEADER AND BUTTONS
// ============================================================

@Composable
private fun PopupTitle(
    title: String,
    subtitle: String,
    onDismiss: () -> Unit,
    label: String = "SCHEDULE"
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(label, color = EcoGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(5.dp))
            Text(title, color = TextGreen, fontSize = 21.sp, fontWeight = FontWeight.Bold)
        }

        IconButton(onClick = onDismiss, modifier = Modifier.size(34.dp)) {
            Icon(Icons.Default.Close, "Close", tint = TextGreen)
        }
    }

    Spacer(Modifier.height(4.dp))
    Text(subtitle, color = Color(0xFF6F9875), fontSize = 12.sp)
}

@Composable
private fun FormButton(
    text: String,
    modifier: Modifier = Modifier,
    outlined: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(38.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (outlined) White else EcoGreen,
            contentColor = if (outlined) EcoGreen else White,
            disabledContainerColor = Color(0xFFE3E3E3),
            disabledContentColor = Color(0xFF888888)
        ),
        border = if (outlined) BorderStroke(1.dp, EcoGreen) else null,
        shape = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

// ============================================================
// ZONE DROPDOWN: ZONE 1 TO ZONE 10
// ============================================================

@Composable
private fun ZoneDropdown(
    selectedZone: String,
    onZoneSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val zones = (1..10).map { "Zone $it" }

    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF8FBF8))
                .border(1.dp, LightBorder, RoundedCornerShape(8.dp))
                .clickable { expanded = true }
                .padding(horizontal = 11.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                selectedZone.ifBlank { "Select Zone" },
                color = if (selectedZone.isBlank()) Color.Gray else TextGreen,
                fontSize = 10.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "Select Zone",
                tint = EcoGreen,
                modifier = Modifier.size(18.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            zones.forEach { zone ->
                DropdownMenuItem(
                    text = { Text(zone, color = TextGreen) },
                    onClick = {
                        onZoneSelected(zone)
                        expanded = false
                    }
                )
            }
        }
    }
}

// ============================================================
// DELETE CONFIRMATION
// ============================================================

@Composable
private fun DeleteScheduleConfirmation(
    schedule: CollectionSchedule,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        modifier = Modifier.width(480.dp),
        onDismissRequest = onDismiss,
        containerColor = White,
        shape = RoundedCornerShape(18.dp),
        title = {
            Column {
                Text(
                    "${schedule.id} - ${schedule.barangayArea}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeleteRed
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    "Delete Schedule",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextGreen
                )
            }
        },
        text = {
            Text(
                "Are you sure you want to delete this schedule?",
                color = Color(0xFF5D6F61),
                fontSize = 12.sp
            )
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = White,
                        contentColor = EcoGreen
                    ),
                    border = BorderStroke(1.dp, EcoGreen),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Cancel", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.width(8.dp))

                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(containerColor = DeleteRed),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Delete", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {}
    )
}

// ============================================================
// FORM FIELDS
// ============================================================

@Composable
private fun ScheduleFieldLabel(text: String) {
    Text(
        text,
        color = TextGreen,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    )
}

@Composable
private fun ScheduleValueBox(
    value: String,
    onValueChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF8FBF8))
            .border(1.dp, LightBorder, RoundedCornerShape(8.dp))
            .padding(horizontal = 11.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(color = TextGreen, fontSize = 10.sp),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text("Enter zone", color = Color.Gray, fontSize = 10.sp)
                    }
                    innerTextField()
                }
            }
        )
    }
}

// ============================================================
// TIME PICKER
// ============================================================

@Composable
private fun ScheduleTimePicker(
    value: String,
    onTimeSelected: (String) -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF8FBF8))
            .border(1.dp, LightBorder, RoundedCornerShape(8.dp))
            .clickable {
                val calendar = Calendar.getInstance()

                val parsed = runCatching {
                    SimpleDateFormat("hh:mm a", Locale.US).parse(value)
                }.getOrNull()

                if (parsed != null) calendar.time = parsed

                TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        val formatted = String.format(
                            Locale.US,
                            "%02d:%02d %s",
                            if (hourOfDay % 12 == 0) 12 else hourOfDay % 12,
                            minute,
                            if (hourOfDay < 12) "AM" else "PM"
                        )
                        onTimeSelected(formatted)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
                ).show()
            }
            .padding(horizontal = 11.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                value.ifBlank { "Select collection time" },
                color = if (value.isBlank()) Color.Gray else TextGreen,
                fontSize = 10.sp,
                modifier = Modifier.weight(1f)
            )
            Text("🕒", fontSize = 12.sp)
        }
    }
}

// ============================================================
// HELPERS
// ============================================================

private fun generateScheduleId(): String {
    return "SCH-${System.currentTimeMillis().toString().takeLast(3)}"
}

private fun String.isIsoDate(): Boolean {
    return runCatching {
        LocalDate.parse(trim())
        true
    }.getOrDefault(false)
}

private fun String.toCalendarBadge(): String {
    return runCatching {
        val date = LocalDate.parse(trim())
        "${date.month.getDisplayName(DateTextStyle.SHORT, Locale.ENGLISH)} ${date.dayOfMonth}"
    }.getOrDefault(trim())
}

private fun String.toDisplayTime(): String {
    val formats = listOf(
        "hh:mm a",
        "h:mm a",
        "HH:mm:ss",
        "HH:mm",
        "hh:mm:ss a"
    )

    for (format in formats) {
        val parsed = runCatching {
            SimpleDateFormat(format, Locale.US).parse(trim())
        }.getOrNull()

        if (parsed != null) {
            return SimpleDateFormat("hh:mm a", Locale.US).format(parsed)
        }
    }

    return this
}
