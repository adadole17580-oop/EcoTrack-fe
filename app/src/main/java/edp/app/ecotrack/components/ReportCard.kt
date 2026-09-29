package edp.app.ecotrack.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edp.app.ecotrack.ui.theme.EcoDarkGreen
import edp.app.ecotrack.ui.theme.EcoGreen
import edp.app.ecotrack.ui.theme.EcoPendingText
import edp.app.ecotrack.ui.theme.EcoPendingYellow
import edp.app.ecotrack.ui.theme.EcoResolvedGreen
import edp.app.ecotrack.ui.theme.EcoResolvedText
import edp.app.ecotrack.ui.theme.EcoTextGreen

@Composable
fun ReportCard(
    title: String,
    location: String,
    date: String,
    reportId: String,
    status: String,
    isResolved: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 12.dp,
                top = 11.dp,
                bottom = 13.dp
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = status,
                    color = if (isResolved) {
                        EcoResolvedText
                    } else {
                        EcoPendingText
                    },
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "📍  $location",
                color = EcoTextGreen,
                style = MaterialTheme.typography.labelSmall
            )

            if (date.isNotEmpty()) {
                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "$date   $reportId",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}