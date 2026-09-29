package edp.app.ecotrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Recycling
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edp.app.ecotrack.ui.components.EcoTrackTopBar
import edp.app.ecotrack.ui.theme.EcoBackground
import edp.app.ecotrack.ui.theme.EcoGreen
import edp.app.ecotrack.ui.theme.EcoGreenLight
import edp.app.ecotrack.ui.theme.EcoSecondaryText
import edp.app.ecotrack.ui.theme.EcoText
import edp.app.ecotrack.ui.theme.EcoWhite

private data class CollectionDay(
    val day: String,
    val date: String,
    val area: String,
    val wasteType: String,
    val time: String
)

@Composable
fun CollectionScheduleScreen() {

    var remindersEnabled by remember {
        mutableStateOf(false)
    }

    val schedule = listOf(
        CollectionDay(
            "Monday",
            "Sept 28",
            "Barangay Bulua",
            "Biodegradable",
            "8:00 – 10:00 AM"
        ),
        CollectionDay(
            "Tuesday",
            "Sept 29",
            "Barangay Bulua",
            "Recyclable",
            "9:00 – 11:00 AM"
        ),
        CollectionDay(
            "Wednesday",
            "Sept 30",
            "Barangay Bulua",
            "Non-Biodegradable",
            "8:30 – 10:30 AM"
        ),
        CollectionDay(
            "Thursday",
            "Oct 1",
            "Barangay Bulua",
            "Food Waste",
            "10:00 AM – 12:00 PM"
        ),
        CollectionDay(
            "Friday",
            "Oct 2",
            "Barangay Bulua",
            "Mixed Collection",
            "8:00 – 10:00 AM"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoBackground)
    ) {

        EcoTrackTopBar(
            title = "Collection Schedule",
            subtitle = "Your weekly collection schedule"
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(
                top = 20.dp,
                bottom = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                CollectionReminder(
                    enabled = remindersEnabled,
                    onToggle = {
                        remindersEnabled = !remindersEnabled
                    }
                )
            }

            item {
                Text(
                    text = "Weekly Schedule",
                    color = EcoText,
                    fontSize = 17.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(
                        top = 8.dp,
                        bottom = 1.dp
                    )
                )
            }

            items(schedule) { collectionDay ->
                ScheduleCard(collectionDay)
            }
        }
    }
}

@Composable
private fun CollectionReminder(
    enabled: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = EcoWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        EcoGreenLight,
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = "Collection reminders",
                    tint = EcoGreen,
                    modifier = Modifier.size(23.dp)
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                Text(
                    text = "Collection Reminder",
                    color = EcoText,
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = if (enabled) {
                        "Reminders are turned on"
                    } else {
                        "Get notified before collection"
                    },
                    color = EcoSecondaryText,
                    fontSize = 10.sp,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            ReminderSwitch(
                enabled = enabled,
                onClick = onToggle
            )
        }
    }
}

@Composable
private fun ReminderSwitch(
    enabled: Boolean,
    onClick: () -> Unit
) {
    val trackColor = if (enabled) {
        EcoGreen
    } else {
        Color(0xFFE3E3E3)
    }

    val thumbAlignment = if (enabled) {
        Alignment.CenterEnd
    } else {
        Alignment.CenterStart
    }

    Box(
        modifier = Modifier
            .size(
                width = 54.dp,
                height = 30.dp
            )
            .background(
                color = trackColor,
                shape = RoundedCornerShape(50.dp)
            )
            .clickable(onClick = onClick)
            .padding(1.dp),
        contentAlignment = thumbAlignment
    ) {

        Box(
            modifier = Modifier
                .size(26.dp)
                .background(
                    color = EcoWhite,
                    shape = RoundedCornerShape(50.dp)
                )
        )
    }
}

@Composable
private fun ScheduleCard(
    collectionDay: CollectionDay
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = EcoWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier
                    .size(
                        width = 58.dp,
                        height = 58.dp
                    )
                    .background(
                        EcoGreenLight,
                        RoundedCornerShape(14.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = collectionDay.day
                        .take(3)
                        .uppercase(),
                    color = EcoGreen,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = collectionDay.date.substringAfter(" "),
                    color = EcoText,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 1.dp)
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 14.dp)
                    .weight(1f)
            ) {

                Text(
                    text = collectionDay.area,
                    color = EcoText,
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = collectionDay.time,
                    color = EcoSecondaryText,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    modifier = Modifier.padding(top = 3.dp)
                )

                Row(
                    modifier = Modifier.padding(top = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Outlined.Recycling,
                        contentDescription = null,
                        tint = EcoGreen,
                        modifier = Modifier.size(14.dp)
                    )

                    Text(
                        text = collectionDay.wasteType,
                        color = EcoSecondaryText,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}