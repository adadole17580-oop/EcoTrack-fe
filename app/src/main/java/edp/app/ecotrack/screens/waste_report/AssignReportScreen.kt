
package edp.app.ecotrack.screens.waste_report

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edp.app.ecotrack.network.ApiClient

private val EcoGreen = Color(0xFF286F32)
private val EcoDarkGreen = Color(0xFF216B2A)
private val EcoLightGreen = Color(0xFFE6F5EA)
private val EcoTextGreen = Color(0xFF286B32)
private val White = Color.White
private val GrayText = Color(0xFF66756A)
private val PhotoGreen = Color(0xFFE4F7E9)
private val DetailYellow = Color(0xFFFFFBEB)
private val DetailBorder = Color(0xFFF2DF9A)

@Composable
fun AssignReportScreen(
    report: WasteReport,
    accessToken: String,
    onDismiss: () -> Unit = {},
    onAssignCollector: (GarbageCollectorOption) -> Unit = {},
    @DrawableRes residentPhotoRes: Int? = null,
    buttonText: String = "Assign Collector"
) {
    var selectedCollector by remember {
        mutableStateOf<GarbageCollectorOption?>(null)
    }

    var availableCollectors by remember {
        mutableStateOf<List<GarbageCollectorOption>>(emptyList())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var collectorError by remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(accessToken) {
        isLoading = true
        collectorError = null

        try {
            val response = ApiClient.getCollectors(accessToken)
            val collectorArray = response.optJSONArray("collectors")
            val loadedCollectors = mutableListOf<GarbageCollectorOption>()

            if (collectorArray != null) {
                for (i in 0 until collectorArray.length()) {
                    val item = collectorArray.optJSONObject(i) ?: continue

                    if (!item.optBoolean("is_active", false)) {
                        continue
                    }

                    val userId = item.optString("user_id", "")

                    if (userId.toIntOrNull() == null) {
                        continue
                    }

                    val fullName = item.optString(
                        "full_name",
                        "Unknown Collector"
                    )

                    val barangayName = item
                        .optJSONObject("barangays")
                        ?.optString("barangay_name")
                        ?.takeIf {
                            it.isNotBlank() && it != "null"
                        } ?: "Unassigned Area"

                    loadedCollectors.add(
                        GarbageCollectorOption(
                            collectorId = userId,
                            name = fullName,
                            assignedArea = barangayName
                        )
                    )
                }
            }

            availableCollectors = loadedCollectors
        } catch (e: Exception) {
            collectorError = e.message
                ?: "Could not load collectors. Please try again."
        } finally {
            isLoading = false
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = White,
        shape = RoundedCornerShape(14.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (buttonText == "Re-assign Collector") {
                        "Re-assign Garbage Collector"
                    } else {
                        "Assign to Garbage Collector"
                    },
                    color = EcoDarkGreen,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(26.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.Black,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "REPORT DETAILS",
                    color = EcoTextGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = DetailYellow,
                            shape = RoundedCornerShape(7.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = DetailBorder,
                            shape = RoundedCornerShape(7.dp)
                        )
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    DetailRow("Report ID", report.reportId)
                    DetailRow("Category", report.category)
                    DetailRow("Reported by", report.resident)
                    DetailRow("Location", report.location)
                    DetailRow("Date", report.date)
                }

                Text(
                    text = "RESIDENT PHOTO",
                    color = EcoTextGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                ResidentPhoto(photoRes = residentPhotoRes)

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Select Garbage Collector",
                        color = EcoTextGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = " *",
                        color = Color(0xFFD6323C),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    when {
                        isLoading -> {
                            Text(
                                text = "Loading collectors...",
                                color = GrayText,
                                fontSize = 11.sp
                            )
                        }

                        collectorError != null -> {
                            Text(
                                text = collectorError
                                    ?: "Unable to load collectors.",
                                color = Color.Red,
                                fontSize = 11.sp
                            )
                        }

                        availableCollectors.isEmpty() -> {
                            Text(
                                text = "No active garbage collectors found.",
                                color = GrayText,
                                fontSize = 11.sp
                            )
                        }

                        else -> {
                            availableCollectors.forEach { collector ->
                                CollectorOption(
                                    collector = collector,
                                    selected = selectedCollector == collector,
                                    onClick = {
                                        selectedCollector = collector
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(30.dp)
                ) {
                    Text(
                        text = "Cancel",
                        color = EcoGreen,
                        fontSize = 10.sp
                    )
                }

                Button(
                    onClick = {
                        selectedCollector?.let { collector ->
                            onAssignCollector(collector)
                        }
                    },
                    enabled = selectedCollector != null &&
                            !isLoading &&
                            collectorError == null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EcoGreen,
                        disabledContainerColor = Color(0xFFB7CDBA)
                    ),
                    shape = RoundedCornerShape(9.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(30.dp)
                ) {
                    Text(
                        text = buttonText,
                        fontSize = 10.sp
                    )
                }
            }
        },
        dismissButton = null
    )
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label: ",
            color = EcoTextGreen,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = value,
            color = EcoTextGreen,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun ResidentPhoto(
    @DrawableRes photoRes: Int?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(RoundedCornerShape(7.dp))
            .background(PhotoGreen),
        contentAlignment = Alignment.Center
    ) {
        if (photoRes != null) {
            Image(
                painter = painterResource(id = photoRes),
                contentDescription = "Resident photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .clip(RoundedCornerShape(7.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Resident photo",
                    tint = EcoGreen,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = "Resident photo",
                    color = EcoGreen,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun CollectorOption(
    collector: GarbageCollectorOption,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = White,
                shape = RoundedCornerShape(7.dp)
            )
            .border(
                width = 1.dp,
                color = if (selected) {
                    EcoGreen
                } else {
                    Color(0xFFE2E8E3)
                },
                shape = RoundedCornerShape(7.dp)
            )
            .clickable { onClick() }
            .padding(
                horizontal = 7.dp,
                vertical = 6.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(13.dp)
                .border(
                    width = 1.dp,
                    color = if (selected) {
                        EcoGreen
                    } else {
                        Color(0xFF9AA69D)
                    },
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .background(
                            color = EcoGreen,
                            shape = CircleShape
                        )
                )
            }
        }

        Spacer(modifier = Modifier.width(6.dp))

        Box(
            modifier = Modifier
                .size(20.dp)
                .background(
                    color = EcoLightGreen,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = EcoGreen,
                modifier = Modifier.size(11.dp)
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = collector.name,
                color = EcoTextGreen,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )

            val displayId = collector.collectorId.toIntOrNull()
                ?.let {
                    "COL-${it.toString().padStart(3, '0')}"
                } ?: collector.collectorId

            Text(
                text = "$displayId · ${collector.assignedArea}",
                color = Color(0xFF58BE78),
                fontSize = 10.sp
            )
        }
    }
}
