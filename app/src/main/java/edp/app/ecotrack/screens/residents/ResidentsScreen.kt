
package edp.app.ecotrack.screens.residents

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edp.app.ecotrack.network.ApiClient
import kotlinx.coroutines.CancellationException
import org.json.JSONArray
import org.json.JSONObject

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

private val ActiveBackground = Color(0xFFD0F1D7)
private val ActiveText = Color(0xFF39924A)

private val InactiveBackground = Color(0xFFE1E1E1)
private val InactiveText = Color(0xFF777777)

private val PendingBackground = Color(0xFFF1DFAF)
private val PendingText = Color(0xFFC0922D)

private val ProgressBackground = Color(0xFFD0F1D7)
private val ProgressText = Color(0xFF39924A)

private val ResolvedBackground = Color(0xFFC5DEC9)
private val ResolvedText = Color(0xFF3C8550)

private val SearchBackground = Color(0xFFE9F5EC)
private val SearchBorder = Color(0xFF8BE5A7)

// ============================================================
// ADMIN INFORMATION
// ============================================================

private data class AdminInfo(
    val role: String,
    val name: String,
    val email: String
)

private val admin = AdminInfo(
    role = "ADMINISTRATOR",
    name = "System Admin",
    email = "admin@ecotrack.gov.ph"
)

// ============================================================
// RESIDENT MODEL
// ============================================================

data class Resident(
    val residentId: String,
    val name: String,
    val barangay: String,
    val phone: String,
    val reports: Int,
    val joined: String,
    val status: String
)

// ============================================================
// JSON HELPER
// ============================================================

private fun firstNonBlank(
    json: JSONObject?,
    vararg keys: String
): String {
    if (json == null) return ""

    for (key in keys) {
        val value = json.optString(key, "").trim()

        if (value.isNotEmpty() && value != "null") {
            return value
        }
    }

    return ""
}

private fun getReportCount(item: JSONObject): Int {
    if (item.has("report_count")) {
        return item.optInt("report_count", 0)
    }

    if (item.has("reports_count")) {
        return item.optInt("reports_count", 0)
    }

    val reportsValue = item.opt("reports")

    return when (reportsValue) {
        is JSONArray -> reportsValue.length()
        is Number -> reportsValue.toInt()
        else -> 0
    }
}

private fun formatJoinedDate(value: String): String {
    if (value.isBlank()) return "—"

    return try {
        val datePart = value.take(10)
        val parts = datePart.split("-")

        if (parts.size == 3) {
            val year = parts[0]
            val month = parts[1].toInt()
            val day = parts[2].toInt()

            val monthName = when (month) {
                1 -> "Jan"
                2 -> "Feb"
                3 -> "Mar"
                4 -> "Apr"
                5 -> "May"
                6 -> "Jun"
                7 -> "Jul"
                8 -> "Aug"
                9 -> "Sep"
                10 -> "Oct"
                11 -> "Nov"
                12 -> "Dec"
                else -> return datePart
            }

            "$monthName ${day.toString().padStart(2, '0')}"
        } else {
            value
        }
    } catch (_: Exception) {
        value
    }
}

// ============================================================
// MAIN SCREEN
// ============================================================

@Composable
fun ResidentsScreen(
    accessToken: String = "",

    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},

    onDashboardClick: () -> Unit = {},
    onWasteReportClick: () -> Unit = {},
    onCollectorsClick: () -> Unit = {},
    onScheduleClick: () -> Unit = {},
    onAnalyticsClick: () -> Unit = {},
    onWasteGuideClick: () -> Unit = {},

    @DrawableRes adminProfileImageRes: Int? = null,

    hasUnreadNotifications: Boolean = true
) {
    // ========================================================
    // BACKEND STATE
    // ========================================================

    var residents by remember {
        mutableStateOf<List<Resident>>(emptyList())
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var refreshKey by remember {
        mutableStateOf(0)
    }

    // ========================================================
    // LOAD RESIDENTS FROM BACKEND
    // ========================================================

    LaunchedEffect(accessToken, refreshKey) {
        if (accessToken.isBlank()) {
            residents = emptyList()
            errorMessage = "Please log in again to load residents."
            isLoading = false
        } else {
            isLoading = true
            errorMessage = ""

            try {
                val response = ApiClient.getResidents(accessToken)

                val residentArray = response.optJSONArray("residents")

                if (
                    response.optString("status")
                        .equals("ERROR", ignoreCase = true)
                ) {
                    errorMessage = response.optString(
                        "message",
                        "Unable to load residents."
                    )
                } else if (residentArray == null) {
                    errorMessage = response.optString(
                        "message",
                        "The backend response does not contain a residents list."
                    )
                } else {
                    val loadedResidents = buildList {
                        for (i in 0 until residentArray.length()) {
                            val item = residentArray.optJSONObject(i)
                                ?: continue

                            val barangayObject =
                                item.optJSONObject("barangays")

                            val rawId = firstNonBlank(
                                item,
                                "resident_id",
                                "resident_code",
                                "user_id",
                                "id"
                            )

                            val residentId = when {
                                rawId.isBlank() ->
                                    "RES-${(i + 1).toString().padStart(3, '0')}"

                                rawId.startsWith(
                                    "RES-",
                                    ignoreCase = true
                                ) -> rawId

                                rawId.length > 12 ->
                                    "RES-${rawId.take(6).uppercase()}"

                                else -> rawId
                            }

                            val residentName = firstNonBlank(
                                item,
                                "full_name",
                                "name",
                                "username"
                            ).ifBlank {
                                "Unnamed Resident"
                            }

                            val barangay = firstNonBlank(
                                item,
                                "barangay",
                                "barangay_name"
                            ).ifBlank {
                                firstNonBlank(
                                    barangayObject,
                                    "name",
                                    "barangay_name"
                                )
                            }.ifBlank {
                                "Not assigned"
                            }

                            val phone = firstNonBlank(
                                item,
                                "contact_number",
                                "phone",
                                "phone_number"
                            ).ifBlank {
                                "Not provided"
                            }

                            val reportCount = getReportCount(item)

                            val createdAt = firstNonBlank(
                                item,
                                "created_at",
                                "joined"
                            )

                            val joined = formatJoinedDate(createdAt)

                            val isActive = item.optBoolean(
                                "is_active",
                                true
                            )

                            val rawStatus = firstNonBlank(
                                item,
                                "status"
                            )

                            val status = when {
                                rawStatus.equals(
                                    "inactive",
                                    ignoreCase = true
                                ) -> "Inactive"

                                rawStatus.equals(
                                    "active",
                                    ignoreCase = true
                                ) -> "Active"

                                item.has("is_active") && !isActive ->
                                    "Inactive"

                                else -> "Active"
                            }

                            add(
                                Resident(
                                    residentId = residentId,
                                    name = residentName,
                                    barangay = barangay,
                                    phone = phone,
                                    reports = reportCount,
                                    joined = joined,
                                    status = status
                                )
                            )
                        }
                    }

                    residents = loadedResidents
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                errorMessage = e.message
                    ?: "Unable to connect to the backend."
            } finally {
                isLoading = false
            }
        }
    }

    // ========================================================
    // SEARCH STATE
    // ========================================================

    var searchText by remember {
        mutableStateOf("")
    }

    val filteredResidents = residents.filter { resident ->
        searchText.isBlank() ||
                resident.name.contains(
                    searchText,
                    ignoreCase = true
                ) ||
                resident.residentId.contains(
                    searchText,
                    ignoreCase = true
                ) ||
                resident.barangay.contains(
                    searchText,
                    ignoreCase = true
                ) ||
                resident.phone.contains(
                    searchText,
                    ignoreCase = true
                )
    }

    // ========================================================
    // POPUP STATE
    // ========================================================

    var selectedResident by remember {
        mutableStateOf<Resident?>(null)
    }

    // ========================================================
    // SCREEN
    // ========================================================

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoLightGreen)
    ) {
        // ==================================================
        // SIDEBAR
        // ==================================================

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
                    text = admin.role,
                    color = Color(0xFF9FD6A5),
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = admin.name,
                    color = White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = admin.email,
                    color = Color(0xFFB9DDBD),
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            ResidentsSidebarItem(
                icon = Icons.Default.GridView,
                text = "Dashboard",
                selected = false,
                onClick = onDashboardClick
            )

            ResidentsSidebarItem(
                icon = Icons.Default.Description,
                text = "Waste Report",
                selected = false,
                onClick = onWasteReportClick
            )

            ResidentsSidebarItem(
                icon = Icons.Default.LocalShipping,
                text = "Collectors",
                selected = false,
                onClick = onCollectorsClick
            )

            ResidentsSidebarItem(
                icon = Icons.Default.Groups,
                text = "Residents",
                selected = true,
                onClick = {}
            )

            ResidentsSidebarItem(
                icon = Icons.Default.CalendarMonth,
                text = "Schedules",
                selected = false,
                onClick = onScheduleClick
            )

            ResidentsSidebarItem(
                icon = Icons.Default.BarChart,
                text = "Analytics",
                selected = false,
                onClick = onAnalyticsClick
            )

            ResidentsSidebarItem(
                icon = Icons.Default.MenuBook,
                text = "Waste Guide",
                selected = false,
                onClick = onWasteGuideClick
            )
        }

        // ==================================================
        // MAIN AREA
        // ==================================================

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

                Spacer(modifier = Modifier.width(15.dp))

                Text(
                    text = "Residents",
                    color = EcoDarkGreen,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.weight(1f))

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

                IconButton(
                    onClick = onProfileClick
                ) {
                    if (adminProfileImageRes != null) {
                        Image(
                            painter = painterResource(
                                id = adminProfileImageRes
                            ),
                            contentDescription = "Admin Profile",
                            modifier = Modifier
                                .size(31.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            tint = EcoDarkGreen,
                            modifier = Modifier.size(31.dp)
                        )
                    }
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
                Text(
                    text = "Resident Management",
                    color = TextGreen,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(20.dp))

                // =================================================
                // SEARCH BAR
                // =================================================

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
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
                                color = SearchBorder,
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

                        Spacer(modifier = Modifier.width(5.dp))

                        BasicTextField(
                            value = searchText,
                            onValueChange = {
                                searchText = it
                            },
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 12.sp,
                                color = TextGreen
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            decorationBox = { innerTextField ->
                                Box {
                                    if (searchText.isEmpty()) {
                                        Text(
                                            text = "Search residents...",
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                    }

                                    innerTextField()
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // =================================================
                // RESIDENT TABLE
                // =================================================

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(10.dp)
                        ),
                    shape = RoundedCornerShape(10.dp),
                    color = White
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ResidentTableHeader()

                        when {
                            isLoading -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(190.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = EcoGreen
                                    )
                                }
                            }

                            errorMessage.isNotBlank() -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(190.dp)
                                        .padding(16.dp),
                                    horizontalAlignment =
                                        Alignment.CenterHorizontally,
                                    verticalArrangement =
                                        Arrangement.Center
                                ) {
                                    Text(
                                        text = errorMessage,
                                        color = Color(0xFFB3261E),
                                        fontSize = 12.sp
                                    )

                                    Spacer(
                                        modifier = Modifier.height(10.dp)
                                    )

                                    Button(
                                        onClick = {
                                            refreshKey++
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = EcoGreen
                                        )
                                    ) {
                                        Text("Retry")
                                    }
                                }
                            }

                            filteredResidents.isEmpty() -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (searchText.isBlank()) {
                                            "No residents found."
                                        } else {
                                            "No residents match your search."
                                        },
                                        color = TextGreen,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            else -> {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(190.dp)
                                ) {
                                    items(
                                        items = filteredResidents,
                                        key = { it.residentId }
                                    ) { resident ->
                                        ResidentRow(
                                            resident = resident,
                                            onReportClick = {
                                                selectedResident = resident
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ==========================================================
    // RESIDENT REPORTS POPUP
    // ==========================================================

    selectedResident?.let { resident ->
        ResidentReportsDialog(
            resident = resident,
            onDismiss = {
                selectedResident = null
            },
            onViewReportTable = {
                selectedResident = null
                onWasteReportClick()
            }
        )
    }
}

// ============================================================
// SIDEBAR ITEM
// ============================================================

@Composable
private fun ResidentsSidebarItem(
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
            .clip(RoundedCornerShape(22.dp))
            .background(
                if (selected) SelectedGreen else Color.Transparent
            )
            .clickable {
                onClick()
            }
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = White,
            modifier = Modifier.size(19.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

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
private fun ResidentTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(
                Color(0xFFF3F9F4),
                RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ResidentHeaderText("RESIDENT", 0.18f)
        ResidentHeaderText("BARANGAY", 0.15f)
        ResidentHeaderText("PHONE", 0.17f)
        ResidentHeaderText("REPORTS", 0.12f)
        ResidentHeaderText("JOINED", 0.10f)
        ResidentHeaderText("STATUS", 0.13f)
        ResidentHeaderText("ACTION", 0.15f)
    }
}

// ============================================================
// HEADER TEXT
// ============================================================

@Composable
private fun RowScope.ResidentHeaderText(
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
// RESIDENT ROW
// ============================================================

@Composable
private fun ResidentRow(
    resident: Resident,
    onReportClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // RESIDENT

        Column(
            modifier = Modifier.weight(0.18f)
        ) {
            Text(
                text = resident.name,
                color = TextGreen,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 12.sp
            )

            Text(
                text = resident.residentId,
                color = Color(0xFF8DA590),
                fontSize = 12.sp,
                lineHeight = 12.sp
            )
        }

        // BARANGAY

        Text(
            text = resident.barangay,
            color = TextGreen,
            fontSize = 12.sp,
            modifier = Modifier
                .weight(0.15f)
                .padding(end = 4.dp)
        )

        // PHONE

        Text(
            text = resident.phone,
            color = TextGreen,
            fontSize = 12.sp,
            modifier = Modifier
                .weight(0.17f)
                .padding(end = 4.dp)
        )

        // REPORTS

        Text(
            text = resident.reports.toString(),
            color = TextGreen,
            fontSize = 12.sp,
            modifier = Modifier.weight(0.12f)
        )

        // JOINED

        Text(
            text = resident.joined,
            color = TextGreen,
            fontSize = 12.sp,
            modifier = Modifier.weight(0.10f)
        )

        // STATUS

        Box(
            modifier = Modifier.weight(0.13f),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (resident.status == "Active") {
                            ActiveBackground
                        } else {
                            InactiveBackground
                        }
                    )
                    .padding(
                        horizontal = 7.dp,
                        vertical = 3.dp
                    )
            ) {
                Text(
                    text = resident.status,
                    color = if (resident.status == "Active") {
                        ActiveText
                    } else {
                        InactiveText
                    },
                    fontSize = 12.sp
                )
            }
        }

        // ACTION

        Box(
            modifier = Modifier.weight(0.15f),
            contentAlignment = Alignment.CenterStart
        ) {
            Button(
                onClick = onReportClick,
                modifier = Modifier.height(27.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE8F4EA)
                ),
                contentPadding = PaddingValues(
                    horizontal = 9.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = "Reports",
                    tint = EcoGreen,
                    modifier = Modifier.size(11.dp)
                )

                Spacer(modifier = Modifier.width(3.dp))

                Text(
                    text = "Reports",
                    color = EcoGreen,
                    fontSize = 12.sp
                )
            }
        }
    }
}

// ============================================================
// RESIDENT REPORTS DIALOG
// ============================================================

@Composable
private fun ResidentReportsDialog(
    resident: Resident,
    onDismiss: () -> Unit,
    onViewReportTable: () -> Unit
) {
    // These counts retain the original UI's estimated breakdown.
    // They are not actual status counts from the database yet.

    val totalReports = resident.reports

    val pendingReports =
        if (resident.reports > 0) 1 else 0

    val inProgressReports =
        if (resident.reports > 1) 1 else 0

    val resolvedReports =
        if (resident.reports > 2) {
            resident.reports - 2
        } else {
            0
        }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = White,
        shape = RoundedCornerShape(10.dp),

        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "RESIDENTS REPORTS",
                    color = TextGreen,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color(0xFF222222),
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        },

        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // RESIDENT SUMMARY

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFFE8F6EB),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(35.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFCDE5D1)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = EcoGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(9.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = resident.name,
                            color = TextGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "${resident.barangay} • ${resident.phone}",
                            color = Color(0xFF638168),
                            fontSize = 10.sp
                        )

                        Text(
                            text = "Resident ID: ${resident.residentId}",
                            color = Color(0xFF638168),
                            fontSize = 10.sp
                        )
                    }

                    StatusBadge(text = resident.status)
                }

                // REPORT COUNT BOXES

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ReportCountBox(
                        count = totalReports,
                        label = "Total",
                        background = Color(0xFFDCEFE1),
                        modifier = Modifier.weight(1f)
                    )

                    ReportCountBox(
                        count = pendingReports,
                        label = "Pending",
                        background = Color(0xFFF6DCCB),
                        modifier = Modifier.weight(1f)
                    )

                    ReportCountBox(
                        count = inProgressReports,
                        label = "In Progress",
                        background = Color(0xFFD7F0DC),
                        modifier = Modifier.weight(1f)
                    )

                    ReportCountBox(
                        count = resolvedReports,
                        label = "Resolved",
                        background = Color(0xFFD2E6D5),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },

        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // CANCEL BUTTON

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, EcoGreen),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = EcoGreen
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp
                    )
                ) {
                    Text(
                        text = "Cancel",
                        fontSize = 10.sp
                    )
                }

                // VIEW REPORT TABLE BUTTON

                Button(
                    onClick = onViewReportTable,
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EcoGreen,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "View in Report Table",
                        fontSize = 10.sp
                    )
                }
            }
        }
    )
}

// ============================================================
// REPORT COUNT BOX
// ============================================================

@Composable
private fun ReportCountBox(
    count: Int,
    label: String,
    background: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(48.dp)
            .background(
                background,
                RoundedCornerShape(7.dp)
            )
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = count.toString(),
            color = TextGreen,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = label,
            color = Color(0xFF58735C),
            fontSize = 12.sp
        )
    }
}

// ============================================================
// STATUS BADGE
// ============================================================

@Composable
private fun StatusBadge(
    text: String
) {
    val background = when (text) {
        "Active" -> ActiveBackground
        "Pending" -> PendingBackground
        "In Progress" -> ProgressBackground
        "Resolved" -> ResolvedBackground
        else -> InactiveBackground
    }

    val textColor = when (text) {
        "Active" -> ActiveText
        "Pending" -> PendingText
        "In Progress" -> ProgressText
        "Resolved" -> ResolvedText
        else -> InactiveText
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(background)
            .padding(
                horizontal = 7.dp,
                vertical = 3.dp
            )
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp
        )
    }
}
