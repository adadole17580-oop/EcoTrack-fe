
package edp.app.ecotrack.screens.waste_report

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

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

private val RejectRed = Color(0xFFDC3545)

/*
 * View Waste Report popup.
 *
 * Pending:
 * Reject, Assign
 *
 * In Progress:
 * Reject, Edit
 *
 * For Verification:
 * Return to In Progress, Accept & Resolve
 *
 * Resolved, Cancelled, Rejected:
 * Viewing only
 */

@Composable
fun ViewReportScreen(
    report: WasteReport,
    onDismiss: () -> Unit = {},
    @DrawableRes residentPhotoRes: Int? = null,
    @DrawableRes collectorPhotoRes: Int? = null,
    dateResolved: String? = null,
    onReject: () -> Unit = {},
    onAssign: () -> Unit = {},
    onEdit: () -> Unit = {},
    onReturnToProgress: () -> Unit = {},
    onAcceptResolve: () -> Unit = {},
    onSendForVerification: () -> Unit = {}
) {
    val status = report.status.trim()

    val isResolved = status.equals("Resolved", ignoreCase = true)
    val isPending = status.equals("Pending", ignoreCase = true)
    val isInProgress = status.equals("In Progress", ignoreCase = true)

    val isForVerification =
        status.equals("For Verification", ignoreCase = true) ||
                status.equals("Waiting for Verification", ignoreCase = true)

    val isViewingOnly =
        isResolved ||
                status.equals("Cancelled", ignoreCase = true) ||
                status.equals("Rejected", ignoreCase = true)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = White,
        shape = RoundedCornerShape(7.dp),

        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = report.reportId,
                        color = EcoGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Improper Waste Disposal",
                        color = EcoDarkGreen,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    StatusBadge(status = status)
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close report",
                        tint = EcoDarkGreen
                    )
                }
            }
        },

        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // REPORT INFORMATION
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
                    verticalArrangement = Arrangement.spacedBy(6.dp)
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

                // REPORT PHOTOS
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
                            modifier = Modifier.weight(1f),
                            photoUrl = report.photoUrl
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
                        modifier = Modifier.fillMaxWidth(),
                        photoUrl = report.photoUrl
                    )
                }

                // REPORT HISTORY
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

                // STATUS-SPECIFIC ACTIONS
                if (isViewingOnly) {
                    Text(
                        text = "Viewing only",
                        color = GrayText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        when {
                            isPending -> {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    ReportActionButton(
                                        text = "Reject",
                                        backgroundColor = RejectRed,
                                        modifier = Modifier.weight(1f),
                                        onClick = onReject
                                    )

                                    ReportActionButton(
                                        text = "Assign",
                                        backgroundColor = EcoGreen,
                                        modifier = Modifier.weight(1f),
                                        onClick = onAssign
                                    )
                                }
                            }

                            isInProgress -> {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    ReportActionButton(
                                        text = "Reject",
                                        backgroundColor = RejectRed,
                                        modifier = Modifier.weight(1f),
                                        onClick = onReject
                                    )

                                    ReportActionButton(
                                        text = "Edit",
                                        backgroundColor = EcoGreen,
                                        modifier = Modifier.weight(1f),
                                        onClick = onEdit
                                    )
                                }
                            }

                            isForVerification -> {
                                ReportActionButton(
                                    text = "Return to In Progress",
                                    backgroundColor = EcoGreen,
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = onReturnToProgress
                                )

                                ReportActionButton(
                                    text = "Accept & Resolve",
                                    backgroundColor = EcoGreen,
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = onAcceptResolve
                                )
                            }
                        }
                    }
                }
            }
        },

        confirmButton = {},
        dismissButton = {}
    )
}

// STATUS BADGE
@Composable
private fun StatusBadge(status: String) {
    val backgroundColor: Color
    val textColor: Color

    when (status.lowercase()) {
        "resolved" -> {
            backgroundColor = ResolvedBg
            textColor = ResolvedText
        }

        "in progress" -> {
            backgroundColor = InProgressBg
            textColor = InProgressText
        }

        "for verification", "waiting for verification" -> {
            backgroundColor = Color(0xFFFFD0D0)
            textColor = Color(0xFFE56A6A)
        }

        "cancelled" -> {
            backgroundColor = Color(0xFFE5E5E5)
            textColor = Color(0xFF666666)
        }

        "rejected" -> {
            backgroundColor = Color(0xFFFFDADA)
            textColor = RejectRed
        }

        else -> {
            backgroundColor = Color(0xFFF1DFAF)
            textColor = Color(0xFFC0922D)
        }
    }

    Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
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

// INFORMATION ROW
@Composable
private fun ViewInfoRow(
    icon: ImageVector,
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
            modifier = Modifier.size(14.dp)
        )

        Spacer(modifier = Modifier.width(6.dp))

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

// PHOTO
@Composable
private fun ViewPhoto(
    title: String,
    @DrawableRes photoRes: Int?,
    backgroundColor: Color,
    modifier: Modifier,
    photoUrl: String? = null
) {
    Box(
        modifier = modifier
            .height(150.dp)
            .clip(RoundedCornerShape(7.dp))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        when {
            !photoUrl.isNullOrBlank() -> {
                AsyncImage(
                    model = photoUrl,
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(7.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            photoRes != null -> {
                Image(
                    painter = painterResource(id = photoRes),
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(7.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            else -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )

                    Text(
                        text = "$title photo unavailable",
                        color = Color.DarkGray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

// REPORT HISTORY
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
            last = isResolved
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

// HISTORY ITEM
@Composable
private fun HistoryItem(
    title: String,
    detail: String,
    icon: ImageVector,
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
                    .size(14.dp)
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
                    modifier = Modifier.size(9.dp)
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

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.padding(bottom = 5.dp)
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

// ACTION BUTTON
@Composable
private fun ReportActionButton(
    text: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(38.dp),
        shape = RoundedCornerShape(9.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = White
        ),
        contentPadding = PaddingValues(
            horizontal = 8.dp,
            vertical = 4.dp
        )
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
