
package edp.app.ecotrack.screens.collectors

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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.IconButton
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.rememberLauncherForActivityResult
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.core.content.ContextCompat
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import java.io.File
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import edp.app.ecotrack.network.ApiClient
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val EcoDarkGreen = Color(0xFF216B2A)
private val EcoGreen = Color(0xFF2E7D32)
private val EcoLightGreen = Color(0xFFE6F5EA)
private val EcoTopBar = Color(0xFFE0F2E5)
private val SelectedGreen = Color(0xFF719873)
private val TextGreen = Color(0xFF286B32)
private val LightBorder = Color(0xFFE1E8E2)
private val ActiveGreen = Color(0xFFCFF7C7)
private val ActiveText = Color(0xFF4C9143)
private val Red = Color(0xFFD6323C)
private val White = Color.White

data class Collector(
    val collectorId: String,
    val fullName: String,
    val assignedArea: String,
    val phoneNumber: String,
    val tasks: Int,
    val completed: Int,
    val status: String,
    val profileImage: String? = null
)
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

/* Converts the backend JSON response into Android data. */
private fun parseCollectorTaskHistory(
    response: JSONObject
): CollectorTaskHistory {
    val summary = response.optJSONObject("summary")
    val records = response.optJSONArray("reports") ?: JSONArray()

    val reports = (0 until records.length()).mapNotNull { index ->
        val item = records.optJSONObject(index) ?: return@mapNotNull null

        val rawId = item.optString("report_id", "")
        val rawDate = item.optString("date", "")
            .takeUnless { it.isBlank() || it == "null" }

        val displayDate = try {
            if (rawDate == null) {
                "Date unavailable"
            } else {
                val datePart = rawDate.take(10)
                val parsed = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.US
                ).parse(datePart)

                if (parsed != null) {
                    SimpleDateFormat(
                        "MMM dd, yyyy",
                        Locale.getDefault()
                    ).format(parsed)
                } else {
                    datePart
                }
            }
        } catch (_: Exception) {
            rawDate?.take(10) ?: "Date unavailable"
        }

        TaskHistoryReport(
            reportId = if (rawId.isBlank() || rawId == "null") {
                "Report unavailable"
            } else {
                "#$rawId"
            },
            status = item.optString("status", "Assigned"),
            issue = item.optString("issue", "Waste Report"),
            location = item.optString(
                "location",
                "Location unavailable"
            ),
            date = displayDate
        )
    }

    return CollectorTaskHistory(
        totalTasks = summary?.optInt("total_tasks", reports.size)
            ?: reports.size,
        completedTasks = summary?.optInt("completed_tasks", 0) ?: 0,
        inProgressTasks = summary?.optInt("in_progress_tasks", 0) ?: 0,
        reports = reports
    )
}

private fun generateNextCollectorId(
    collectors: List<Collector>
): String {
    val highest = collectors.mapNotNull {
        it.collectorId.removePrefix("COL-").toIntOrNull()
    }.maxOrNull() ?: 0

    return "COL-${(highest + 1).toString().padStart(3, '0')}"
}

@Composable
fun CollectorsScreen(
    accessToken: String,
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
    val collectors = remember { mutableStateListOf<Collector>() }
    val scope = rememberCoroutineScope()

    var isLoadingCollectors by remember { mutableStateOf(true) }
    var collectorsError by remember { mutableStateOf("") }
    var isAddingCollector by remember { mutableStateOf(false) }
    var addCollectorError by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var editingCollector by remember { mutableStateOf<Collector?>(null) }
    var deletingCollector by remember { mutableStateOf<Collector?>(null) }
    var viewingTaskHistory by remember { mutableStateOf<Collector?>(null) }

    /*
     * Read collectors and their task summaries from the backend.
     * The backend must return the real user_id for each collector.
     */
    suspend fun reloadCollectors() {
        val response = ApiClient.getCollectors(accessToken)
        val records = response.optJSONArray("collectors") ?: JSONArray()

        val loaded = (0 until records.length()).mapNotNull { index ->
            val item = records.optJSONObject(index) ?: return@mapNotNull null
            val userId = item.optInt("user_id", 0)

            if (userId <= 0) return@mapNotNull null

            val barangay = item.optJSONObject("barangays")
            val barangayName = barangay?.optString("barangay_name")
                ?.takeIf { !it.isNullOrBlank() && it != "null" }
                ?: "Not assigned"

            val phone = item.optString("contact_number")
                .takeIf { it.isNotBlank() && it != "null" }
                ?: "Not provided"

            // Do not use sample task counts.
            // If the history request fails, the table temporarily shows zero.
            val taskHistory = try {
                parseCollectorTaskHistory(
                    ApiClient.getCollectorTaskHistory(
                        accessToken = accessToken,
                        collectorId = userId.toString()
                    )
                )
            } catch (_: Exception) {
                null
            }

            Collector(
                collectorId = "COL-${userId.toString().padStart(3, '0')}",
                fullName = item.optString("full_name", "Unknown"),
                assignedArea = barangayName,
                phoneNumber = phone,
                tasks = taskHistory?.totalTasks ?: 0,
                completed = taskHistory?.completedTasks ?: 0,
                status = if (item.optBoolean("is_active", false)) {
                    "Active"
                } else {
                    "Inactive"
                },
                profileImage = item.optString("profile_image", "")
                    .takeIf { it.isNotBlank() && it != "null" }
            )
        }

        collectors.clear()
        collectors.addAll(loaded)
    }

    LaunchedEffect(accessToken) {
        if (accessToken.isBlank()) {
            collectorsError = "Please log in again to load collectors."
            isLoadingCollectors = false
            return@LaunchedEffect
        }

        isLoadingCollectors = true
        collectorsError = ""

        try {
            reloadCollectors()
        } catch (e: Exception) {
            collectorsError = e.message ?: "Unable to load collectors."
        } finally {
            isLoadingCollectors = false
        }
    }

    Row(
        Modifier
            .fillMaxSize()
            .background(EcoLightGreen)
    ) {
        // SIDEBAR
        Column(
            Modifier
                .width(250.dp)
                .fillMaxHeight()
                .background(EcoDarkGreen)
                .padding(bottom = 20.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(62.dp)
                    .padding(horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("♧EcoTrack", color = White, fontSize = 19.sp)
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF4A8A50))
                    .padding(horizontal = 18.dp, vertical = 12.dp)
            ) {
                Text(
                    "ADMINISTRATOR",
                    color = Color(0xFF9FD6A5),
                    fontSize = 11.sp
                )
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
            SidebarItem(Icons.Default.GridView, "Dashboard", onClick = onDashboardClick)
            SidebarItem(Icons.Default.Description, "Waste Report", onClick = onWasteReportClick)
            SidebarItem(Icons.Default.LocalShipping, "Collectors", selected = true, onClick = onCollectorsClick)
            SidebarItem(Icons.Default.Groups, "Residents", onClick = onResidentsClick)
            SidebarItem(Icons.Default.CalendarMonth, "Schedules", onClick = onScheduleClick)
            SidebarItem(Icons.Default.BarChart, "Analytics", onClick = onAnalyticsClick)
            SidebarItem(Icons.Default.BarChart, "Waste Guide", onClick = onWasteGuideClick)
        }

        // MAIN CONTENT
        Column(
            Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(62.dp)
                    .background(EcoTopBar)
                    .padding(horizontal = 25.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(15.dp))
                Text("Collectors", color = EcoDarkGreen, fontSize = 18.sp)
                Spacer(Modifier.weight(1f))

                IconButton(onClick = onNotificationClick) {
                    Icon(
                        Icons.Default.Notifications,
                        "Notifications",
                        tint = Color.Black
                    )
                }

                IconButton(onClick = onProfileClick) {
                    Icon(
                        Icons.Default.AccountCircle,
                        "Profile",
                        tint = EcoDarkGreen,
                        modifier = Modifier.size(31.dp)
                    )
                }
            }

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp, vertical = 18.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Garbage Collector Management",
                        color = TextGreen,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(Modifier.weight(1f))

                    Button(
                        onClick = {
                            addCollectorError = ""
                            showAddDialog = true
                        },
                        modifier = Modifier.height(35.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EcoGreen
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            "Add",
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Add Collector", fontSize = 10.sp)
                    }
                }

                Spacer(Modifier.height(24.dp))

                if (isLoadingCollectors) {
                    Text(
                        "Loading collectors...",
                        color = TextGreen,
                        fontSize = 12.sp
                    )
                }

                if (collectorsError.isNotBlank()) {
                    Text(
                        collectorsError,
                        color = Red,
                        fontSize = 12.sp
                    )
                }

                Spacer(Modifier.height(8.dp))

                Surface(
                    Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(10.dp)),
                    shape = RoundedCornerShape(10.dp),
                    color = White
                ) {
                    Column {
                        CollectorTableHeader()

                        LazyColumn(
                            Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) {
                            items(collectors, key = { it.collectorId }) { collector ->
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

    // ADD COLLECTOR
    if (showAddDialog) {
        AddCollectorDialog(
            nextCollectorId = generateNextCollectorId(collectors),
            isSaving = isAddingCollector,
            errorMessage = addCollectorError,
            onDismiss = {
                if (!isAddingCollector) {
                    showAddDialog = false
                    addCollectorError = ""
                }
            },
            onAdd = { fullName, email, password, phone, assignedArea, status ->
                scope.launch {
                    isAddingCollector = true
                    addCollectorError = ""

                    try {
                        if (accessToken.isBlank()) {
                            throw IllegalStateException(
                                "Please log in again before adding a collector."
                            )
                        }

                        val barangayId = when {
                            assignedArea.equals(
                                "Barangay Bulua",
                                ignoreCase = true
                            ) -> 1

                            else -> throw IllegalArgumentException(
                                "Only Barangay Bulua (ID 1) is currently configured. " +
                                        "Please configure other barangays in Supabase first."
                            )
                        }

                        ApiClient.addCollector(
                            accessToken = accessToken,
                            name = fullName,
                            email = email,
                            password = password,
                            phone = phone,
                            barangayId = barangayId
                        )

                        reloadCollectors()
                        showAddDialog = false
                        addCollectorError = ""

                        collectorsError = if (status == "Inactive") {
                            "Collector created. The backend currently creates accounts as Active."
                        } else {
                            ""
                        }
                    } catch (e: Exception) {
                        addCollectorError =
                            e.message ?: "Unable to add collector."
                    } finally {
                        isAddingCollector = false
                    }
                }
            }
        )
    }

    // EDIT COLLECTOR
    editingCollector?.let { collector ->
        EditCollectorDialog(
            collector = collector,
            onDismiss = { editingCollector = null },
            onSave = { updated ->
                val index = collectors.indexOfFirst {
                    it.collectorId == updated.collectorId
                }
                if (index >= 0) collectors[index] = updated
                editingCollector = null
            }
        )
    }

    // DELETE COLLECTOR
    deletingCollector?.let { collector ->
        DeleteCollectorDialog(
            collector = collector,
            onDismiss = { deletingCollector = null },
            onConfirm = {
                collectors.removeAll {
                    it.collectorId == collector.collectorId
                }
                deletingCollector = null
            }
        )
    }

    // REAL TASK HISTORY
    viewingTaskHistory?.let { collector ->
        CollectorTaskHistoryLoader(
            collector = collector,
            accessToken = accessToken,
            onDismiss = { viewingTaskHistory = null }
        )
    }
}

@Composable
private fun CollectorTaskHistoryLoader(
    collector: Collector,
    accessToken: String,
    onDismiss: () -> Unit
) {
    var history by remember(collector.collectorId) {
        mutableStateOf<CollectorTaskHistory?>(null)
    }
    var loading by remember(collector.collectorId) {
        mutableStateOf(true)
    }
    var error by remember(collector.collectorId) {
        mutableStateOf("")
    }

    LaunchedEffect(collector.collectorId, accessToken) {
        history = null
        error = ""
        loading = true

        try {
            val userId = collector.collectorId
                .removePrefix("COL-")
                .toIntOrNull()
                ?: throw IllegalArgumentException("Invalid collector ID.")

            history = parseCollectorTaskHistory(
                ApiClient.getCollectorTaskHistory(
                    accessToken = accessToken,
                    collectorId = userId.toString()
                )
            )
        } catch (e: Exception) {
            error = e.message ?: "Unable to load task history."
        } finally {
            loading = false
        }
    }

    CollectorTaskHistoryDialog(
        collector = collector,
        history = history,
        isLoading = loading,
        errorMessage = error,
        accessToken = accessToken,
        onDismiss = onDismiss
    )
}

@Composable
private fun SidebarItem(
    icon: ImageVector,
    title: String,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Row(
        Modifier
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
        Icon(icon, title, tint = White, modifier = Modifier.size(19.dp))
        Spacer(Modifier.width(14.dp))
        Text(title, color = White, fontSize = 13.sp)
    }
}

@Composable
private fun CollectorTableHeader() {
    Row(
        Modifier
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

@Composable
private fun CollectorRow(
    collector: Collector,
    onNameClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(45.dp)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableText(collector.collectorId, 0.14f)
        TableNameText(collector.fullName, 0.15f, onNameClick)
        TableText(collector.assignedArea, 0.15f)
        TableText(collector.phoneNumber, 0.13f)
        TableText(collector.tasks.toString(), 0.07f)
        TableText(collector.completed.toString(), 0.09f)

        Box(
            Modifier.weight(0.10f),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(
                        if (collector.status == "Active") {
                            ActiveGreen
                        } else {
                            Color(0xFFE1E1E1)
                        }
                    )
                    .padding(horizontal = 7.dp, vertical = 3.dp)
            ) {
                Text(
                    collector.status,
                    color = if (collector.status == "Active") {
                        ActiveText
                    } else {
                        Color.DarkGray
                    },
                    fontSize = 10.sp
                )
            }
        }

        Row(
            Modifier.weight(0.12f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onEdit,
                modifier = Modifier.size(29.dp)
            ) {
                Icon(
                    Icons.Default.Edit,
                    "Edit Collector",
                    tint = Color(0xFF555555),
                    modifier = Modifier.size(14.dp)
                )
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(29.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    "Delete Collector",
                    tint = Red,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
private fun RowScope.TableText(text: String, weight: Float) {
    Text(
        text = text,
        color = TextGreen,
        fontSize = 12.sp,
        maxLines = 1,
        modifier = Modifier.weight(weight).padding(end = 4.dp)
    )
}

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
        maxLines = 1,
        modifier = Modifier
            .weight(weight)
            .padding(end = 4.dp)
            .clickable(onClick = onClick)
    )
}

@Composable
private fun RowScope.TableHeaderText(text: String, weight: Float) {
    Text(
        text = text,
        color = TextGreen,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        maxLines = 1,
        modifier = Modifier.weight(weight).padding(end = 4.dp)
    )
}

@Composable
private fun AddCollectorTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    isPassword: Boolean = false
) {
    var showPassword by remember { mutableStateOf(false) }

    Row(
        Modifier
            .fillMaxWidth()
            .height(48.dp)
            .border(1.dp, LightBorder, RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = EcoDarkGreen,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(9.dp))

        Box(Modifier.weight(1f)) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                visualTransformation = if (isPassword && !showPassword) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
                modifier = Modifier.fillMaxWidth(),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 12.sp,
                    color = TextGreen
                ),
                decorationBox = { inner ->
                    if (value.isEmpty()) {
                        Text(
                            label,
                            fontSize = 12.sp,
                            color = Color(0xFF758078)
                        )
                    }
                    inner()
                }
            )
        }

        if (isPassword) {
            IconButton(
                onClick = { showPassword = !showPassword },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = if (showPassword) {
                        Icons.Default.VisibilityOff
                    } else {
                        Icons.Default.Visibility
                    },
                    contentDescription = if (showPassword) {
                        "Hide password"
                    } else {
                        "Show password"
                    },
                    tint = EcoDarkGreen,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun AddCollectorDialog(
    nextCollectorId: String,
    isSaving: Boolean,
    errorMessage: String,
    onDismiss: () -> Unit,
    onAdd: (String, String, String, String, String, String) -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var assignedArea by remember { mutableStateOf("Barangay Bulua") }
    var status by remember { mutableStateOf("Active") }
    var statusExpanded by remember { mutableStateOf(false) }

    val todayDate = SimpleDateFormat(
        "MMM dd, yyyy",
        Locale.getDefault()
    ).format(Date())

    val validEmail = android.util.Patterns.EMAIL_ADDRESS
        .matcher(email.trim())
        .matches()

    val canAdd = fullName.isNotBlank() &&
            validEmail &&
            password.length >= 8 &&
            phone.isNotBlank() &&
            assignedArea.isNotBlank() &&
            !isSaving

    AlertDialog(
        onDismissRequest = { if (!isSaving) onDismiss() },
        containerColor = White,
        title = {
            Column {
                Text(
                    "COLLECTORS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = EcoDarkGreen
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "Add New Collector",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextGreen
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Create a new garbage collector account.",
                    fontSize = 11.sp,
                    color = TextGreen
                )
            }
        },
        text = {
            Column(
                Modifier
                    .heightIn(max = 430.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AddCollectorTextField(
                    fullName, { fullName = it },
                    "Full Name", Icons.Default.Person
                )
                AddCollectorTextField(
                    email, { email = it },
                    "Email Address", Icons.Default.AccountCircle
                )
                AddCollectorTextField(
                    password, { password = it },
                    "Password (at least 8 characters)",
                    Icons.Default.Person, true
                )
                AddCollectorTextField(
                    phone, { phone = it },
                    "Phone Number", Icons.Default.Phone
                )
                AddCollectorTextField(
                    assignedArea, { assignedArea = it },
                    "Assigned Barangay", Icons.Default.LocationOn
                )

                if (email.isNotBlank() && !validEmail) {
                    Text(
                        "Please enter a valid email address.",
                        color = Red,
                        fontSize = 11.sp
                    )
                }

                if (password.isNotEmpty() && password.length < 8) {
                    Text(
                        "Password must contain at least 8 characters.",
                        color = Red,
                        fontSize = 11.sp
                    )
                }

                Box {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .border(1.dp, LightBorder, RoundedCornerShape(10.dp))
                            .clickable(enabled = !isSaving) {
                                statusExpanded = true
                            }
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Account Status",
                            fontSize = 11.sp,
                            color = TextGreen
                        )
                        Spacer(Modifier.weight(1f))
                        Text(status, fontSize = 12.sp, color = TextGreen)
                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            "Status",
                            tint = EcoDarkGreen
                        )
                    }

                    DropdownMenu(
                        expanded = statusExpanded,
                        onDismissRequest = { statusExpanded = false }
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

                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(EcoLightGreen, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        "System Information",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EcoDarkGreen
                    )
                    Text(
                        "Collector ID: assigned by the database",
                        fontSize = 11.sp,
                        color = TextGreen
                    )
                    Text("Date: $todayDate", fontSize = 11.sp, color = TextGreen)
                    Text(
                        "Login credentials are not emailed automatically.",
                        fontSize = 11.sp,
                        color = TextGreen
                    )
                }

                if (errorMessage.isNotBlank()) {
                    Text(errorMessage, color = Red, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    enabled = !isSaving,
                    modifier = Modifier.weight(1f).height(42.dp),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, EcoGreen)
                ) {
                    Text("Cancel", fontSize = 10.sp)
                }

                Button(
                    onClick = {
                        onAdd(
                            fullName.trim(),
                            email.trim(),
                            password,
                            phone.trim(),
                            assignedArea.trim(),
                            status
                        )
                    },
                    enabled = canAdd,
                    modifier = Modifier.weight(1f).height(42.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EcoGreen,
                        contentColor = White
                    )
                ) {
                    Text(
                        if (isSaving) "Saving..." else "Add Collector",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        dismissButton = {}
    )
}

@Composable
private fun EditCollectorDialog(
    collector: Collector,
    onDismiss: () -> Unit,
    onSave: (Collector) -> Unit
) {
    var fullName by remember { mutableStateOf(collector.fullName) }
    var phone by remember { mutableStateOf(collector.phoneNumber) }
    var area by remember { mutableStateOf(collector.assignedArea) }
    var status by remember { mutableStateOf(collector.status) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = White,
        title = {
            Text(
                "Edit Collector",
                color = TextGreen,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                AddCollectorTextField(
                    fullName, { fullName = it },
                    "Full Name", Icons.Default.Person
                )
                AddCollectorTextField(
                    phone, { phone = it },
                    "Phone Number", Icons.Default.Phone
                )
                AddCollectorTextField(
                    area, { area = it },
                    "Assigned Area", Icons.Default.LocationOn
                )

                Box {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true }
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Status: $status", color = TextGreen)
                        Spacer(Modifier.weight(1f))
                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            "Choose status"
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Active") },
                            onClick = {
                                status = "Active"
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Inactive") },
                            onClick = {
                                status = "Inactive"
                                expanded = false
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        collector.copy(
                            fullName = fullName,
                            phoneNumber = phone,
                            assignedArea = area,
                            status = status
                        )
                    )
                },
                enabled = fullName.isNotBlank() &&
                        phone.isNotBlank() &&
                        area.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EcoGreen
                )
            ) {
                Text("Save Changes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

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
            Text(
                "Delete Collector",
                color = TextGreen,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                "Are you sure you want to delete ${collector.fullName}? " +
                        "This action cannot be undone.",
                color = TextGreen
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Red)
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}




@Composable
private fun CollectorTaskHistoryDialog(
    collector: Collector,
    history: CollectorTaskHistory?,
    isLoading: Boolean,
    errorMessage: String,
    accessToken: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var imageUrl by remember(collector.collectorId) {
        mutableStateOf(collector.profileImage)
    }
    var isUploading by remember { mutableStateOf(false) }
    var uploadMessage by remember { mutableStateOf("") }
    var showPhotoOptions by remember { mutableStateOf(false) }

    suspend fun uploadImage(uri: Uri, mimeType: String) {
        var tempFile: File? = null

        try {
            isUploading = true
            uploadMessage = ""

            require(
                mimeType.lowercase(Locale.ROOT) in
                        listOf("image/jpeg", "image/png", "image/webp")
            ) {
                "Please select a JPG, PNG, or WEBP image."
            }

            require(accessToken.isNotBlank()) {
                "Please log in again before uploading a photo."
            }

            val extension = when (mimeType.lowercase(Locale.ROOT)) {
                "image/png" -> ".png"
                "image/webp" -> ".webp"
                else -> ".jpg"
            }

            tempFile = File.createTempFile(
                "collector_profile_",
                extension,
                context.cacheDir
            )

            val input = context.contentResolver.openInputStream(uri)
                ?: throw IllegalStateException("Could not read the image.")

            input.use { source ->
                tempFile.outputStream().use { output ->
                    source.copyTo(output)
                }
            }

            require(tempFile.length() > 0L) {
                "The selected image is empty."
            }

            require(tempFile.length() <= 5L * 1024 * 1024) {
                "Image must be 5 MB or smaller."
            }

            val collectorId = collector.collectorId
                .removePrefix("COL-")
                .toIntOrNull()
                ?.toString()
                ?: throw IllegalArgumentException("Invalid collector ID.")

            val response = ApiClient.uploadCollectorProfileImage(
                accessToken = accessToken,
                collectorId = collectorId,
                imageFile = tempFile,
                imageMimeType = mimeType.lowercase(Locale.ROOT)
            )

            val savedUrl = response
                .optJSONObject("collector")
                ?.optString("profile_image")
                ?.takeIf { it.isNotBlank() && it != "null" }
                ?: throw IllegalStateException(
                    "The server did not return the saved profile image URL."
                )

            // Refresh the image URL so Coil does not show a cached old photo.
            val separator = if (savedUrl.contains("?")) "&" else "?"
            imageUrl = "$savedUrl${separator}refresh=${System.currentTimeMillis()}"

            uploadMessage = "Profile picture saved."
        } catch (e: Exception) {
            uploadMessage = e.message ?: "Image upload failed."
        } finally {
            tempFile?.delete()
            isUploading = false
        }
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch {
                val mimeType = context.contentResolver.getType(uri)
                    ?: "image/jpeg"

                uploadImage(uri, mimeType)
            }
        }
    }

    if (showPhotoOptions) {
        AlertDialog(
            onDismissRequest = { showPhotoOptions = false },
            containerColor = White,
            title = {
                Text(
                    "Change Profile Picture",
                    color = TextGreen,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = {
                            showPhotoOptions = false
                            imagePicker.launch("image/*")
                        },
                        enabled = !isUploading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            Icons.Default.PhotoLibrary,
                            contentDescription = null
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Choose from Gallery")
                    }

                    TextButton(
                        onClick = { showPhotoOptions = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancel")
                    }
                }
            },
            confirmButton = {}
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .widthIn(max = 500.dp)
                .heightIn(max = 680.dp),
            shape = RoundedCornerShape(18.dp),
            color = White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 680.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                Text(
                    text = "${collector.collectorId} — Task History",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextGreen
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier.size(76.dp),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Box(
                            modifier = Modifier
                                .size(76.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE1F4E5)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (!imageUrl.isNullOrBlank()) {
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = "Collector profile picture",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Collector profile picture",
                                    tint = EcoGreen,
                                    modifier = Modifier.size(44.dp)
                                )
                            }
                        }

                        IconButton(
                            onClick = { showPhotoOptions = true },
                            enabled = !isUploading,
                            modifier = Modifier
                                .size(22.dp)
                                .background(EcoGreen, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Change profile picture",
                                tint = White,
                                modifier = Modifier.size(11.dp)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            collector.fullName,
                            color = TextGreen,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            collector.assignedArea.takeIf {
                                it.isNotBlank() &&
                                        !it.equals("Not Provided", true)
                            } ?: "Location not provided",
                            color = TextGreen,
                            fontSize = 12.sp
                        )

                        Text(
                            collector.phoneNumber.takeIf {
                                it.isNotBlank() &&
                                        !it.equals("Not Provided", true)
                            } ?: "Phone number not provided",
                            color = Color(0xFF758078),
                            fontSize = 12.sp
                        )
                    }
                }

                if (isUploading) {
                    Spacer(Modifier.height(8.dp))

                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = EcoGreen
                    )

                    Text(
                        "Saving profile picture...",
                        fontSize = 11.sp,
                        color = TextGreen
                    )
                }

                if (uploadMessage.isNotBlank()) {
                    Spacer(Modifier.height(6.dp))

                    Text(
                        uploadMessage,
                        color = if (uploadMessage == "Profile picture saved.")
                            EcoGreen else Red,
                        fontSize = 11.sp
                    )
                }

                Spacer(Modifier.height(20.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TaskSummaryCard(
                        label = "Total Tasks",
                        count = history?.totalTasks ?: 0,
                        modifier = Modifier.weight(1f),
                        background = Color(0xFFF1F9F3),
                        countColor = TextGreen
                    )

                    TaskSummaryCard(
                        label = "Completed",
                        count = history?.completedTasks ?: 0,
                        modifier = Modifier.weight(1f),
                        background = Color(0xFFE1F4E5),
                        countColor = EcoGreen
                    )

                    TaskSummaryCard(
                        label = "In Progress",
                        count = history?.inProgressTasks ?: 0,
                        modifier = Modifier.weight(1f),
                        background = Color(0xFFFFF1D6),
                        countColor = Color(0xFF9A6700)
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    "Assigned Reports",
                    fontWeight = FontWeight.Bold,
                    color = TextGreen
                )

                Spacer(Modifier.height(8.dp))

                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 100.dp, max = 320.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = EcoGreen,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }

                    errorMessage.isNotBlank() -> {
                        Text(
                            "Unable to load task history: $errorMessage",
                            color = Red,
                            fontSize = 12.sp
                        )
                    }

                    history == null || history.reports.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 70.dp, max = 320.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "No assigned tasks yet.",
                                color = Color(0xFF758078),
                                fontSize = 13.sp
                            )
                        }
                    }

                    else -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 320.dp),
                            verticalArrangement = Arrangement.spacedBy(9.dp)
                        ) {
                            history.reports.forEach { report ->
                                TaskHistoryReportCard(report)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = if (isLoading || errorMessage.isNotBlank()) {
                        "Task history has not finished loading."
                    } else {
                        "Showing ${history?.reports?.size ?: 0} of " +
                                "${history?.totalTasks ?: 0} total tasks"
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color(0xFF758078),
                    fontSize = 12.sp
                )

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EcoGreen
                    )
                ) {
                    Text("Close")
                }
            }
        }
    }
}



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
            .background(background, RoundedCornerShape(10.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count.toString(),
            color = countColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            color = TextGreen,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun TaskHistoryReportCard(
    report: TaskHistoryReport
) {
    val normalizedStatus = report.status.trim().lowercase(Locale.ROOT)
    val completed = normalizedStatus in listOf(
        "resolved",
        "completed"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, LightBorder, RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                report.reportId,
                color = TextGreen,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.weight(1f))
            Text(
                report.status,
                color = if (completed) EcoGreen else Color(0xFF9A6700),
                fontSize = 12.sp
            )
        }

        Spacer(Modifier.height(4.dp))

        Text(
            report.issue,
            color = TextGreen,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        )

        Text(
            report.location,
            color = Color(0xFF66756A),
            fontSize = 12.sp
        )

        Spacer(Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = if (completed) EcoGreen else Color(0xFFB7811B),
                modifier = Modifier.size(12.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                report.date,
                color = Color(0xFF758078),
                fontSize = 12.sp
            )
        }
    }
}
