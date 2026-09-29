package edp.app.ecotrack.screens.waste_guide

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
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

// ============================================================
// COLORS - SAME AS DASHBOARD
// ============================================================

private val EcoDarkGreen = Color(0xFF216B2A)
private val EcoGreen = Color(0xFF2E7D32)
private val EcoLightGreen = Color(0xFFE6F5EA)
private val EcoTopBar = Color(0xFFE0F2E5)
private val EcoBorder = Color(0xFF8BE5A7)
private val EcoTextGreen = Color(0xFF286B32)
private val White = Color.White

// ============================================================
// CATEGORY COLORS
// ============================================================

private val BiodegradableBackground = Color(0xFFE5F8EA)
private val BiodegradableText = Color(0xFF218A46)

private val RecyclableBackground = Color(0xFFDDF5F8)
private val RecyclableText = Color(0xFF168999)

private val ResidualBackground = Color(0xFFF5EACD)
private val ResidualText = Color(0xFFA57A19)

private val InfectiousBackground = Color(0xFFF7DADA)
private val InfectiousText = Color(0xFFC24F56)

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
// SAMPLE DATA
// ============================================================

private fun sampleCategories(): List<WasteGuideCategory> {

    return listOf(

        WasteGuideCategory(
            id = 1,
            name = "Biodegradable",
            colorTheme = "Biodegradable",
            description =
                "Waste materials that can naturally decompose and return to the environment.",
            examples = listOf(
                "Food scraps",
                "Fruit and vegetable peels",
                "Leaves",
                "Garden waste"
            ),
            disposalTips =
                "Separate biodegradable waste from other waste. Place food scraps and organic materials in the proper biodegradable container."
        ),

        WasteGuideCategory(
            id = 2,
            name = "Recyclable",
            colorTheme = "Recyclable",
            description =
                "Materials that can be collected, processed, and used again to create new products.",
            examples = listOf(
                "Plastic bottles",
                "Paper",
                "Cardboard",
                "Clean cans"
            ),
            disposalTips =
                "Clean and dry recyclable materials before placing them in the recycling container."
        ),

        WasteGuideCategory(
            id = 3,
            name = "Residual",
            colorTheme = "Residual",
            description =
                "Waste that cannot be composted or recycled and needs proper disposal.",
            examples = listOf(
                "Used tissue",
                "Dirty wrappers",
                "Styrofoam",
                "Contaminated packaging"
            ),
            disposalTips =
                "Place residual waste in the designated residual waste container and avoid mixing it with recyclable materials."
        ),

        WasteGuideCategory(
            id = 4,
            name = "Infectious",
            colorTheme = "Infectious",
            description =
                "Waste that may contain harmful microorganisms and can cause infection or disease.",
            examples = listOf(
                "Used masks",
                "Used gloves",
                "Medical waste",
                "Contaminated materials"
            ),
            disposalTips =
                "Handle infectious waste carefully and place it in the designated container. Do not mix it with ordinary household waste."
        )
    )
}

// ============================================================
// MAIN SCREEN
// ============================================================

@Composable
fun WasteGuideScreen(
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
        mutableStateListOf(
            *sampleCategories().toTypedArray()
        )
    }

    var showAddDialog by remember {
        mutableStateOf(false)
    }

    var editingCategory by remember {
        mutableStateOf<WasteGuideCategory?>(null)
    }

    var deletingCategory by remember {
        mutableStateOf<WasteGuideCategory?>(null)
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoLightGreen)
    ) {

        // ====================================================
        // SIDEBAR
        // ====================================================

        WasteGuideSidebar(
            onDashboardClick = onDashboardClick,
            onWasteReportClick = onWasteReportClick,
            onCollectorsClick = onCollectorsClick,
            onResidentsClick = onResidentsClick,
            onScheduleClick = onScheduleClick,
            onAnalyticsClick = onAnalyticsClick
        )

        // ====================================================
        // MAIN AREA
        // ====================================================

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
                    .fillMaxSize()
                    .padding(
                        horizontal = 30.dp,
                        vertical = 18.dp
                    )
            ) {

                // =================================================
                // TITLE + ADD BUTTON
                // =================================================

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
                            showAddDialog = true
                        },
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

                        Spacer(
                            modifier = Modifier.width(6.dp)
                        )

                        Text(
                            text = "Add Category",
                            color = White,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                // =================================================
                // CATEGORY CARDS
                // =================================================

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement =
                        Arrangement.spacedBy(16.dp)
                ) {

                    items(
                        items = categories,
                        key = { it.id }
                    ) { category ->

                        WasteGuideCard(
                            category = category,

                            onEditClick = {
                                editingCategory = category
                            },

                            onDeleteClick = {
                                deletingCategory = category
                            }
                        )
                    }
                }
            }
        }
    }

    // ==========================================================
    // ADD CATEGORY
    // ==========================================================

    if (showAddDialog) {

        WasteCategoryDialog(
            title = "Add Waste Category",
            initialCategory = null,

            dialogWidth = 500.dp,
            dialogHeight = 500.dp,

            onDismiss = {
                showAddDialog = false
            },

            onSave = { newCategory ->

                val newId =
                    (categories.maxOfOrNull {
                        it.id
                    } ?: 0) + 1

                categories.add(
                    newCategory.copy(
                        id = newId
                    )
                )

                showAddDialog = false
            }
        )
    }

    // ==========================================================
    // EDIT CATEGORY
    // ==========================================================

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

                val index =
                    categories.indexOfFirst {
                        it.id == category.id
                    }

                if (index >= 0) {

                    categories[index] =
                        updatedCategory.copy(
                            id = category.id
                        )
                }

                editingCategory = null
            }
        )
    }

    // ==========================================================
    // DELETE CONFIRMATION
    // ==========================================================

    deletingCategory?.let { category ->

        DeleteWasteCategoryDialog(
            categoryName = category.name,

            onDismiss = {
                deletingCategory = null
            },

            onConfirm = {

                categories.removeAll {
                    it.id == category.id
                }

                deletingCategory = null
            }
        )
    }
}

// ============================================================
// TOP BAR - SAME AS DASHBOARD
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

        Spacer(
            modifier = Modifier.width(15.dp)
        )

        Text(
            text = "Waste Guide",
            color = EcoDarkGreen,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

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
                contentDescription = "My Profile",
                tint = EcoDarkGreen,
                modifier = Modifier.size(31.dp)
            )
        }
    }
}

// ============================================================
// SIDEBAR - SAME AS DASHBOARD
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

        WasteGuideSidebarItem(
            icon = Icons.Default.GridView,
            text = "Dashboard",
            selected = false,
            onClick = onDashboardClick
        )

        WasteGuideSidebarItem(
            icon = Icons.Default.Description,
            text = "Waste Report",
            selected = false,
            onClick = onWasteReportClick
        )

        WasteGuideSidebarItem(
            icon = Icons.Default.LocalShipping,
            text = "Collectors",
            selected = false,
            onClick = onCollectorsClick
        )

        WasteGuideSidebarItem(
            icon = Icons.Default.Groups,
            text = "Residents",
            selected = false,
            onClick = onResidentsClick
        )

        WasteGuideSidebarItem(
            icon = Icons.Default.CalendarMonth,
            text = "Schedules",
            selected = false,
            onClick = onScheduleClick
        )

        WasteGuideSidebarItem(
            icon = Icons.Default.BarChart,
            text = "Analytics",
            selected = false,
            onClick = onAnalyticsClick
        )

        WasteGuideSidebarItem(
            icon = Icons.Default.MenuBook,
            text = "Waste Guide",
            selected = true,
            onClick = {}
        )
    }
}

// ============================================================
// SIDEBAR ITEM - SAME AS DASHBOARD
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
            .padding(
                horizontal = 15.dp,
                vertical = 4.dp
            )
            .height(42.dp)
            .clip(
                RoundedCornerShape(22.dp)
            )
            .background(
                if (selected)
                    Color(0xFF719873)
                else
                    Color.Transparent
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
// WASTE GUIDE CARD
// ============================================================

@Composable
private fun WasteGuideCard(
    category: WasteGuideCategory,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {

    val themeBackground =
        when (category.colorTheme) {

            "Biodegradable" ->
                BiodegradableBackground

            "Recyclable" ->
                RecyclableBackground

            "Residual" ->
                ResidualBackground

            "Infectious" ->
                InfectiousBackground

            else ->
                EcoLightGreen
        }

    val themeText =
        when (category.colorTheme) {

            "Biodegradable" ->
                BiodegradableText

            "Recyclable" ->
                RecyclableText

            "Residual" ->
                ResidualText

            "Infectious" ->
                InfectiousText

            else ->
                EcoGreen
        }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = White,
        shadowElevation = 4.dp
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(8.dp)
                        )
                        .background(themeBackground)
                        .padding(
                            horizontal = 12.dp,
                            vertical = 7.dp
                        )
                ) {

                    Text(
                        text = category.name,
                        color = themeText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                Row(
                    modifier = Modifier
                        .clickable {
                            onEditClick()
                        }
                        .padding(6.dp),

                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = EcoGreen,
                        modifier = Modifier.size(17.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(4.dp)
                    )

                    Text(
                        text = "Edit",
                        color = EcoGreen,
                        fontSize = 12.sp
                    )
                }

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Row(
                    modifier = Modifier
                        .clickable {
                            onDeleteClick()
                        }
                        .padding(6.dp),

                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFFE35D68),
                        modifier = Modifier.size(17.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(4.dp)
                    )

                    Text(
                        text = "Delete",
                        color = Color(0xFFE35D68),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = category.description,
                color = EcoTextGreen,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "Examples",
                color = EcoTextGreen,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            category.examples.forEach { example ->

                Row(
                    modifier = Modifier.padding(
                        vertical = 2.dp
                    )
                ) {

                    Text(
                        text = "•",
                        color = themeText,
                        fontSize = 13.sp
                    )

                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )

                    Text(
                        text = example,
                        color = EcoTextGreen,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Disposal Tips for Residents",
                color = EcoTextGreen,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

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

    var categoryName by remember {
        mutableStateOf(
            initialCategory?.name ?: ""
        )
    }

    var selectedTheme by remember {
        mutableStateOf(
            initialCategory?.colorTheme
                ?: "Biodegradable"
        )
    }

    var description by remember {
        mutableStateOf(
            initialCategory?.description ?: ""
        )
    }

    var disposalTips by remember {
        mutableStateOf(
            initialCategory?.disposalTips ?: ""
        )
    }

    val examples = remember {
        mutableStateListOf(
            *(initialCategory?.examples
                ?: emptyList())
                .toTypedArray()
        )
    }

    var exampleInput by remember {
        mutableStateOf("")
    }

    Dialog(
        onDismissRequest = onDismiss
    ) {

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
                    .verticalScroll(
                        rememberScrollState()
                    )
            ) {

                // ================================================
                // WASTE SEGREGATION GUIDE
                // ================================================

                Text(
                    text = "WASTE SEGREGATION GUIDE",
                    color = EcoDarkGreen,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                // ================================================
                // TITLE + CLOSE
                // ================================================

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
                            .clickable {
                                onDismiss()
                            }
                    )
                }

                Spacer(
                    modifier = Modifier.height(13.dp)
                )

                // ================================================
                // CATEGORY NAME
                // ================================================

                DialogLabel(
                    text = "Category Name"
                )

                SmallTextField(
                    value = categoryName,
                    onValueChange = {
                        categoryName = it
                    },
                    placeholder = "Enter category name"
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                // ================================================
                // COLOR THEME
                // ================================================

                DialogLabel(
                    text = "Color Theme"
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    ThemeChoice(
                        text = "Biodegradable",
                        selected =
                            selectedTheme ==
                                    "Biodegradable",
                        background =
                            BiodegradableBackground,
                        textColor =
                            BiodegradableText,
                        onClick = {
                            selectedTheme =
                                "Biodegradable"
                        }
                    )

                    ThemeChoice(
                        text = "Recyclable",
                        selected =
                            selectedTheme ==
                                    "Recyclable",
                        background =
                            RecyclableBackground,
                        textColor =
                            RecyclableText,
                        onClick = {
                            selectedTheme =
                                "Recyclable"
                        }
                    )
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    ThemeChoice(
                        text = "Residual",
                        selected =
                            selectedTheme ==
                                    "Residual",
                        background =
                            ResidualBackground,
                        textColor =
                            ResidualText,
                        onClick = {
                            selectedTheme =
                                "Residual"
                        }
                    )

                    ThemeChoice(
                        text = "Infectious",
                        selected =
                            selectedTheme ==
                                    "Infectious",
                        background =
                            InfectiousBackground,
                        textColor =
                            InfectiousText,
                        onClick = {
                            selectedTheme =
                                "Infectious"
                        }
                    )
                }

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                // ================================================
                // DESCRIPTION
                // ================================================

                DialogLabel(
                    text = "Description"
                )

                SmallTextField(
                    value = description,
                    onValueChange = {
                        description = it
                    },
                    placeholder = "Enter description"
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                // ================================================
                // EXAMPLES
                // ================================================

                DialogLabel(
                    text = "Examples"
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    SmallTextField(
                        value = exampleInput,
                        onValueChange = {
                            exampleInput = it
                        },
                        placeholder = "Type an example",
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(
                        modifier = Modifier.width(5.dp)
                    )

                    Button(
                        onClick = {

                            if (
                                exampleInput
                                    .trim()
                                    .isNotEmpty()
                            ) {

                                examples.add(
                                    exampleInput.trim()
                                )

                                exampleInput = ""
                            }
                        },

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    Color(0xFFE5F7EA),
                                contentColor =
                                    EcoGreen
                            ),

                        shape =
                            RoundedCornerShape(16.dp),

                        modifier =
                            Modifier.height(40.dp)
                    ) {

                        Text(
                            text = "Add",
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                examples.forEachIndexed { index, example ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = 2.dp
                            )
                            .clip(
                                RoundedCornerShape(16.dp)
                            )
                            .background(
                                Color(0xFFE9F8ED)
                            )
                            .padding(
                                horizontal = 12.dp,
                                vertical = 8.dp
                            ),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Text(
                            text = example,
                            color = EcoTextGreen,
                            fontSize = 12.sp,
                            modifier =
                                Modifier.weight(1f)
                        )

                        Icon(
                            imageVector =
                                Icons.Default.Close,
                            contentDescription =
                                "Remove example",

                            tint =
                                Color(0xFF20A457),

                            modifier =
                                Modifier
                                    .size(16.dp)
                                    .clickable {

                                        examples.removeAt(
                                            index
                                        )
                                    }
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                // ================================================
                // DISPOSAL TIPS
                // ================================================

                DialogLabel(
                    text = "Disposal Tips for Residents"
                )

                SmallTextField(
                    value = disposalTips,
                    onValueChange = {
                        disposalTips = it
                    },
                    placeholder = "Enter disposal tips"
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                // ================================================
                // BUTTONS
                // ================================================

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    Button(
                        onClick = onDismiss,

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = White,
                                contentColor = EcoGreen
                            ),

                        border =
                            androidx.compose.foundation.BorderStroke(
                                1.dp,
                                EcoBorder
                            ),

                        shape =
                            RoundedCornerShape(16.dp),

                        modifier =
                            Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Cancel",
                            fontSize = 12.sp
                        )
                    }

                    Button(
                        onClick = {

                            if (
                                categoryName
                                    .trim()
                                    .isNotEmpty()
                            ) {

                                onSave(
                                    WasteGuideCategory(
                                        id =
                                            initialCategory
                                                ?.id ?: 0,

                                        name =
                                            categoryName
                                                .trim(),

                                        colorTheme =
                                            selectedTheme,

                                        description =
                                            description
                                                .trim(),

                                        examples =
                                            examples.toList(),

                                        disposalTips =
                                            disposalTips
                                                .trim()
                                    )
                                )
                            }
                        },

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    EcoGreen
                            ),

                        shape =
                            RoundedCornerShape(16.dp),

                        modifier =
                            Modifier.weight(1f)
                    ) {

                        Text(
                            text =
                                if (
                                    initialCategory == null
                                ) {
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
// DIALOG LABEL
// ============================================================

@Composable
private fun DialogLabel(
    text: String
) {

    Text(
        text = text,
        color = EcoTextGreen,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    )

    Spacer(
        modifier = Modifier.height(4.dp)
    )
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

        colors =
            TextFieldDefaults.colors(
                focusedContainerColor = White,
                unfocusedContainerColor = White,
                focusedIndicatorColor = EcoBorder,
                unfocusedIndicatorColor = EcoBorder,
                cursorColor = EcoGreen
            ),

        shape =
            RoundedCornerShape(14.dp),

        textStyle =
            TextStyle(
                color = EcoTextGreen,
                fontSize = 11.sp
            ),

        modifier = modifier
            .fillMaxWidth()
            .height(45.dp)
    )
}

// ============================================================
// COLOR THEME
// ============================================================

@Composable
private fun ThemeChoice(
    text: String,
    selected: Boolean,
    background: Color,
    textColor: Color,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .width(210.dp)
            .height(38.dp)
            .clip(
                RoundedCornerShape(18.dp)
            )
            .background(
                if (selected)
                    background
                else
                    White
            )
            .border(
                width = 1.dp,
                color =
                    if (selected)
                        textColor
                    else
                        Color(0xFFD5E8D9),
                shape =
                    RoundedCornerShape(18.dp)
            )
            .clickable {
                onClick()
            },

        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            color =
                if (selected)
                    textColor
                else
                    EcoTextGreen,
            fontSize = 10.sp
        )
    }
}

// ============================================================
// DELETE CONFIRMATION
// ============================================================

@Composable
private fun DeleteWasteCategoryDialog(
    categoryName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {

    Dialog(
        onDismissRequest = onDismiss
    ) {

        Surface(
            modifier = Modifier.width(420.dp),
            shape = RoundedCornerShape(12.dp),
            color = White,
            shadowElevation = 8.dp
        ) {

            Column(
                modifier = Modifier.padding(22.dp)
            ) {

                Text(
                    text = "Delete Waste Category",
                    color = EcoTextGreen,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Text(
                    text =
                        "Are you sure you want to delete \"$categoryName\"? This category will be removed from the Waste Segregation Guide.",

                    color = EcoTextGreen,

                    fontSize = 12.sp,

                    lineHeight = 18.sp
                )

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    Button(
                        onClick = onDismiss,

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = White,
                                contentColor = EcoGreen
                            ),

                        border =
                            androidx.compose.foundation.BorderStroke(
                                1.dp,
                                EcoBorder
                            ),

                        shape =
                            RoundedCornerShape(16.dp),

                        modifier =
                            Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Cancel",
                            fontSize = 12.sp
                        )
                    }

                    Button(
                        onClick = onConfirm,

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    Color(0xFFD9535F)
                            ),

                        shape =
                            RoundedCornerShape(16.dp),

                        modifier =
                            Modifier.weight(1f)
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