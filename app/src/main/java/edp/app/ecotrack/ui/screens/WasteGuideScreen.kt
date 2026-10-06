package edp.app.ecotrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edp.app.ecotrack.ui.theme.EcoGreenDark
import edp.app.ecotrack.ui.theme.EcoWhite
import edp.app.ecotrack.ui.theme.WasteBiodegradable
import edp.app.ecotrack.ui.theme.WasteHazardous
import edp.app.ecotrack.ui.theme.WasteRecyclable
import edp.app.ecotrack.ui.theme.WasteResidual

private data class WasteCategory(
    val name: String,
    val description: String,
    val examples: List<String>,
    val instructions: List<String>,
    val color: Color,
    val icon: ImageVector
)

@Composable
fun WasteGuideScreen() {

    val wasteCategories = remember {
        listOf(
            WasteCategory(
                name = "Biodegradable",
                description = "Waste that naturally breaks down and can be composted.",
                examples = listOf(
                    "Food scraps & kitchen leftovers",
                    "Fruit and vegetable peels",
                    "Leaves, grass & garden waste",
                    "Coffee grounds & tea bags",
                    "Eggshells & organic paper"
                ),
                instructions = listOf(
                    "Place food and plant waste in the biodegradable bin.",
                    "Remove plastic, glass, and other non-biodegradable materials.",
                    "Use biodegradable waste for composting when possible."
                ),
                color = WasteBiodegradable,
                icon = Icons.Default.LocalDining
            ),
            WasteCategory(
                name = "Recyclable",
                description = "Materials that can be processed and used to make new products.",
                examples = listOf(
                    "Paper, newspapers & cardboard boxes",
                    "Plastic bottles & containers",
                    "Glass bottles and jars",
                    "Metal cans & aluminum foil",
                    "Clean plastic packaging"
                ),
                instructions = listOf(
                    "Clean and dry recyclable materials before disposal.",
                    "Separate recyclable materials from food waste.",
                    "Place recyclable items in the designated recycling bin."
                ),
                color = WasteRecyclable,
                icon = Icons.Default.Recycling
            ),
            WasteCategory(
                name = "Residual",
                description = "Waste that cannot be composted or economically recycled.",
                examples = listOf(
                    "Used tissues & sanitary products",
                    "Soiled food packaging",
                    "Styrofoam containers",
                    "Contaminated plastic wrappers",
                    "Single-use non-recyclable items"
                ),
                instructions = listOf(
                    "Make sure the waste cannot be recycled or composted.",
                    "Place residual waste in the designated residual bin.",
                    "Keep residual waste separate from recyclable materials."
                ),
                color = WasteResidual,
                icon = Icons.Default.DeleteOutline
            ),
            WasteCategory(
                name = "Hazardous",
                description = "Waste that may be harmful to people or the environment.",
                examples = listOf(
                    "Household batteries & electronics",
                    "Chemical containers & pesticides",
                    "Paint materials & thinners",
                    "Expired medications",
                    "Fluorescent light bulbs"
                ),
                instructions = listOf(
                    "Do not mix hazardous waste with regular household waste.",
                    "Keep hazardous materials in a safe and secure place.",
                    "Bring hazardous waste to an appropriate collection facility."
                ),
                color = WasteHazardous,
                icon = Icons.Default.Warning
            )
        )
    }

    var expandedCategory by remember {
        mutableStateOf<String?>(null)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(EcoGreenDark)
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = 32.dp,
                        bottom = 24.dp
                    )
            ) {
                Text(
                    text = "Waste Guide",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = "Learn how to properly segregate and dispose of different types of waste.",
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        item {
            Spacer(
                modifier = Modifier.height(4.dp)
            )
        }

        item {
            Text(
                text = "HOW TO SEGREGATE YOUR WASTE",
                modifier = Modifier.padding(horizontal = 24.dp),
                color = EcoGreenDark,
                style = MaterialTheme.typography.labelMedium
            )
        }

        items(
            items = wasteCategories,
            key = {
                it.name
            }
        ) { category ->

            Box(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                WasteCategoryCard(
                    category = category,
                    expanded = expandedCategory == category.name,
                    onClick = {
                        expandedCategory =
                            if (expandedCategory == category.name) {
                                null
                            } else {
                                category.name
                            }
                    }
                )
            }
        }

        item {
            Spacer(
                modifier = Modifier.height(24.dp)
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
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = EcoWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(category.color),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = category.name,
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.width(14.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = category.name,
                        color = EcoGreenDark,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = category.description,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Icon(
                    imageVector = if (expanded) {
                        Icons.Default.ExpandLess
                    } else {
                        Icons.Default.ExpandMore
                    },
                    contentDescription = if (expanded) {
                        "Collapse"
                    } else {
                        "Expand"
                    },
                    tint = EcoGreenDark,
                    modifier = Modifier.size(26.dp)
                )
            }

            if (expanded) {

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFFE5E5E5))
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = "Examples",
                    color = EcoGreenDark,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    category.examples.forEach { example ->

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "•",
                                color = EcoGreenDark,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(16.dp)
                            )

                            Text(
                                text = example,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                Text(
                    text = "How to dispose",
                    color = EcoGreenDark,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    category.instructions.forEachIndexed { index, instruction ->

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(category.color),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(
                                modifier = Modifier.width(10.dp)
                            )

                            Text(
                                text = instruction,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}