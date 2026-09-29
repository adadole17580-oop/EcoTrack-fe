package edp.app.ecotrack.screens.waste_report

import androidx.annotation.DrawableRes
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

private val EcoGreen = Color(0xFF286F32)
private val EcoDarkGreen = Color(0xFF216B2A)
private val EcoLightGreen = Color(0xFFE6F5EA)
private val EcoTextGreen = Color(0xFF286B32)

private val White = Color.White
private val GrayText = Color(0xFF66756A)

private val PhotoGreen = Color(0xFFE4F7E9)

private val DetailYellow = Color(0xFFFFFBEB)
private val DetailBorder = Color(0xFFF2DF9A)

data class GarbageCollectorOption(
    val collectorId: String,
    val name: String,
    val assignedArea: String
)

private val availableCollectors = listOf(

    GarbageCollectorOption(
        collectorId = "COL-001",
        name = "Lito Bautista",
        assignedArea = "Brgy. Area A & B"
    ),

    GarbageCollectorOption(
        collectorId = "COL-002",
        name = "Carlos Mendoza",
        assignedArea = "Brgy. Area A & D"
    ),

    GarbageCollectorOption(
        collectorId = "COL-003",
        name = "Ramon Dela Cruz",
        assignedArea = "Brgy. Area C"
    )
)

@Composable
fun AssignReportScreen(
    report: WasteReport,
    onDismiss: () -> Unit = {},
    onAssignCollector: (GarbageCollectorOption) -> Unit = {},
    @DrawableRes residentPhotoRes: Int? = null
) {

    var selectedCollector by remember {
        mutableStateOf<GarbageCollectorOption?>(null)
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
                    text = "Assign to Garbage Collector",
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

                // =================================================
                // REPORT DETAILS
                // =================================================

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

                    DetailRow(
                        label = "Category",
                        value = report.category
                    )

                    DetailRow(
                        label = "Reported by",
                        value = report.resident
                    )

                    DetailRow(
                        label = "Location",
                        value = report.location,
                    )

                    DetailRow(
                        label = "Date",
                        value = report.date
                    )
                }

                // =================================================
                // RESIDENT PHOTO
                // =================================================

                Text(
                    text = "RESIDENT PHOTO",
                    color = EcoTextGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                ResidentPhoto(
                    photoRes = residentPhotoRes
                )

                // =================================================
                // SELECT COLLECTOR
                // =================================================

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

                // =================================================
                // COLLECTORS
                // =================================================

                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {

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
        },

        confirmButton = {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                // =================================================
                // CANCEL
                // =================================================

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

                // =================================================
                // ASSIGN COLLECTOR
                // =================================================

                Button(
                    onClick = {

                        selectedCollector?.let { collector ->
                            onAssignCollector(collector)
                        }
                    },

                    enabled = selectedCollector != null,

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
                        text = "Assign Collector",
                        fontSize = 10.sp
                    )
                }
            }
        },

        dismissButton = null
    )
}

// =============================================================
// DETAIL ROW
// =============================================================

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

// =============================================================
// RESIDENT PHOTO
// =============================================================

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

            androidx.compose.foundation.Image(
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

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = "Resident photo",
                    color = EcoGreen,
                    fontSize = 11.sp
                )
            }
        }
    }
}

// =============================================================
// COLLECTOR OPTION
// =============================================================

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
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 7.dp,
                vertical = 6.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // RADIO BUTTON

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

        Spacer(
            modifier = Modifier.width(6.dp)
        )

        // COLLECTOR AVATAR

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

        Spacer(
            modifier = Modifier.width(6.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = collector.name,
                color = EcoTextGreen,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "${collector.collectorId} · ${collector.assignedArea}",
                color = Color(0xFF58BE78),
                fontSize = 10.sp
            )
        }
    }
}
