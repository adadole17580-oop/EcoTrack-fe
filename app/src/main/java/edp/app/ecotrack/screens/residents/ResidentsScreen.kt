package edp.app.ecotrack.screens.residents

import androidx.annotation.DrawableRes
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============================================================
// COLORS
// SAME AS DASHBOARD / COLLECTORS
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
// MAIN SCREEN
// ============================================================

@Composable
fun ResidentsScreen(
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
    // FRONTEND SAMPLE DATA
    // ========================================================

    val residents = remember {

        listOf(

            Resident(
                residentId = "RES-001",
                name = "Maria Santos",
                barangay = "Brgy. Area 3",
                phone = "0912-345-6789",
                reports = 4,
                joined = "Jan 07",
                status = "Active"
            ),

            Resident(
                residentId = "RES-002",
                name = "John Reyes",
                barangay = "Brgy. Area 1",
                phone = "0922-123-4567",
                reports = 2,
                joined = "Jan 07",
                status = "Active"
            ),

            Resident(
                residentId = "RES-003",
                name = "Ana Gonzalez",
                barangay = "Brgy. Area 2",
                phone = "0933-555-8899",
                reports = 3,
                joined = "Jan 07",
                status = "Active"
            ),

            Resident(
                residentId = "RES-004",
                name = "Pedro Cruz",
                barangay = "Brgy. Area 3",
                phone = "0944-777-1234",
                reports = 1,
                joined = "Jan 07",
                status = "Inactive"
            )
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

            // =================================================
            // LOGO
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
            // ADMINISTRATOR INFORMATION
            // SAME SIZE AS DASHBOARD
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
                    text = admin.role,
                    color = Color(0xFF9FD6A5),
                    fontSize = 11.sp
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

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

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            // =================================================
            // DASHBOARD
            // =================================================

            ResidentsSidebarItem(
                icon = Icons.Default.GridView,
                text = "Dashboard",
                selected = false,
                onClick = onDashboardClick
            )

            // =================================================
            // WASTE REPORT
            // =================================================

            ResidentsSidebarItem(
                icon = Icons.Default.Description,
                text = "Waste Report",
                selected = false,
                onClick = onWasteReportClick
            )

            // =================================================
            // COLLECTORS
            // =================================================

            ResidentsSidebarItem(
                icon = Icons.Default.LocalShipping,
                text = "Collectors",
                selected = false,
                onClick = onCollectorsClick
            )

            // =================================================
            // RESIDENTS
            // =================================================

            ResidentsSidebarItem(
                icon = Icons.Default.Groups,
                text = "Residents",
                selected = true,
                onClick = {}
            )

            // =================================================
            // SCHEDULES
            // =================================================

            ResidentsSidebarItem(
                icon = Icons.Default.CalendarMonth,
                text = "Schedules",
                selected = false,
                onClick = onScheduleClick
            )

            // =================================================
            // ANALYTICS
            // =================================================

            ResidentsSidebarItem(
                icon = Icons.Default.BarChart,
                text = "Analytics",
                selected = false,
                onClick = onAnalyticsClick
            )

            // =================================================
            // WASTE GUIDE
            // =================================================

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
            // SAME AS DASHBOARD
            // =================================================

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp)
                    .background(EcoTopBar)
                    .padding(horizontal = 25.dp),

                verticalAlignment = Alignment.CenterVertically
            ) {

                // MENU

                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = EcoDarkGreen,
                    modifier = Modifier.size(23.dp)
                )

                Spacer(
                    modifier = Modifier.width(15.dp)
                )

                // TITLE

                Text(
                    text = "Residents",
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

                // =================================================
                // PROFILE
                // =================================================

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

                // =================================================
                // PAGE TITLE
                // =================================================

                Text(
                    text = "Resident Management",
                    color = TextGreen,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(
                    modifier = Modifier.height(31.dp)
                )

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

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(190.dp)
                        ) {

                            items(
                                items = residents,
                                key = {
                                    it.residentId
                                }
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
            .padding(
                horizontal = 12.dp
            ),

        verticalAlignment = Alignment.CenterVertically
    ) {

        ResidentHeaderText(
            text = "RESIDENT",
            weight = 0.18f
        )

        ResidentHeaderText(
            text = "BARANGAY",
            weight = 0.15f
        )

        ResidentHeaderText(
            text = "PHONE",
            weight = 0.17f
        )

        ResidentHeaderText(
            text = "REPORTS",
            weight = 0.12f
        )

        ResidentHeaderText(
            text = "JOINED",
            weight = 0.10f
        )

        ResidentHeaderText(
            text = "STATUS",
            weight = 0.13f
        )

        ResidentHeaderText(
            text = "ACTION",
            weight = 0.15f
        )
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
            .padding(
                horizontal = 12.dp
            ),

        verticalAlignment = Alignment.CenterVertically
    ) {

        // =================================================
        // RESIDENT
        // =================================================

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

        // =================================================
        // BARANGAY
        // =================================================

        Text(
            text = resident.barangay,
            color = TextGreen,
            fontSize = 12.sp,

            modifier = Modifier
                .weight(0.15f)
                .padding(
                    end = 4.dp
                )
        )

        // =================================================
        // PHONE
        // =================================================

        Text(
            text = resident.phone,
            color = TextGreen,
            fontSize = 12.sp,

            modifier = Modifier
                .weight(0.17f)
                .padding(
                    end = 4.dp
                )
        )

        // =================================================
        // REPORTS
        // =================================================

        Text(
            text = resident.reports.toString(),
            color = TextGreen,
            fontSize = 12.sp,

            modifier = Modifier.weight(0.12f)
        )

        // =================================================
        // JOINED
        // =================================================

        Text(
            text = resident.joined,
            color = TextGreen,
            fontSize = 12.sp,

            modifier = Modifier.weight(0.10f)
        )

        // =================================================
        // STATUS
        // =================================================

        Box(
            modifier = Modifier.weight(0.13f),

            contentAlignment = Alignment.CenterStart
        ) {

            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(8.dp)
                    )
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

                    color =
                        if (resident.status == "Active") {
                            ActiveText
                        } else {
                            InactiveText
                        },

                    fontSize = 12.sp
                )
            }
        }

        // =================================================
        // ACTION
        // =================================================

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

                Spacer(
                    modifier = Modifier.width(3.dp)
                )

                Text(
                    text = "Report",
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

                verticalAlignment =
                    Alignment.CenterVertically
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

                verticalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                // =================================================
                // RESIDENT SUMMARY
                // =================================================

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFFE8F6EB),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(10.dp),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(35.dp)
                            .clip(CircleShape)
                            .background(
                                Color(0xFFCDE5D1)
                            ),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Person,

                            contentDescription = null,

                            tint = EcoGreen,

                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(
                        modifier = Modifier.width(9.dp)
                    )

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
                            text =
                                "${resident.barangay} • ${resident.phone}",

                            color =
                                Color(0xFF638168),

                            fontSize = 10.sp
                        )

                        Text(
                            text =
                                "Resident ID: ${resident.residentId}",

                            color =
                                Color(0xFF638168),

                            fontSize = 10.sp
                        )
                    }

                    StatusBadge(
                        text = resident.status
                    )
                }

                // =================================================
                // REPORT COUNT BOXES
                // =================================================

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.spacedBy(6.dp)
                ) {

                    ReportCountBox(
                        count = totalReports,

                        label = "Total",

                        background =
                            Color(0xFFDCEFE1),

                        modifier =
                            Modifier.weight(1f)
                    )

                    ReportCountBox(
                        count = pendingReports,

                        label = "Pending",

                        background =
                            Color(0xFFF6DCCB),

                        modifier =
                            Modifier.weight(1f)
                    )

                    ReportCountBox(
                        count = inProgressReports,

                        label = "In Progress",

                        background =
                            Color(0xFFD7F0DC),

                        modifier =
                            Modifier.weight(1f)
                    )

                    ReportCountBox(
                        count = resolvedReports,

                        label = "Resolved",

                        background =
                            Color(0xFFD2E6D5),

                        modifier =
                            Modifier.weight(1f)
                    )
                }
            }
        },

        // =====================================================
        // BUTTONS
        // =====================================================

        confirmButton = {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                // =================================================
                // CANCEL BUTTON
                // =================================================

                androidx.compose.material3.OutlinedButton(
                    onClick = onDismiss,

                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),

                    shape =
                        RoundedCornerShape(20.dp),

                    border =
                        androidx.compose.foundation.BorderStroke(
                            1.dp,
                            EcoGreen
                        ),

                    colors =
                        ButtonDefaults.outlinedButtonColors(
                            containerColor =
                                Color.Transparent,

                            contentColor =
                                EcoGreen
                        ),

                    contentPadding =
                        PaddingValues(
                            horizontal = 16.dp
                        )
                ) {

                    Text(
                        text = "Cancel",

                        fontSize = 10.sp
                    )
                }

                // =================================================
                // VIEW REPORT TABLE BUTTON
                // =================================================

                Button(
                    onClick = {
                        onViewReportTable()
                    },

                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),

                    shape =
                        RoundedCornerShape(20.dp),

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                EcoGreen,

                            contentColor =
                                Color.White
                        ),

                    contentPadding =
                        PaddingValues(
                            horizontal = 16.dp
                        )
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.Visibility,

                        contentDescription = null,

                        modifier =
                            Modifier.size(13.dp)
                    )

                    Spacer(
                        modifier =
                            Modifier.width(4.dp)
                    )

                    Text(
                        text =
                            "View in Report Table",

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

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
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

    val background =
        when (text) {

            "Active" ->
                ActiveBackground

            "Pending" ->
                PendingBackground

            "In Progress" ->
                ProgressBackground

            "Resolved" ->
                ResolvedBackground

            else ->
                InactiveBackground
        }

    val textColor =
        when (text) {

            "Active" ->
                ActiveText

            "Pending" ->
                PendingText

            "In Progress" ->
                ProgressText

            "Resolved" ->
                ResolvedText

            else ->
                InactiveText
        }

    Box(
        modifier = Modifier
            .clip(
                RoundedCornerShape(8.dp)
            )
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