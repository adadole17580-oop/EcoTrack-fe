package edp.app.ecotrack.screens.residents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import edp.app.ecotrack.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException

private val ReportGreen = Color(0xFF24652A)
private val ReportLightGreen = Color(0xFFE0F4E8)
private val ReportTextGreen = Color(0xFF3E9E68)
private val ReportTextDark = Color(0xFF26332A)
private val ReportMuted = Color(0xFF788078)

@Composable
fun ReportDetailsScreen(
    accessToken: String,
    reportId: Long,
    onDismiss: () -> Unit,
    onReportUpdated: () -> Unit = {}
) {
    var report by remember(reportId) { mutableStateOf<JSONObject?>(null) }
    var isLoading by remember(reportId) { mutableStateOf(true) }
    var errorMessage by remember(reportId) { mutableStateOf<String?>(null) }
    var showCancelConfirmation by remember { mutableStateOf(false) }
    var isCancelling by remember { mutableStateOf(false) }
    var cancelError by remember { mutableStateOf<String?>(null) }
    var showFullScreenPhoto by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(accessToken, reportId) {
        isLoading = true
        errorMessage = null

        try {
            val response = withContext(Dispatchers.IO) {
                ApiClient.getResidentReport(accessToken, reportId)
            }

            report = response.optJSONObject("report")
                ?: throw IOException("Report details were not found.")
        } catch (error: Exception) {
            errorMessage = error.message ?: "Unable to load report details."
        } finally {
            isLoading = false
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 650.dp),
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            shadowElevation = 12.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp, vertical = 18.dp)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(42.dp)
                        .height(5.dp)
                        .clip(CircleShape)
                        .background(ReportLightGreen)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Report Details",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = ReportGreen
                        )
                        Text(
                            text = "View your submitted report",
                            fontSize = 13.sp,
                            color = ReportMuted
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close report details",
                            tint = ReportGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 44.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = ReportGreen)
                        }
                    }

                    errorMessage != null -> {
                        Text(
                            text = errorMessage ?: "Unable to load report details.",
                            color = Color(0xFFB3261E),
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        TextButton(onClick = onDismiss) {
                            Text("Close", color = ReportGreen)
                        }
                    }

                    report != null -> {
                        val item = report!!
                        val reportType = item.optString("report_type", "Waste Report")
                        val description = item.optString("description", "")
                        val location = item.optString("location_details", "")
                        val status = item.optString("status", "Pending")
                            .replace("_", " ")
                            .replaceFirstChar { it.uppercase() }
                        val createdAt = item.optString("created_at", "")
                        val photoUrl = item.optString("photo_url", "")
                        val id = item.optLong("report_id", reportId)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = reportType,
                                modifier = Modifier.weight(1f),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = ReportTextDark
                            )

                            Surface(
                                shape = RoundedCornerShape(50),
                                color = when (status.lowercase()) {
                                    "resolved" -> Color(0xFFE0F4E8)
                                    "rejected", "cancelled" -> Color(0xFFFCE8E6)
                                    else -> Color(0xFFFFF3D6)
                                }
                            ) {
                                Text(
                                    text = status,
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 7.dp
                                    ),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = when (status.lowercase()) {
                                        "resolved" -> ReportGreen
                                        "rejected", "cancelled" -> Color(0xFFB3261E)
                                        else -> Color(0xFF946200)
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Report #$id",
                            fontSize = 12.sp,
                            color = ReportMuted
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        if (photoUrl.isNotBlank() && photoUrl != "null") {
                            var photoLoadFailed by remember(photoUrl) {
                                mutableStateOf(false)
                            }

                            if (!photoLoadFailed) {
                                AsyncImage(
                                    model = photoUrl,
                                    contentDescription = "Uploaded report photo",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = 160.dp, max = 260.dp)
                                        .clip(RoundedCornerShape(18.dp))
                                        .clickable { showFullScreenPhoto = true },
                                    contentScale = ContentScale.Crop,
                                    onError = {
                                        photoLoadFailed = true
                                    }
                                )
                            } else {
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp),
                                    shape = RoundedCornerShape(18.dp),
                                    color = ReportLightGreen
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "Unable to load report photo",
                                            color = ReportGreen,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                        }

                        DetailSection(
                            title = "Description",
                            value = description.ifBlank { "No description provided." }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        DetailSection(
                            title = "Location",
                            value = location.ifBlank { "No location provided." }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        DetailSection(
                            title = "Date Submitted",
                            value = createdAt
                                .takeIf { it.isNotBlank() && it != "null" }
                                ?.replace("T", " ")
                                ?.take(16)
                                ?: "Not available"
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            color = ReportLightGreen
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(ReportTextGreen)
                                )

                                Spacer(modifier = Modifier.width(10.dp))

                                Text(
                                    text = "Keep this report for your records.",
                                    fontSize = 13.sp,
                                    color = ReportGreen
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        if (status.lowercase() == "pending") {
                            OutlinedButton(
                                onClick = { showCancelConfirmation = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFFB3261E)
                                ),
                                border = BorderStroke(1.dp, Color(0xFFB3261E))
                            ) {
                                Text(
                                    text = "Cancel Report",
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(
                                onClick = onDismiss,
                                modifier = Modifier
                                    .fillMaxWidth(0.75f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ReportGreen)
                            ) {
                                Text(
                                    text = "Close",
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showCancelConfirmation) {
        AlertDialog(
            onDismissRequest = {
                if (!isCancelling) showCancelConfirmation = false
            },
            title = { Text("Cancel Report") },
            text = {
                Column {
                    Text("Are you sure you want to cancel this pending report? This action cannot be undone.")
                    if (cancelError != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(cancelError!!, color = Color(0xFFB3261E))
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            isCancelling = true
                            cancelError = null
                            try {
                                val result = withContext(Dispatchers.IO) {
                                    ApiClient.cancelResidentReport(accessToken, reportId)
                                }
                                report = result.optJSONObject("report") ?: report
                                showCancelConfirmation = false
                                onReportUpdated()
                            } catch (e: Exception) {
                                cancelError = e.message ?: "Unable to cancel the report."
                            } finally {
                                isCancelling = false
                            }
                        }
                    },
                    enabled = !isCancelling
                ) {
                    if (isCancelling) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color(0xFFB3261E)
                        )
                    } else {
                        Text("Yes, Cancel", color = Color(0xFFB3261E))
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showCancelConfirmation = false },
                    enabled = !isCancelling
                ) {
                    Text("Keep Report", color = ReportGreen)
                }
            }
        )
    }

    val currentPhotoUrl = report?.optString("photo_url") ?: ""
    if (showFullScreenPhoto && currentPhotoUrl.isNotBlank() && currentPhotoUrl != "null") {
        FullScreenPhotoViewer(
            photoUrl = currentPhotoUrl,
            onDismiss = { showFullScreenPhoto = false }
        )
    }
}

@Composable
private fun FullScreenPhotoViewer(
    photoUrl: String,
    onDismiss: () -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(1f, 5f)
                        if (scale == 1f) {
                            offsetX = 0f
                            offsetY = 0f
                        } else {
                            offsetX += pan.x
                            offsetY += pan.y
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = photoUrl,
                contentDescription = "Full-screen report photo",
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offsetX,
                        translationY = offsetY
                    ),
                contentScale = ContentScale.Fit
            )

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close full-screen view",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun DetailSection(
    title: String,
    value: String
) {
    Column {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = ReportTextGreen
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = value,
            fontSize = 14.sp,
            lineHeight = 21.sp,
            color = ReportTextDark
        )
    }
}
