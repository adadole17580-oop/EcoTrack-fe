package edp.app.ecotrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Recycling
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edp.app.ecotrack.ui.components.EcoTrackTopBar
import edp.app.ecotrack.ui.theme.EcoGreen
import edp.app.ecotrack.ui.theme.EcoGreenLight
import edp.app.ecotrack.ui.theme.EcoSecondaryText
import edp.app.ecotrack.ui.theme.EcoText
import edp.app.ecotrack.ui.theme.EcoWhite
import edp.app.ecotrack.ui.theme.WasteBiodegradable
import edp.app.ecotrack.ui.theme.WasteBiodegradableText
import edp.app.ecotrack.ui.theme.WasteHazardous
import edp.app.ecotrack.ui.theme.WasteHazardousText
import edp.app.ecotrack.ui.theme.WasteRecyclable
import edp.app.ecotrack.ui.theme.WasteRecyclableText
import edp.app.ecotrack.ui.theme.WasteResidual
import edp.app.ecotrack.ui.theme.WasteResidualText

private data class WasteCategory(
    val title: String,
    val description: String,
    val examples: List<String>,
    val backgroundColor: Color,
    val textColor: Color
)

@Composable
fun WasteGuideScreen() {

    val categories = listOf(
        WasteCategory(
            title = "Biodegradable",
            description = "Waste that naturally breaks down over time.",
            examples = listOf(
                "Fruit and vegetable peels",
                "Leftover food",
                "Leaves and grass",
                "Other organic waste"
            ),
            backgroundColor = WasteBiodegradable,
            textColor = WasteBiodegradableText
        ),
        WasteCategory(
            title = "Recyclable",
            description = "Materials that can be collected and processed for reuse.",
            examples = listOf(
                "Plastic bottles",
                "Clean paper and cardboard",
                "Glass containers",
                "Metal cans"
            ),
            backgroundColor = WasteRecyclable,
            textColor = WasteRecyclableText
        ),
        WasteCategory(
            title = "Non-Biodegradable",
            description = "Waste that does not easily decompose naturally.",
            examples = listOf(
                "Plastic wrappers",
                "Styrofoam",
                "Used packaging",
                "Other residual waste"
            ),
            backgroundColor = WasteResidual,
            textColor = WasteResidualText
        ),
        WasteCategory(
            title = "Hazardous",
            description = "Waste that may be harmful and needs special handling.",
            examples = listOf(
                "Batteries",
                "Chemical containers",
                "Broken fluorescent bulbs",
                "Other hazardous materials"
            ),
            backgroundColor = WasteHazardous,
            textColor = WasteHazardousText
        )
    )

    var expandedCategory by remember {
        mutableStateOf<String?>(null)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        EcoTrackTopBar(
            title = "Waste Guide",
            subtitle = "Know where each type of waste belongs"
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = EcoGreenLight
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Outlined.Recycling,
                        contentDescription = "Waste guide",
                        tint = EcoGreen,
                        modifier = Modifier.size(28.dp)
                    )

                    Column(
                        modifier = Modifier.padding(start = 12.dp)
                    ) {

                        Text(
                            text = "Proper segregation matters",
                            style = MaterialTheme.typography.titleMedium,
                            color = EcoText
                        )

                        Text(
                            text = "Tap a category to see common examples.",
                            style = MaterialTheme.typography.bodySmall,
                            color = EcoSecondaryText,
                            modifier = Modifier.padding(top = 3.dp)
                        )
                    }
                }
            }

            categories.forEach { category ->

                WasteCategoryCard(
                    category = category,
                    expanded = expandedCategory == category.title,
                    onClick = {
                        expandedCategory =
                            if (expandedCategory == category.title) {
                                null
                            } else {
                                category.title
                            }
                    }
                )
            }

            Spacer(
                modifier = Modifier.size(8.dp)
            )
        }
    }
}

@Composable
private fun WasteCategoryCard(
    category: WasteCategory,
    expanded: Boolean,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = EcoWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = when (category.title) {
                            "Recyclable" -> Icons.Outlined.Recycling
                            "Hazardous" -> Icons.Outlined.WarningAmber
                            else -> Icons.Outlined.DeleteOutline
                        },
                        contentDescription = category.title,
                        tint = category.textColor,
                        modifier = Modifier
                            .size(24.dp)
                            .background(
                                color = category.backgroundColor,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(4.dp)
                    )

                    Column(
                        modifier = Modifier.padding(start = 12.dp)
                    ) {

                        Text(
                            text = category.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = EcoText
                        )

                        Text(
                            text = category.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = EcoSecondaryText,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }

                Icon(
                    imageVector = if (expanded) {
                        Icons.Outlined.ExpandLess
                    } else {
                        Icons.Outlined.ExpandMore
                    },
                    contentDescription = if (expanded) {
                        "Collapse"
                    } else {
                        "Expand"
                    },
                    tint = EcoSecondaryText,
                    modifier = Modifier.size(22.dp)
                )
            }

            if (expanded) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(category.backgroundColor.copy(alpha = 0.45f))
                        .padding(
                            start = 18.dp,
                            end = 18.dp,
                            top = 12.dp,
                            bottom = 15.dp
                        ),
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {

                    Text(
                        text = "Common examples",
                        style = MaterialTheme.typography.labelLarge,
                        color = category.textColor
                    )

                    category.examples.forEach { example ->

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "•",
                                color = category.textColor,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Text(
                                text = example,
                                style = MaterialTheme.typography.bodySmall,
                                color = EcoText,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}