
package edp.app.ecotrack.screens.waste_guide

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import edp.app.ecotrack.network.ApiClient
import kotlinx.coroutines.launch

// ============================================================
// COLORS
// ============================================================

private val EcoDarkGreen = Color(0xFF216B2A)
private val EcoGreen = Color(0xFF2E7D32)
private val EcoLightGreen = Color(0xFFE6F5EA)
private val EcoBorder = Color(0xFF8BE5A7)
private val EcoTextGreen = Color(0xFF286B32)
private val White = Color.White

// Biodegradable = Green
private val BiodegradableBackground = Color(0xFFE5F8EA)
private val BiodegradableText = Color(0xFF218A46)

// Non-biodegradable = Yellow
private val NonBiodegradableBackground = Color(0xFFFFF3C4)
private val NonBiodegradableText = Color(0xFF9A7300)

// Hazardous = Red
private val HazardousBackground = Color(0xFFF7DADA)
private val HazardousText = Color(0xFFC24F56)

// Recyclable = Blue
private val RecyclableBackground = Color(0xFFDCEBFF)
private val RecyclableText = Color(0xFF2463B5)

// ============================================================
// MODEL
// ============================================================

data class WasteGuideCategory(
    val id: Int,
    val name: String,
    val colorTheme: String,
    val description: String,
    val examples: List<String>,
    val disposalTips: String
)

// ============================================================
// GARBAGE TYPE COLORS
// ============================================================

private fun getWasteTypeBackground(type: String): Color {
    return when (type) {
        "Biodegradable" -> BiodegradableBackground
        "Non-biodegradable" -> NonBiodegradableBackground
        "Hazardous" -> HazardousBackground
        "Recyclable" -> RecyclableBackground
        else -> EcoLightGreen
    }
}

private fun getWasteTypeTextColor(type: String): Color {
    return when (type) {
        "Biodegradable" -> BiodegradableText
        "Non-biodegradable" -> NonBiodegradableText
        "Hazardous" -> HazardousText
        "Recyclable" -> RecyclableText
        else -> EcoGreen
    }
}

// ============================================================
// MAIN SCREEN
// ============================================================

@Composable
fun WasteGuideScreen(
    accessToken: String,
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onDashboardClick: () -> Unit = {},
    onWasteReportClick: () -> Unit = {},
    onCollectorsClick: () -> Unit = {},
    onResidentsClick: () -> Unit = {},
    onScheduleClick: () -> Unit = {},
    onAnalyticsClick: () -> Unit = {},
    hasUnreadNotifications: Boolean = true,
    profileImageUrl: String? = null
) {
    val categories = remember {
        mutableStateListOf<WasteGuideCategory>()
    }

    val scope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var showAddDialog by remember { mutableStateOf(false) }

    var editingCategory by remember {
        mutableStateOf<WasteGuideCategory?>(null)
    }

    var deletingCategory by remember {
        mutableStateOf<WasteGuideCategory?>(null)
    }

    // Pending Add/Edit confirmation
    var pendingSaveCategory by remember {
        mutableStateOf<WasteGuideCategory?>(null)
    }

    var pendingSaveIsEdit by remember {
        mutableStateOf(false)
    }

    // ========================================================
    // LOAD CATEGORIES FROM SUPABASE
    // ========================================================

    LaunchedEffect(accessToken) {
        if (accessToken.isBlank()) {
            errorMessage = "Please log in again."
            return@LaunchedEffect
        }

        isLoading = true
        errorMessage = null

        try {
            val response = ApiClient.getWasteGuide(accessToken)
            val array = response.getJSONArray("categories")

            categories.clear()

            for (i in 0 until array.length()) {
                val item = array.getJSONObject(i)
                val wasteType = item.getString("waste_type")

                val savedExamples = item.optJSONArray("examples")

                val examplesList = buildList {
                    if (savedExamples != null) {
                        for (index in 0 until savedExamples.length()) {
                            val example = savedExamples
                                .optString(index)
                                .trim()

                            if (example.isNotEmpty()) {
                                add(example)
                            }
                        }
                    }
                }

                categories.add(
                    WasteGuideCategory(
                        id = item.getInt("guide_id"),
                        name = wasteType,
                        colorTheme = wasteType,
                        description = item.optString(
                            "description",
                            ""
                        ),
                        examples = examplesList,
                        disposalTips = item.optString(
                            "disposal_method",
                            ""
                        )
                    )
                )
            }
        } catch (e: Exception) {
            errorMessage =
                e.message ?: "Failed to load waste guide."
        } finally {
            isLoading = false
        }
    }

    // ========================================================
    // MAIN LAYOUT
    // ========================================================

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoLightGreen)
    ) {
        WasteGuideSidebar(
            onDashboardClick = onDashboardClick,
            onWasteReportClick = onWasteReportClick,
            onCollectorsClick = onCollectorsClick,
            onResidentsClick = onResidentsClick,
            onScheduleClick = onScheduleClick,
            onAnalyticsClick = onAnalyticsClick
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            WasteGuideTopBar(
                onNotificationClick = onNotificationClick,
                onProfileClick = onProfileClick
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = 30.dp,
                        vertical = 18.dp
                    )
            ) {
                if (isLoading) {
                    Text(
                        text = "Loading waste guide...",
                        color = EcoDarkGreen,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                errorMessage?.let { message ->
                    Text(
                        text = message,
                        color = Color.Red,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Waste Segregation Guide",
                        color = EcoTextGreen,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )

                    Button(
                        onClick = {
                            errorMessage = null
                            showAddDialog = true
                        },
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EcoGreen
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.height(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Category",
                            tint = White,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(Modifier.width(6.dp))

                        Text(
                            text = "Add Category",
                            color = White,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                if (!isLoading && categories.isEmpty() &&
                    errorMessage == null
                ) {
                    Text(
                        text = "No waste categories found.",
                        color = EcoTextGreen,
                        fontSize = 13.sp
                    )
                }

                categories.forEach { category ->
                    WasteGuideCard(
                        category = category,
                        onEditClick = {
                            errorMessage = null
                            editingCategory = category
                        },
                        onDeleteClick = {
                            errorMessage = null
                            deletingCategory = category
                        }
                    )

                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }

    // ========================================================
    // ADD CATEGORY EDITOR
    // ========================================================

    if (showAddDialog) {
        WasteCategoryDialog(
            title = "Add Waste Category",
            initialCategory = null,
            dialogWidth = 500.dp,
            dialogHeight = 650.dp,
            onDismiss = {
                showAddDialog = false
            },
            onSave = { newCategory ->
                pendingSaveCategory = newCategory
                pendingSaveIsEdit = false
            }
        )
    }

    // ========================================================
    // EDIT CATEGORY EDITOR
    // ========================================================

    editingCategory?.let { category ->
        WasteCategoryDialog(
            title = "Edit - ${category.name}",
            initialCategory = category,
            dialogWidth = 500.dp,
            dialogHeight = 650.dp,
            onDismiss = {
                editingCategory = null
            },
            onSave = { updatedCategory ->
                pendingSaveCategory = updatedCategory
                pendingSaveIsEdit = true
            }
        )
    }

    // ========================================================
    // ADD / EDIT CONFIRMATION
    // ========================================================

    pendingSaveCategory?.let { pendingCategory ->
        ConfirmWasteCategoryDialog(
            isEdit = pendingSaveIsEdit,
            categoryName = pendingCategory.name,
            onDismiss = {
                pendingSaveCategory = null
            },
            onConfirm = {
                scope.launch {
                    isLoading = true
                    errorMessage = null

                    try {
                        val response = if (pendingSaveIsEdit) {
                            val originalId =
                                editingCategory?.id
                                    ?: pendingCategory.id

                            ApiClient.updateWasteGuideCategory(
                                accessToken = accessToken,
                                guideId = originalId,
                                wasteType = pendingCategory.name,
                                description = pendingCategory.description,
                                disposalMethod = pendingCategory.disposalTips,
                                examples = pendingCategory.examples
                            )
                        } else {
                            ApiClient.addWasteGuideCategory(
                                accessToken = accessToken,
                                wasteType = pendingCategory.name,
                                description = pendingCategory.description,
                                disposalMethod = pendingCategory.disposalTips,
                                examples = pendingCategory.examples
                            )
                        }

                        val savedCategory =
                            response.optJSONObject("category")
                                ?: response

                        val savedName =
                            savedCategory.getString("waste_type")

                        val savedExamplesArray =
                            savedCategory.optJSONArray("examples")

                        val savedExamples = buildList {
                            if (savedExamplesArray != null) {
                                for (index in 0 until
                                        savedExamplesArray.length()
                                ) {
                                    val example =
                                        savedExamplesArray
                                            .optString(index)
                                            .trim()

                                    if (example.isNotEmpty()) {
                                        add(example)
                                    }
                                }
                            }
                        }

                        val savedItem = WasteGuideCategory(
                            id = savedCategory.getInt("guide_id"),
                            name = savedName,
                            colorTheme = savedName,
                            description = savedCategory.optString(
                                "description",
                                pendingCategory.description
                            ),
                            examples = savedExamples,
                            disposalTips = savedCategory.optString(
                                "disposal_method",
                                pendingCategory.disposalTips
                            )
                        )

                        if (pendingSaveIsEdit) {
                            val index = categories.indexOfFirst {
                                it.id == savedItem.id
                            }

                            if (index >= 0) {
                                categories[index] = savedItem
                            }
                        } else {
                            categories.add(savedItem)
                        }

                        pendingSaveCategory = null
                        showAddDialog = false
                        editingCategory = null
                    } catch (e: Exception) {
                        errorMessage =
                            e.message ?: if (pendingSaveIsEdit) {
                                "Failed to update waste category."
                            } else {
                                "Failed to add waste category."
                            }

                        pendingSaveCategory = null
                    } finally {
                        isLoading = false
                    }
                }
            }
        )
    }

    // ========================================================
    // DELETE CATEGORY CONFIRMATION
    // ========================================================

    deletingCategory?.let { category ->
        DeleteWasteCategoryDialog(
            categoryName = category.name,
            onDismiss = {
                deletingCategory = null
            },
            onConfirm = {
                scope.launch {
                    isLoading = true
                    errorMessage = null

                    try {
                        ApiClient.deleteWasteGuideCategory(
                            accessToken = accessToken,
                            guideId = category.id
                        )

                        categories.removeAll {
                            it.id == category.id
                        }

                        deletingCategory = null
                    } catch (e: Exception) {
                        errorMessage =
                            e.message ?: "Failed to delete waste category."
                        deletingCategory = null
                    } finally {
                        isLoading = false
                    }
                }
            }
        )
    }
}

// ============================================================
// TOP BAR
// ============================================================

@Composable
private fun WasteGuideTopBar(
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

        Spacer(Modifier.width(15.dp))

        Text(
            text = "Waste Guide",
            color = EcoDarkGreen,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.weight(1f))

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
                contentDescription = "My Profile",
                tint = EcoDarkGreen,
                modifier = Modifier.size(31.dp)
            )
        }
    }
}

// ============================================================
// SIDEBAR
// ============================================================

@Composable
private fun WasteGuideSidebar(
    onDashboardClick: () -> Unit,
    onWasteReportClick: () -> Unit,
    onCollectorsClick: () -> Unit,
    onResidentsClick: () -> Unit,
    onScheduleClick: () -> Unit,
    onAnalyticsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(250.dp)
            .fillMaxHeight()
            .background(EcoDarkGreen)
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
                .padding(horizontal = 18.dp, vertical = 12.dp)
        ) {
            Text(
                text = "ADMINISTRATOR",
                color = Color(0xFF9FD6A5),
                fontSize = 11.sp
            )

            Spacer(Modifier.height(6.dp))

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

        Spacer(Modifier.height(20.dp))

        WasteGuideSidebarItem(
            Icons.Default.GridView,
            "Dashboard",
            false,
            onDashboardClick
        )

        WasteGuideSidebarItem(
            Icons.Default.Description,
            "Waste Report",
            false,
            onWasteReportClick
        )

        WasteGuideSidebarItem(
            Icons.Default.LocalShipping,
            "Collectors",
            false,
            onCollectorsClick
        )

        WasteGuideSidebarItem(
            Icons.Default.Groups,
            "Residents",
            false,
            onResidentsClick
        )

        WasteGuideSidebarItem(
            Icons.Default.CalendarMonth,
            "Schedules",
            false,
            onScheduleClick
        )

        WasteGuideSidebarItem(
            Icons.Default.BarChart,
            "Analytics",
            false,
            onAnalyticsClick
        )

        WasteGuideSidebarItem(
            Icons.Default.MenuBook,
            "Waste Guide",
            true,
            {}
        )
    }
}

// ============================================================
// SIDEBAR ITEM
// ============================================================

@Composable
private fun WasteGuideSidebarItem(
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
            .clip(RoundedCornerShape(22.dp))
            .background(
                if (selected) Color(0xFF719873)
                else Color.Transparent
            )
            .clickable { onClick() }
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = White,
            modifier = Modifier.size(19.dp)
        )

        Spacer(Modifier.width(14.dp))

        Text(
            text = text,
            color = White,
            fontSize = 13.sp
        )
    }
}

// ============================================================
// WASTE GUIDE CARD
// ============================================================

@Composable
private fun WasteGuideCard(
    category: WasteGuideCategory,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val themeBackground =
        getWasteTypeBackground(category.name)

    val themeText =
        getWasteTypeTextColor(category.name)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = White,
        shadowElevation = 4.dp
    ) {
        Column(Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(themeBackground)
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = category.name,
                        color = themeText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .clickable { onEditClick() }
                        .padding(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = EcoGreen,
                        modifier = Modifier.size(17.dp)
                    )

                    Spacer(Modifier.width(4.dp))

                    Text(
                        text = "Edit",
                        color = EcoGreen,
                        fontSize = 12.sp
                    )
                }

                Spacer(Modifier.width(8.dp))

                Row(
                    modifier = Modifier
                        .clickable { onDeleteClick() }
                        .padding(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFFE35D68),
                        modifier = Modifier.size(17.dp)
                    )

                    Spacer(Modifier.width(4.dp))

                    Text(
                        text = "Delete",
                        color = Color(0xFFE35D68),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = category.description,
                color = EcoTextGreen,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = "Examples",
                color = EcoTextGreen,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(5.dp))

            if (category.examples.isEmpty()) {
                Text(
                    text = "No examples added yet.",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            } else {
                category.examples.forEach { example ->
                    Row(Modifier.padding(vertical = 2.dp)) {
                        Text(
                            text = "•",
                            color = themeText,
                            fontSize = 13.sp
                        )

                        Spacer(Modifier.width(6.dp))

                        Text(
                            text = example,
                            color = EcoTextGreen,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Disposal Tips for Residents",
                color = EcoTextGreen,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = category.disposalTips,
                color = EcoTextGreen,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        }
    }
}

// ============================================================
// ADD / EDIT CATEGORY DIALOG
// ============================================================

@Composable
private fun WasteCategoryDialog(
    title: String,
    initialCategory: WasteGuideCategory?,
    dialogWidth: Dp,
    dialogHeight: Dp,
    onDismiss: () -> Unit,
    onSave: (WasteGuideCategory) -> Unit
) {
    val wasteTypes = listOf(
        "Biodegradable",
        "Non-biodegradable",
        "Hazardous",
        "Recyclable"
    )

    var selectedTheme by remember(initialCategory?.id) {
        mutableStateOf(
            initialCategory?.name ?: "Biodegradable"
        )
    }

    var description by remember(initialCategory?.id) {
        mutableStateOf(initialCategory?.description ?: "")
    }

    var disposalTips by remember(initialCategory?.id) {
        mutableStateOf(initialCategory?.disposalTips ?: "")
    }

    val examples = remember(initialCategory?.id) {
        mutableStateListOf(
            *(initialCategory?.examples ?: emptyList()).toTypedArray()
        )
    }

    var exampleInput by remember(initialCategory?.id) {
        mutableStateOf("")
    }

    var validationMessage by remember(initialCategory?.id) {
        mutableStateOf<String?>(null)
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .width(dialogWidth)
                .height(dialogHeight),
            shape = RoundedCornerShape(12.dp),
            color = White,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "WASTE SEGREGATION GUIDE",
                    color = EcoDarkGreen,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.height(5.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        color = EcoTextGreen,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = EcoTextGreen,
                        modifier = Modifier
                            .size(22.dp)
                            .clickable { onDismiss() }
                    )
                }

                Spacer(Modifier.height(13.dp))

                DialogLabel("Garbage Type")

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    wasteTypes.chunked(2).forEach { typeRow ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                                Arrangement.spacedBy(8.dp)
                        ) {
                            typeRow.forEach { type ->
                                ThemeChoice(
                                    text = type,
                                    selected = selectedTheme == type,
                                    background =
                                        getWasteTypeBackground(type),
                                    textColor =
                                        getWasteTypeTextColor(type),
                                    onClick = {
                                        selectedTheme = type
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            if (typeRow.size == 1) {
                                Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                DialogLabel("Description")

                SmallTextField(
                    value = description,
                    onValueChange = {
                        description = it
                        validationMessage = null
                    },
                    placeholder = "Enter description"
                )

                Spacer(Modifier.height(12.dp))

                DialogLabel("Examples")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SmallTextField(
                        value = exampleInput,
                        onValueChange = { exampleInput = it },
                        placeholder = "Type an example",
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(Modifier.width(5.dp))

                    Button(
                        onClick = {
                            val value = exampleInput.trim()

                            if (value.isNotEmpty()) {
                                examples.add(value)
                                exampleInput = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE5F7EA),
                            contentColor = EcoGreen
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.height(40.dp)
                    ) {
                        Text("Add", fontSize = 12.sp)
                    }
                }

                Spacer(Modifier.height(5.dp))

                examples.forEachIndexed { index, example ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFE9F8ED))
                            .padding(
                                horizontal = 12.dp,
                                vertical = 8.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = example,
                            color = EcoTextGreen,
                            fontSize = 12.sp,
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove example",
                            tint = Color(0xFF20A457),
                            modifier = Modifier
                                .size(16.dp)
                                .clickable {
                                    examples.removeAt(index)
                                }
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                DialogLabel("Disposal Tips for Residents")

                SmallTextField(
                    value = disposalTips,
                    onValueChange = {
                        disposalTips = it
                        validationMessage = null
                    },
                    placeholder = "Enter disposal tips"
                )

                validationMessage?.let { message ->
                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = message,
                        color = Color.Red,
                        fontSize = 11.sp
                    )
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = White,
                            contentColor = EcoGreen
                        ),
                        border = BorderStroke(1.dp, EcoBorder),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel", fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            when {
                                description.trim().isEmpty() -> {
                                    validationMessage =
                                        "Please enter a description."
                                }

                                disposalTips.trim().isEmpty() -> {
                                    validationMessage =
                                        "Please enter disposal tips."
                                }

                                else -> {
                                    validationMessage = null

                                    onSave(
                                        WasteGuideCategory(
                                            id = initialCategory?.id ?: 0,
                                            name = selectedTheme,
                                            colorTheme = selectedTheme,
                                            description = description.trim(),
                                            examples = examples
                                                .map { it.trim() }
                                                .filter { it.isNotEmpty() },
                                            disposalTips =
                                                disposalTips.trim()
                                        )
                                    )
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EcoGreen
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = if (initialCategory == null) {
                                "Add Category"
                            } else {
                                "Save Changes"
                            },
                            color = White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

// ============================================================
// ADD / EDIT CONFIRMATION DIALOG
// ============================================================

@Composable
private fun ConfirmWasteCategoryDialog(
    isEdit: Boolean,
    categoryName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.width(420.dp),
            shape = RoundedCornerShape(12.dp),
            color = White,
            shadowElevation = 8.dp
        ) {
            Column(Modifier.padding(22.dp)) {
                Text(
                    text = if (isEdit) {
                        "Confirm Changes"
                    } else {
                        "Confirm New Category"
                    },
                    color = EcoTextGreen,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = if (isEdit) {
                        "Save the changes to \"$categoryName\"? Your description, examples, and disposal tips will be updated."
                    } else {
                        "Add \"$categoryName\" to the Waste Segregation Guide?"
                    },
                    color = EcoTextGreen,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )

                Spacer(Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = White,
                            contentColor = EcoGreen
                        ),
                        border = BorderStroke(1.dp, EcoBorder),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel", fontSize = 12.sp)
                    }

                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EcoGreen
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = if (isEdit) "Save Changes" else "Confirm Add",
                            color = White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

// ============================================================
// DIALOG LABEL
// ============================================================

@Composable
private fun DialogLabel(text: String) {
    Text(
        text = text,
        color = EcoTextGreen,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    )

    Spacer(Modifier.height(4.dp))
}

// ============================================================
// SMALL TEXT FIELD
// ============================================================

@Composable
private fun SmallTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = Color(0xFF80A486),
                fontSize = 11.sp
            )
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = White,
            unfocusedContainerColor = White,
            focusedIndicatorColor = EcoBorder,
            unfocusedIndicatorColor = EcoBorder,
            cursorColor = EcoGreen
        ),
        shape = RoundedCornerShape(14.dp),
        textStyle = TextStyle(
            color = EcoTextGreen,
            fontSize = 11.sp
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp)
    )
}

// ============================================================
// GARBAGE TYPE OPTION
// ============================================================

@Composable
private fun ThemeChoice(
    text: String,
    selected: Boolean,
    background: Color,
    textColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(42.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(if (selected) background else White)
            .border(
                width = 1.dp,
                color = if (selected) {
                    textColor
                } else {
                    Color(0xFFD5E8D9)
                },
                shape = RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) textColor else EcoTextGreen,
            fontSize = 10.sp
        )
    }
}

// ============================================================
// DELETE CONFIRMATION DIALOG
// ============================================================

@Composable
private fun DeleteWasteCategoryDialog(
    categoryName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.width(420.dp),
            shape = RoundedCornerShape(12.dp),
            color = White,
            shadowElevation = 8.dp
        ) {
            Column(Modifier.padding(22.dp)) {
                Text(
                    text = "Delete Waste Category",
                    color = EcoTextGreen,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Are you sure you want to delete \"$categoryName\"? This category will be removed from the Waste Segregation Guide.",
                    color = EcoTextGreen,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )

                Spacer(Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = White,
                            contentColor = EcoGreen
                        ),
                        border = BorderStroke(1.dp, EcoBorder),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel", fontSize = 12.sp)
                    }

                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD9535F)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Delete",
                            color = White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
