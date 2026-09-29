package edp.app.ecotrack.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edp.app.ecotrack.components.ScheduleCard
import edp.app.ecotrack.ui.theme.EcoDarkGreen
import edp.app.ecotrack.ui.theme.EcoGreen

@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier
) {

    var remindersEnabled by remember {
        mutableStateOf(true)
    }

    Column(
        modifier = modifier
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
                text = "Collection Schedule",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = "Weekly schedule by waste type",
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline
            )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 15.dp,
                        vertical = 13.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Collection Reminders",
                        color = EcoDarkGreen,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = "Get notified the night before",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Switch(
                    checked = remindersEnabled,
                    onCheckedChange = {
                        remindersEnabled = it
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = EcoGreen,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFFBDBDBD)
                    )
                )
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "WEEKLY COLLECTION",
            modifier = Modifier.padding(horizontal = 26.dp),
            color = EcoDarkGreen,
            style = MaterialTheme.typography.labelMedium
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = "Follow the schedule for proper waste disposal.",
            modifier = Modifier.padding(horizontal = 26.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelSmall
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        ScheduleCard(
            day = "Monday",
            zone = "Zone 1 - 4",
            time = "6:00 AM - 9:00 AM",
            type = "🌿  Biodegradable"
        )

        ScheduleCard(
            day = "Wednesday",
            zone = "Zone 5 - 8",
            time = "7:00 AM - 10:00 AM",
            type = "♻  Recyclable"
        )

        ScheduleCard(
            day = "Friday",
            zone = "All Barangays",
            time = "6:00 AM - 8:00 AM",
            type = "🗑  Residual"
        )

        ScheduleCard(
            day = "Saturday",
            zone = "Barangay 3 & 6",
            time = "8:00 AM - 11:00 AM",
            type = "⚠  Hazardous"
        )

        Spacer(
            modifier = Modifier.height(25.dp)
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun ScheduleScreenPreview() {
    ScheduleScreen()
}