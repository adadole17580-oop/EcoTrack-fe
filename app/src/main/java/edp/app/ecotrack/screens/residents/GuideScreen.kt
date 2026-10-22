package edp.app.ecotrack.screens.residents

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edp.app.ecotrack.data.repository.GuideRepository
import edp.app.ecotrack.data.model.GuideItem
import edp.app.ecotrack.ui.theme.EcoBiodegradable
import edp.app.ecotrack.ui.theme.EcoDarkGreen
import edp.app.ecotrack.ui.theme.EcoHazardous
import edp.app.ecotrack.ui.theme.EcoRecyclable
import edp.app.ecotrack.ui.theme.EcoResidual
import edp.app.ecotrack.ui.theme.EcoWhite

@Composable
fun GuideScreen() {
    var guides by remember { mutableStateOf<List<GuideItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var expandedGuideId by remember { mutableStateOf<Int?>(null) }
    var refreshKey by remember { mutableStateOf(0) }

    LaunchedEffect(refreshKey) {
        isLoading = true
        errorMessage = null

        val result = GuideRepository.getGuides()

        result.onSuccess {
            guides = it
        }.onFailure {
            errorMessage = it.message ?: "Unable to load the waste segregation guide."
        }

        isLoading = false
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
                    .background(EcoDarkGreen)
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

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Learn how to properly segregate and dispose of different types of waste.",
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(4.dp))
        }

        item {
            Text(
                text = "HOW TO SEGREGATE YOUR WASTE",
                modifier = Modifier.padding(horizontal = 24.dp),
                color = EcoDarkGreen,
                style = MaterialTheme.typography.labelMedium
            )
        }

        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = EcoDarkGreen)
                }
            }
        } else if (errorMessage != null) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = errorMessage ?: "Unable to load the guide.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { refreshKey++ }
                    ) {
                        Text("Try Again")
                    }
                }
            }
        } else if (guides.isEmpty()) {
            item {
                Text(
                    text = "No waste guide information is available yet.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 24.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            items(
                items = guides,
                key = { it.guideId }
            ) { guide ->
                Box(
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    GuideItemCard(
                        guide = guide,
                        expanded = expandedGuideId == guide.guideId,
                        onClick = {
                            expandedGuideId =
                                if (expandedGuideId == guide.guideId) {
                                    null
                                } else {
                                    guide.guideId
                                }
                        }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun GuideItemCard(
    guide: GuideItem,
    expanded: Boolean,
    onClick: () -> Unit
) {
    val wasteType = guide.wasteType.trim()

    val categoryColor = when (wasteType.lowercase()) {
        "biodegradable" -> EcoBiodegradable
        "recyclable" -> EcoRecyclable
        "residual", "non-biodegradable" -> EcoResidual
        "hazardous" -> EcoHazardous
        else -> EcoDarkGreen
    }

    val categoryIcon: ImageVector = when (wasteType.lowercase()) {
        "biodegradable" -> Icons.Default.LocalDining
        "recyclable" -> Icons.Default.Recycling
        "residual", "non-biodegradable" -> Icons.Default.DeleteOutline
        "hazardous" -> Icons.Default.Warning
        else -> Icons.Default.Recycling
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
                        .background(categoryColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = categoryIcon,
                        contentDescription = wasteType,
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = wasteType,
                        color = EcoDarkGreen,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = guide.description,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = if (expanded) {
                        Icons.Default.ExpandLess
                    } else {
                        Icons.Default.ExpandMore
                    },
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = EcoDarkGreen,
                    modifier = Modifier.size(26.dp)
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFFE5E5E5))
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "How to dispose",
                    color = EcoDarkGreen,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = guide.disposalMethod,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
