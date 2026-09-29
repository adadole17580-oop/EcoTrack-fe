package edp.app.ecotrack.screens.waste_report

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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

private val RejectRed = Color(0xFFE56A6A)

/**
 * Review Collector Proof popup.
 *
 * residentPhotoRes:
 *      Optional local resident photo.
 *
 * collectorPhotoRes:
 *      Optional local collector proof photo.
 *
 * Both are optional so the frontend can compile before
 * the real backend image URLs are connected.
 */
@Composable
fun ReviewReportScreen(
    report: WasteReport,
    onDismiss: () -> Unit = {},
    onRejectProof: () -> Unit = {},
    onApproveReport: () -> Unit = {},
    @DrawableRes residentPhotoRes: Int? = null,
    @DrawableRes collectorPhotoRes: Int? = null
) {

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
                    text = "Review Collector Proof",
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
                // REPORT SUMMARY
                // =================================================

                Text(
                    text = "REPORT SUMMARY",
                    color = EcoTextGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

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

                    SummaryRow(
                        label = "Report ID",
                        value = report.reportId
                    )

                    SummaryRow(
                        label = "Report Type",
                        value = report.category
                    )

                    SummaryRow(
                        label = "Resident",
                        value = report.resident
                    )

                    SummaryRow(
                        label = "Location",
                        value = report.location
                    )

                    SummaryRow(
                        label = "Assigned Collector",
                        value = report.assignedTo
                    )
                }

                // =================================================
                // BEFORE & AFTER PHOTOS
                // =================================================

                Text(
                    text = "BEFORE & AFTER PHOTOS",
                    color = EcoTextGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    ProofPhoto(
                        title = "Before",
                        photoRes = residentPhotoRes,
                        backgroundColor = PhotoGreen,
                        modifier = Modifier.weight(1f)
                    )

                    ProofPhoto(
                        title = "After",
                        photoRes = collectorPhotoRes,
                        backgroundColor = PhotoBlue,
                        modifier = Modifier.weight(1f)
                    )
                }

                // =================================================
                // ACTIVITY TIMELINE
                // =================================================

                Text(
                    text = "ACTIVITY TIMELINE",
                    color = EcoTextGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                ActivityTimeline(
                    report = report
                )
            }
        },

        // =========================================================
        // ACTION BUTTONS
        // =========================================================

        confirmButton = {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                // -------------------------------------------------
                // REJECT PROOF
                // -------------------------------------------------

                OutlinedButton(
                    onClick = onRejectProof,
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = RejectRed
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = RejectRed
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp
                    )
                ) {

                    Text(
                        text = "Reject Proof",
                        fontSize = 10.sp
                    )
                }

                // -------------------------------------------------
                // APPROVE REPORT
                // -------------------------------------------------

                Button(
                    onClick = onApproveReport,
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

                    Text(
                        text = "Approve Report",
                        fontSize = 10.sp
                    )
                }
            }
        },

        dismissButton = null
    )
}

// =============================================================
// SUMMARY ROW
// =============================================================

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    showLocationIcon: Boolean = false
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (showLocationIcon) {

            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = EcoGreen,
                modifier = Modifier.size(9.dp)
            )

            Spacer(
                modifier = Modifier.width(3.dp)
            )
        }

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
private fun ProofPhoto(
    title: String,
    @DrawableRes photoRes: Int?,
    backgroundColor: Color,
    modifier: Modifier
) {

    Box(
        modifier = modifier
            .height(68.dp)
            .clip(RoundedCornerShape(7.dp))
            .background(backgroundColor)
            .clickable {
                // Reserved for photo viewer later.
            },
        contentAlignment = Alignment.Center
    ) {

        if (photoRes != null) {

            androidx.compose.foundation.Image(
                painter = painterResource(id = photoRes),
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
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
// ACTIVITY TIMELINE
// =============================================================

@Composable
private fun ActivityTimeline(
    report: WasteReport
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        TimelineItem(
            title = "Report submitted",
            detail = "${report.resident} submitted this report",
            icon = Icons.Default.Assignment,
            last = false
        )

        TimelineItem(
            title = "Collector assigned",
            detail = report.assignedTo,
            icon = Icons.Default.Person,
            last = false
        )

        TimelineItem(
            title = "Collection task started",
            detail = "Status moved to In Progress",
            icon = Icons.Default.Assignment,
            last = false
        )

        TimelineItem(
            title = "Proof photo uploaded",
            detail = "Collector submitted proof",
            icon = Icons.Default.Image,
            last = false
        )

        TimelineItem(
            title = "For Verification",
            detail = "Awaiting administrator review",
            icon = Icons.Default.CheckCircle,
            last = true
        )
    }
}

// =============================================================
// TIMELINE ITEM
// =============================================================

@Composable
private fun TimelineItem(
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
                    .size(12.dp)
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
                    modifier = Modifier.size(6.dp)
                )
            }

            if (!last) {

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(22.dp)
                        .background(EcoBorder)
                )
            }
        }

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Column(
            modifier = Modifier.padding(bottom = 6.dp)
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
                fontSize = 9.sp
            )
        }
    }
}