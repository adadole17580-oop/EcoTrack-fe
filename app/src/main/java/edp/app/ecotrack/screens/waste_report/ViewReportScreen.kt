package edp.app.ecotrack.screens.waste_report

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
private val EcoBorder = Color(0xFFB7DCC0)
private val EcoTextGreen = Color(0xFF286B32)

private val White = Color.White
private val GrayText = Color(0xFF66756A)

private val PhotoGreen = Color(0xFFDFF4E6)
private val PhotoBlue = Color(0xFFDDF5F7)

private val InProgressBg = Color(0xFFD0F1D7)
private val InProgressText = Color(0xFF39924A)

private val ResolvedBg = Color(0xFFC5DEC9)
private val ResolvedText = Color(0xFF3C8550)

/**
 * View Waste Report popup.
 *
 * In Progress:
 * - Resident information
 * - Before/resident photo
 * - Report history
 *
 * Resolved:
 * - Resident information
 * - Before/resident photo
 * - After/collector photo
 * - Report history
 * - Date Resolved
 */
@Composable
fun ViewReportScreen(
    report: WasteReport,
    onDismiss: () -> Unit = {},
    @DrawableRes residentPhotoRes: Int? = null,
    @DrawableRes collectorPhotoRes: Int? = null,
    dateResolved: String? = null
) {

    val isResolved = report.status == "Resolved"

    AlertDialog(
        onDismissRequest = onDismiss,

        containerColor = White,

        shape = RoundedCornerShape(7.dp),

        title = {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = report.reportId,
                        color = EcoGreen,
                        fontSize = 7.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Improper Waste Disposal",
                        color = EcoDarkGreen,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                StatusBadge(
                    status = report.status
                )

                Spacer(
                    modifier = Modifier.width(5.dp)
                )
            }
        },

        text = {

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                // =================================================
                // REPORT INFORMATION
                // =================================================

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = EcoLightGreen,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color(0xFFD5EBDD),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(9.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    ViewInfoRow(
                        icon = Icons.Default.Assignment,
                        label = "Report ID",
                        value = report.reportId
                    )

                    ViewInfoRow(
                        icon = Icons.Default.Person,
                        label = "Resident",
                        value = report.resident
                    )

                    ViewInfoRow(
                        icon = Icons.Default.Person,
                        label = "Contact",
                        value = "Contact information"
                    )

                    ViewInfoRow(
                        icon = Icons.Default.Assignment,
                        label = "Category",
                        value = report.category
                    )

                    ViewInfoRow(
                        icon = Icons.Default.LocationOn,
                        label = "Location",
                        value = report.location
                    )

                    ViewInfoRow(
                        icon = Icons.Default.Assignment,
                        label = "Date Submitted",
                        value = report.date
                    )

                    ViewInfoRow(
                        icon = Icons.Default.Person,
                        label = "Assigned Collector",
                        value = report.assignedTo
                    )

                    if (isResolved && !dateResolved.isNullOrBlank()) {

                        ViewInfoRow(
                            icon = Icons.Default.CheckCircle,
                            label = "Date Resolved",
                            value = dateResolved
                        )
                    }
                }

                // =================================================
                // PHOTOS
                // =================================================

                Text(
                    text = if (isResolved) {
                        "BEFORE & AFTER PHOTOS"
                    } else {
                        "BEFORE PHOTO"
                    },
                    color = EcoTextGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                if (isResolved) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        ViewPhoto(
                            title = "Before",
                            photoRes = residentPhotoRes,
                            backgroundColor = PhotoGreen,
                            modifier = Modifier.weight(1f)
                        )

                        ViewPhoto(
                            title = "After",
                            photoRes = collectorPhotoRes,
                            backgroundColor = PhotoBlue,
                            modifier = Modifier.weight(1f)
                        )
                    }

                } else {

                    ViewPhoto(
                        title = "Before",
                        photoRes = residentPhotoRes,
                        backgroundColor = PhotoGreen,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // =================================================
                // REPORT HISTORY
                // =================================================

                Text(
                    text = "REPORT HISTORY",
                    color = EcoTextGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                ReportHistory(
                    report = report,
                    isResolved = isResolved,
                    dateResolved = dateResolved
                )
            }
        },

        confirmButton = {

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = White
                ),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = EcoGreen
                ),
                shape = RoundedCornerShape(9.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
            ) {

                Text(
                    text = "Close",
                    color = EcoGreen,
                    fontSize = 10.sp
                )
            }
        }
    )
}

// =============================================================
// STATUS BADGE
// =============================================================

@Composable
private fun StatusBadge(
    status: String
) {

    val background: Color
    val textColor: Color

    when (status) {

        "Resolved" -> {
            background = ResolvedBg
            textColor = ResolvedText
        }

        "In Progress" -> {
            background = InProgressBg
            textColor = InProgressText
        }

        else -> {
            background = Color(0xFFF1DFAF)
            textColor = Color(0xFFC0922D)
        }
    }

    Box(
        modifier = Modifier
            .background(
                color = background,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(
                horizontal = 7.dp,
                vertical = 3.dp
            )
    ) {

        Text(
            text = status,
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// =============================================================
// INFORMATION ROW
// =============================================================

@Composable
private fun ViewInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = EcoGreen,
            modifier = Modifier.size(9.dp)
        )

        Spacer(
            modifier = Modifier.width(4.dp)
        )

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
// PHOTO
// =============================================================
@Composable
private fun ViewPhoto(
    title: String,
    @DrawableRes photoRes: Int?,
    backgroundColor: Color,
    modifier: Modifier
) {

    Box(
        modifier = modifier
            .height(45.dp)
            .clip(RoundedCornerShape(7.dp))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {

        if (photoRes != null) {

            androidx.compose.foundation.Image(
                painter = painterResource(id = photoRes),
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
                    .clip(RoundedCornerShape(7.dp)),
                contentScale = ContentScale.Crop
            )

        } else {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = title,
                    tint = EcoGreen,
                    modifier = Modifier.size(15.dp)
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = title,
                    color = EcoGreen,
                    fontSize = 10.sp
                )
            }
        }
    }
}

// =============================================================
// REPORT HISTORY
// =============================================================

@Composable
private fun ReportHistory(
    report: WasteReport,
    isResolved: Boolean,
    dateResolved: String?
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        HistoryItem(
            title = "Report submitted by ${report.resident}",
            detail = report.date,
            icon = Icons.Default.Assignment,
            last = false
        )

        HistoryItem(
            title = "Assigned to ${report.assignedTo}",
            detail = "Collector assigned",
            icon = Icons.Default.Person,
            last = false
        )

        HistoryItem(
            title = "Task started — status moved to In Progress",
            detail = "Collection task started",
            icon = Icons.Default.Assignment,
            last = false
        )

        HistoryItem(
            title = "Proof photo uploaded by collector",
            detail = "Collector submitted proof",
            icon = Icons.Default.Image,
            last = false
        )

        HistoryItem(
            title = "Status moved to For Verification",
            detail = "Awaiting administrator review",
            icon = Icons.Default.CheckCircle,
            last = isResolved.not()
        )

        if (isResolved) {

            HistoryItem(
                title = "Status moved to Resolved",
                detail = dateResolved ?: "Report resolved",
                icon = Icons.Default.CheckCircle,
                last = true
            )
        }
    }
}

// =============================================================
// HISTORY ITEM
// =============================================================

@Composable
private fun HistoryItem(
    title: String,
    detail: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    last: Boolean
) {

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(9.dp)
                    .background(
                        color = EcoGreen,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier.size(5.dp)
                )
            }

            if (!last) {

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(18.dp)
                        .background(EcoBorder)
                )
            }
        }

        Spacer(
            modifier = Modifier.width(6.dp)
        )

        Column(
            modifier = Modifier.padding(bottom = 4.dp)
        ) {

            Text(
                text = title,
                color = EcoTextGreen,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = detail,
                color = GrayText,
                fontSize = 10.sp
            )
        }
    }
}