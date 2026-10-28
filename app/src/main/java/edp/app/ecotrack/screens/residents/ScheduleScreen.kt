package edp.app.ecotrack.screens.residents

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import edp.app.ecotrack.data.model.ResidentSchedule
import edp.app.ecotrack.data.repository.ResidentScheduleRepository
import edp.app.ecotrack.network.ApiClient

private fun parseCollectionDate(dateStr: String): LocalDate? {
    return try {
        LocalDate.parse(dateStr.trim())
    } catch (_: Exception) {
        try {
            val dayOfWeek = java.time.DayOfWeek.valueOf(dateStr.trim().uppercase(Locale.ENGLISH))
            val today = LocalDate.now()
            var daysUntil = dayOfWeek.value - today.dayOfWeek.value
            if (daysUntil < 0) daysUntil += 7
            today.plusDays(daysUntil.toLong())
        } catch (_: Exception) {
            null
        }
    }
}

private val EcoDarkGreen = Color(0xFF24652A)
private val EcoGreen = Color(0xFF2D9B59)
private val EcoLightGreen = Color(0xFFE0F4E8)
private val EcoPaleGreen = Color(0xFFF3FAF5)
private val EcoGray = Color(0xFF777777)
private val EcoBorderGreen = Color(0xFF8CB99A)

@Composable
fun ScheduleScreen(
    accessToken: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("ecotrack_prefs", Context.MODE_PRIVATE)
    }

    var schedules by remember {
        mutableStateOf<List<ResidentSchedule>>(emptyList())
    }

    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var remindersEnabled by remember {
        mutableStateOf(sharedPreferences.getBoolean("reminders_enabled", true))
    }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var reloadKey by remember { mutableStateOf(0) }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            remindersEnabled = true
            sharedPreferences.edit().putBoolean("reminders_enabled", true).apply()
            scheduleCollectionReminders(context)
        } else {
            remindersEnabled = false
            sharedPreferences.edit().putBoolean("reminders_enabled", false).apply()
            cancelCollectionReminders(context)
        }
    }

    LaunchedEffect(accessToken, reloadKey) {
        if (accessToken.isBlank()) {
            schedules = emptyList()
            errorMessage = "Please log in to view collection schedules."
            isLoading = false
        } else {
            isLoading = true
            errorMessage = null

            try {
                val profileResult = ApiClient.getResidentProfile(accessToken)
                val userObj = profileResult.optJSONObject("user") ?: profileResult
                val residentBarangayId = userObj.optInt("barangay_id", 0)

                val fetchedSchedules = ResidentScheduleRepository.getSchedules(accessToken)

                if (residentBarangayId > 0 && fetchedSchedules.isNotEmpty()) {
                    val mismatch = fetchedSchedules.any { it.barangayId != residentBarangayId }
                    if (mismatch) {
                        errorMessage = "Assigned barangay mismatch: Your barangay (ID $residentBarangayId) does not match the collection schedules barangay."
                        schedules = emptyList()
                    } else {
                        schedules = fetchedSchedules
                    }
                } else {
                    schedules = fetchedSchedules
                }

                if (remindersEnabled && errorMessage == null) {
                    scheduleCollectionReminders(context)
                }
            } catch (exception: Exception) {
                schedules = emptyList()
                errorMessage = exception.message
                    ?: "Unable to load collection schedules."
            } finally {
                isLoading = false
            }
        }
    }

    val selectedSchedules = schedules.filter { schedule ->
        val scheduleDate = parseCollectionDate(schedule.collectionDay)
        scheduleDate != null && scheduleDate == selectedDate
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(EcoPaleGreen),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        EcoDarkGreen,
                        RoundedCornerShape(
                            bottomStart = 24.dp,
                            bottomEnd = 24.dp
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Text(
                    text = "Collection Schedule",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "View collection days for your barangay",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 14.sp
                )
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(EcoLightGreen, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = EcoDarkGreen
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp)
                    ) {
                        Text(
                            text = "Collection Reminders",
                            fontWeight = FontWeight.SemiBold,
                            color = EcoDarkGreen,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Get notified before collection day",
                            color = EcoGray,
                            fontSize = 12.sp
                        )
                    }

                    Switch(
                        checked = remindersEnabled,
                        onCheckedChange = { enabled ->
                            remindersEnabled = enabled
                            sharedPreferences.edit().putBoolean("reminders_enabled", enabled).apply()
                            if (enabled) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                    context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                } else {
                                    scheduleCollectionReminders(context)
                                }
                            } else {
                                cancelCollectionReminders(context)
                            }
                        }
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                currentMonth = currentMonth.minusMonths(1)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Previous month",
                                tint = EcoDarkGreen
                            )
                        }

                        Text(
                            text = currentMonth.format(
                                DateTimeFormatter.ofPattern("MMMM yyyy")
                            ),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = EcoDarkGreen
                        )

                        IconButton(
                            onClick = {
                                currentMonth = currentMonth.plusMonths(1)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForwardIos,
                                contentDescription = "Next month",
                                tint = EcoDarkGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf("M", "T", "W", "T", "F", "S", "S")
                            .forEach { day ->
                                Text(
                                    text = day,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Center,
                                    color = EcoGray,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val firstDay = currentMonth.atDay(1)
                    val leadingDays = firstDay.dayOfWeek.value - 1
                    val daysInMonth = currentMonth.lengthOfMonth()
                    val totalCells = ((leadingDays + daysInMonth + 6) / 7) * 7

                    for (week in 0 until totalCells / 7) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            for (dayIndex in 0 until 7) {
                                val cellIndex = week * 7 + dayIndex
                                val dayNumber = cellIndex - leadingDays + 1

                                if (dayNumber in 1..daysInMonth) {
                                    val date = currentMonth.atDay(dayNumber)
                                    val isSelected = date == selectedDate

                                    val hasCollection = schedules.any { schedule ->
                                        val scheduleDate = parseCollectionDate(schedule.collectionDay)
                                        scheduleDate != null && scheduleDate == date
                                    }

                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(vertical = 4.dp)
                                            .clickable {
                                                selectedDate = date
                                            },
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .background(
                                                    color = if (isSelected) {
                                                        EcoDarkGreen
                                                    } else {
                                                        Color.Transparent
                                                    },
                                                    shape = CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = dayNumber.toString(),
                                                color = if (isSelected) {
                                                    Color.White
                                                } else {
                                                    Color.Black
                                                },
                                                fontWeight = if (isSelected) {
                                                    FontWeight.Bold
                                                } else {
                                                    FontWeight.Normal
                                                }
                                            )
                                        }

                                        Box(
                                            modifier = Modifier
                                                .padding(top = 2.dp)
                                                .size(5.dp)
                                                .background(
                                                    color = if (hasCollection) {
                                                        EcoGreen
                                                    } else {
                                                        Color.Transparent
                                                    },
                                                    shape = CircleShape
                                                )
                                        )
                                    }
                                } else {
                                    Spacer(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(45.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    HorizontalDivider(color = EcoLightGreen)

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = selectedDate.format(
                            DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")
                        ),
                        color = EcoDarkGreen,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                }
            }
        }

        item {
            Text(
                text = "COLLECTION DETAILS",
                modifier = Modifier.padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 4.dp
                ),
                color = EcoDarkGreen,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        when {
            isLoading -> {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .background(
                                Color.White,
                                RoundedCornerShape(16.dp)
                            )
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = EcoGreen
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Loading collection schedules...",
                            color = EcoGray
                        )
                    }
                }
            }

            errorMessage != null -> {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = errorMessage
                                    ?: "Unable to load schedules.",
                                color = Color(0xFFB3261E),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    reloadKey++
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = EcoDarkGreen
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null
                                )

                                Spacer(modifier = Modifier.size(8.dp))

                                Text("Retry")
                            }
                        }
                    }
                }
            }

            schedules.isEmpty() -> {
                item {
                    EmptyScheduleCard(
                        message = "No collection schedules are available for your assigned barangay."
                    )
                }
            }

            selectedSchedules.isEmpty() -> {
                item {
                    EmptyScheduleCard(
                        message = "No collection is scheduled for this day."
                    )
                }
            }

            else -> {
                items(
                    items = selectedSchedules,
                    key = { it.scheduleId }
                ) { schedule ->
                    CollectionScheduleCard(
                        schedule = schedule,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@SuppressLint("MissingPermission")
private fun scheduleCollectionReminders(context: Context) {
    val notificationManager = NotificationManagerCompat.from(context)
    val channel = NotificationChannel(
        "collection_reminders",
        "Collection Reminders",
        NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
        description = "Notifications for upcoming waste collection schedules"
    }
    notificationManager.createNotificationChannel(channel)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
        context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }

    val notification = NotificationCompat.Builder(context, "collection_reminders")
        .setSmallIcon(android.R.drawable.ic_menu_agenda)
        .setContentTitle("Collection Reminders Active")
        .setContentText("You are set to receive notifications before scheduled waste collections.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .build()

    notificationManager.notify(1001, notification)
}

private fun cancelCollectionReminders(context: Context) {
    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.cancel(1001)
    for (i in 2000..2010) {
        notificationManager.cancel(i)
    }
}

@Composable
private fun EmptyScheduleCard(
    message: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = EcoGreen,
                modifier = Modifier.size(36.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = message,
                color = EcoGray,
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun CollectionScheduleCard(
    schedule: ResidentSchedule,
    modifier: Modifier = Modifier
) {
    val wasteColor = when (schedule.wasteType.lowercase()) {
        "biodegradable" -> Color(0xFF2D9B59)
        "recyclable" -> Color(0xFF2878B5)
        "residual" -> Color(0xFF777777)
        "hazardous" -> Color(0xFFB88916)
        else -> EcoDarkGreen
    }

    val wasteIcon = when (schedule.wasteType.lowercase()) {
        "biodegradable" -> "🌿"
        "recyclable" -> "♻️"
        "residual" -> "🗑️"
        "hazardous" -> "⚠️"
        else -> "♻️"
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = wasteColor.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = wasteIcon,
                    fontSize = 23.sp
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = schedule.wasteType,
                    color = EcoDarkGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(5.dp))

                val formattedDate = parseCollectionDate(schedule.collectionDay)?.format(
                    DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.ENGLISH)
                ) ?: schedule.collectionDay

                Text(
                    text = formattedDate,
                    color = EcoGray,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = formatCollectionTime(schedule.collectionTime),
                    color = wasteColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )

                schedule.customAddress
                    ?.takeIf { it.isNotBlank() }
                    ?.let { address ->
                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = address,
                            color = EcoGray,
                            fontSize = 12.sp
                        )
                    }
            }
        }
    }
}
