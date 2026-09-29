package edp.app.ecotrack.screens

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edp.app.ecotrack.components.ReportCard
import edp.app.ecotrack.ui.theme.EcoDarkGreen
import edp.app.ecotrack.ui.theme.EcoGreen

@Composable
fun ReportScreen() {

    var showNewReport by remember {
        mutableStateOf(false)
    }

    if (showNewReport) {

        NewReportScreen(
            onBack = {
                showNewReport = false
            }
        )

    } else {

        MyReportsScreen(
            onNewReport = {
                showNewReport = true
            }
        )
    }
}

@Composable
private fun MyReportsScreen(
    onNewReport: () -> Unit
) {

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

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = "Track and submit barangay waste reports",
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

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

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Text(
                text = "SUBMITTED REPORTS",
                color = EcoDarkGreen,
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            ReportCard(
                title = "Illegal Dumping",
                location = "Zone 3, Bulua",
                date = "Sep 12, 2026",
                reportId = "#RPT-001",
                status = "Pending",
                isResolved = false
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            ReportCard(
                title = "Overflowing Bin",
                location = "Zone 2, Bulua",
                date = "Sep 10, 2026",
                reportId = "#RPT-002",
                status = "Resolved",
                isResolved = true
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            ReportCard(
                title = "Uncollected Waste",
                location = "Zone 10, Bulua",
                date = "Sep 05, 2026",
                reportId = "#RPT-003",
                status = "Resolved",
                isResolved = true
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )
        }
    }
}
