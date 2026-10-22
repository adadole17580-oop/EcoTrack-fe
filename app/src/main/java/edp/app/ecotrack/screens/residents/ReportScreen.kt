package edp.app.ecotrack.screens.residents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edp.app.ecotrack.components.residents.ReportCard
import edp.app.ecotrack.network.ApiClient
import edp.app.ecotrack.ui.theme.EcoDarkGreen
import edp.app.ecotrack.ui.theme.EcoGreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ReportScreen(
    accessToken: String,
    onReportClick: (Long) -> Unit = {}
) {
    var showNewReport by remember { mutableStateOf(false) }

    if (showNewReport) {
        NewReportScreen(
            accessToken = accessToken,
            onBack = {
                showNewReport = false
            }
        )
    } else {
        MyReportsScreen(
            accessToken = accessToken,
            onNewReport = {
                showNewReport = true
            },
            onReportClick = onReportClick
        )
    }
}

private data class ResidentReport(
    val title: String,
    val location: String,
    val date: String,
    val reportId: String,
    val status: String,
    val numericReportId: Long
)

@Composable
private fun MyReportsScreen(
    accessToken: String,
    onNewReport: () -> Unit,
    onReportClick: (Long) -> Unit
) {
    var reports by remember { mutableStateOf<List<ResidentReport>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }
    var refreshKey by remember { mutableStateOf(0) }

    LaunchedEffect(accessToken, refreshKey) {
        isLoading = true
        errorMessage = ""

        try {
            val result = withContext(Dispatchers.IO) {
                ApiClient.getResidentReports(accessToken)
            }

            val reportArray = result.optJSONArray("reports") ?: JSONArray()

            val inputFormatter = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss",
                Locale.US
            )

            val outputFormatter = SimpleDateFormat(
                "MMM dd, yyyy",
                Locale.getDefault()
            )

            reports = (0 until reportArray.length()).mapNotNull { index ->
                val item = reportArray.optJSONObject(index)
                    ?: return@mapNotNull null

                val reportId = item.optLong("report_id", 0L)

                if (reportId <= 0L) {
                    return@mapNotNull null
                }

                val createdAt = item.optString("created_at")

                val parsedDate = try {
                    inputFormatter.parse(createdAt.take(19))
                } catch (_: Exception) {
                    null
                }

                val formattedDate = parsedDate?.let {
                    outputFormatter.format(it)
                } ?: "Date unavailable"

                ResidentReport(
                    title = item.optString("report_type", "Waste Report"),
                    location = item.optString("location_details", "Location unavailable"),
                    date = formattedDate,
                    reportId = "#RPT-${reportId.toString().padStart(3, '0')}",
                    status = item.optString("status").ifBlank { "Pending" },
                    numericReportId = reportId
                )
            }
        } catch (exception: Exception) {
            errorMessage = exception.message ?: "Unable to load your reports."
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
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
                text = "My Reports",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Track and submit barangay waste reports",
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Button(
                onClick = onNewReport,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EcoGreen
                )
            ) {
                Text(
                    text = "+ Submit New Report",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "SUBMITTED REPORTS",
                color = EcoDarkGreen,
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            when {
                isLoading -> {
                    CircularProgressIndicator()
                }

                errorMessage.isNotBlank() -> {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { refreshKey++ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EcoGreen
                        )
                    ) {
                        Text("Retry")
                    }
                }

                reports.isEmpty() -> {
                    Text(
                        text = "You haven't submitted any reports yet.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                else -> {
                    reports.forEachIndexed { index, report ->
                        ReportCard(
                            title = report.title,
                            location = report.location,
                            date = report.date,
                            reportId = report.reportId,
                            status = report.status,
                            isResolved = report.status.equals(
                                "Resolved",
                                ignoreCase = true
                            ),
                            onClick = {
                                onReportClick(report.numericReportId)
                            }
                        )

                        if (index < reports.lastIndex) {
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}