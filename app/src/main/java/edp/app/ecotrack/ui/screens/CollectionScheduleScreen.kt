
package edp.app.ecotrack.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edp.app.ecotrack.ui.theme.EcoBackground
import edp.app.ecotrack.ui.theme.EcoGreen
import edp.app.ecotrack.ui.theme.EcoGreenDark
import edp.app.ecotrack.ui.theme.EcoGreenLight
import edp.app.ecotrack.ui.theme.EcoSecondaryText
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

private data class CollectionEventInfo(
    val dayName: String,
    val typeName: String,
    val typeIcon: String,
    val badgeColor: Color,
    val timeSlot: String,
    val zoneName: String,
    val description: String
)

private fun getCollectionForDay(
    year: Int,
    month: Int,
    dayOfMonth: Int
): CollectionEventInfo? {
    val date = LocalDate.of(year, month, dayOfMonth)

    return when (date.dayOfWeek) {
        DayOfWeek.MONDAY -> CollectionEventInfo(
            dayName = "Monday",
            typeName = "Biodegradable",
            typeIcon = "🌿",
            badgeColor = Color(0xFF2E7D32),
            timeSlot = "6:00 AM - 9:00 AM",
            zoneName = "Zone 1 - 4",
            description = "Food scraps, fruit peelings, yard waste, and organic garden waste."
        )

        DayOfWeek.WEDNESDAY -> CollectionEventInfo(
            dayName = "Wednesday",
            typeName = "Recyclable",
            typeIcon = "♻",
            badgeColor = Color(0xFF00838F),
            timeSlot = "7:00 AM - 10:00 AM",
            zoneName = "Zone 5 - 8",
            description = "Plastic bottles, clean glass containers, paper, and cardboard."
        )

        DayOfWeek.FRIDAY -> CollectionEventInfo(
            dayName = "Friday",
            typeName = "Residual",
            typeIcon = "🗑",
            badgeColor = Color(0xFFD84315),
            timeSlot = "6:00 AM - 8:00 AM",
            zoneName = "All Barangays",
            description = "Styrofoam, worn-out rags, single-use wrappers, and non-recyclable waste."
        )

        DayOfWeek.SATURDAY -> CollectionEventInfo(
            dayName = "Saturday",
            typeName = "Hazardous",
            typeIcon = "⚠",
            badgeColor = Color(0xFFC62828),
            timeSlot = "8:00 AM - 11:00 AM",
            zoneName = "Barangay 3 & 6",
            description = "Batteries, paint cans, electronics, light bulbs, and household chemicals."
        )

        else -> null
    }
}

@Composable
fun CollectionScheduleScreen() {
    var remindersEnabled by remember { mutableStateOf(true) }

    val realToday = remember { LocalDate.now() }

    var currentYear by remember { mutableIntStateOf(realToday.year) }
    var currentMonth by remember { mutableIntStateOf(realToday.monthValue) }
    var selectedDay by remember { mutableIntStateOf(realToday.dayOfMonth) }

    val monthNames = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    val yearMonth = YearMonth.of(currentYear, currentMonth)
    val totalDays = yearMonth.lengthOfMonth()

    if (selectedDay > totalDays) {
        selectedDay = totalDays
    }

    val selectedEvent = getCollectionForDay(
        currentYear,
        currentMonth,
        selectedDay
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoBackground)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(EcoGreenDark)
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

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Interactive calendar & waste collection timeline",
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Collection Reminders",
                        color = EcoGreenDark,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "Get notified the night before",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Switch(
                    checked = remindersEnabled,
                    onCheckedChange = { remindersEnabled = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = EcoGreen,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFFBDBDBD)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Calendar",
                            tint = EcoGreenDark,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "${monthNames[currentMonth - 1]} $currentYear",
                            color = EcoGreenDark,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(EcoGreenLight)
                                .clickable {
                                    currentYear = realToday.year
                                    currentMonth = realToday.monthValue
                                    selectedDay = realToday.dayOfMonth
                                }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "Today",
                                color = EcoGreenDark,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        IconButton(
                            onClick = {
                                if (currentMonth == 1) {
                                    currentMonth = 12
                                    currentYear--
                                } else {
                                    currentMonth--
                                }

                                selectedDay = 1
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "Previous Month",
                                tint = EcoGreenDark
                            )
                        }

                        IconButton(
                            onClick = {
                                if (currentMonth == 12) {
                                    currentMonth = 1
                                    currentYear++
                                } else {
                                    currentMonth++
                                }

                                selectedDay = 1
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Next Month",
                                tint = EcoGreenDark
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                val daysOfWeek = listOf(
                    "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    daysOfWeek.forEach { dayName ->
                        Text(
                            text = dayName,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            color = EcoSecondaryText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                val firstDayOfWeek = yearMonth.atDay(1).dayOfWeek
                val firstDayOffset = when (firstDayOfWeek) {
                    DayOfWeek.SUNDAY -> 0
                    DayOfWeek.MONDAY -> 1
                    DayOfWeek.TUESDAY -> 2
                    DayOfWeek.WEDNESDAY -> 3
                    DayOfWeek.THURSDAY -> 4
                    DayOfWeek.FRIDAY -> 5
                    DayOfWeek.SATURDAY -> 6
                }

                val totalCells = firstDayOffset + totalDays
                val rows = (totalCells + 6) / 7

                for (rowIndex in 0 until rows) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                    ) {
                        for (colIndex in 0 until 7) {
                            val cellIndex = rowIndex * 7 + colIndex
                            val dayNumber = cellIndex - firstDayOffset + 1

                            if (dayNumber in 1..totalDays) {
                                val isSelected = dayNumber == selectedDay
                                val isToday =
                                    currentYear == realToday.year &&
                                            currentMonth == realToday.monthValue &&
                                            dayNumber == realToday.dayOfMonth

                                val event = getCollectionForDay(
                                    currentYear,
                                    currentMonth,
                                    dayNumber
                                )

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            when {
                                                isSelected -> EcoGreenDark
                                                isToday -> EcoGreenLight
                                                else -> Color.Transparent
                                            }
                                        )
                                        .then(
                                            if (isToday && !isSelected) {
                                                Modifier.border(
                                                    width = 1.dp,
                                                    color = EcoGreen,
                                                    shape = RoundedCornerShape(10.dp)
                                                )
                                            } else {
                                                Modifier
                                            }
                                        )
                                        .clickable {
                                            selectedDay = dayNumber
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = dayNumber.toString(),
                                            color = if (isSelected) {
                                                Color.White
                                            } else {
                                                EcoGreenDark
                                            },
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected || isToday) {
                                                FontWeight.Bold
                                            } else {
                                                FontWeight.Medium
                                            }
                                        )

                                        Spacer(modifier = Modifier.height(2.dp))

                                        if (event != null) {
                                            Box(
                                                modifier = Modifier
                                                    .size(5.dp)
                                                    .clip(CircleShape)
                                                    .background(
                                                        if (isSelected) {
                                                            Color.White
                                                        } else {
                                                            event.badgeColor
                                                        }
                                                    )
                                            )
                                        } else {
                                            Spacer(modifier = Modifier.size(5.dp))
                                        }
                                    }
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = "${monthNames[currentMonth - 1].uppercase()} $selectedDay, $currentYear SCHEDULE",
            modifier = Modifier.padding(horizontal = 20.dp),
            color = EcoGreenDark,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (selectedEvent != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .width(6.dp)
                            .height(115.dp)
                            .background(selectedEvent.badgeColor)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${selectedEvent.typeIcon}  ${selectedEvent.typeName} Waste",
                                color = EcoGreenDark,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        selectedEvent.badgeColor.copy(alpha = 0.12f)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = selectedEvent.dayName,
                                    color = selectedEvent.badgeColor,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = "Time",
                                tint = EcoGreen,
                                modifier = Modifier.size(15.dp)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = selectedEvent.timeSlot,
                                color = EcoGreenDark,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Zone",
                                tint = EcoGreen,
                                modifier = Modifier.size(15.dp)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = selectedEvent.zoneName,
                                color = EcoSecondaryText,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = selectedEvent.description,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(EcoGreenLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EventAvailable,
                            contentDescription = "No collection",
                            tint = EcoGreenDark,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "No Collection Scheduled",
                            color = EcoGreenDark,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "No waste collection service on ${monthNames[currentMonth - 1]} $selectedDay, $currentYear. Please store waste until the next scheduled day.",
                            color = EcoSecondaryText,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
