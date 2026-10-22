package edp.app.ecotrack.screens.residents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import edp.app.ecotrack.data.repository.ResidentScheduleRepository
import edp.app.ecotrack.network.ApiClient
import edp.app.ecotrack.ui.theme.EcoDarkGreen
import edp.app.ecotrack.ui.theme.EcoGreen
import edp.app.ecotrack.ui.theme.EcoLightGreen
import edp.app.ecotrack.ui.theme.EcoTextGreen
import org.json.JSONObject
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private data class MyBarangayData(
    val name: String = "Loading barangay...",
    val address: String = "",
    val captainName: String = "Not available",
    val residentId: String = "Not available",
    val residentSince: String = "Not available",
    val upcomingCollection: String = "Loading collection schedule...",
    val error: String = ""
)

private fun jsonField(json: JSONObject, key: String): String {
    return json.optString(key, "")
        .takeIf { it.isNotBlank() && it != "null" }
        .orEmpty()
}

private suspend fun loadMyBarangay(
    accessToken: String
): MyBarangayData {
    val response = ApiClient.getResidentProfile(accessToken)

    val user = response.optJSONObject("user")
        ?: throw IllegalStateException("Resident details are not available.")

    val barangay = response.optJSONObject("barangay")
        ?: throw IllegalStateException("Barangay details are not available.")

    val createdAt = jsonField(user, "created_at")

    val residentSince = try {
        if (createdAt.isNotBlank()) {
            LocalDate.parse(createdAt.take(10))
                .format(DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH))
        } else {
            "Not available"
        }
    } catch (_: Exception) {
        "Not available"
    }

    val upcomingCollection = try {
        val schedules = ResidentScheduleRepository.getSchedules(accessToken)
        val today = LocalDate.now()

        val nextSchedule = schedules
            .mapNotNull { schedule ->
                val day = try {
                    DayOfWeek.valueOf(
                        schedule.collectionDay
                            .trim()
                            .uppercase(Locale.ENGLISH)
                    )
                } catch (_: Exception) {
                    null
                }

                day?.let {
                    val daysUntil = (it.value - today.dayOfWeek.value + 7) % 7
                    Triple(schedule, daysUntil, it)
                }
            }
            .minByOrNull { it.second }

        if (nextSchedule != null) {
            val schedule = nextSchedule.first
            val dayLabel = if (nextSchedule.second == 0) {
                "Today"
            } else if (nextSchedule.second == 1) {
                "Tomorrow"
            } else {
                schedule.collectionDay
            }

            "${schedule.wasteType} • $dayLabel • ${schedule.collectionTime}"
        } else {
            "No upcoming collection scheduled"
        }
    } catch (_: Exception) {
        "Collection schedule unavailable"
    }

    return MyBarangayData(
        name = jsonField(barangay, "barangay_name")
            .ifBlank { "Barangay Hall" },
        address = jsonField(barangay, "address")
            .ifBlank { "Address unavailable" },
        captainName = jsonField(barangay, "captain_name")
            .ifBlank { "Not available" },
        residentId = jsonField(user, "user_id")
            .ifBlank { "Not available" },
        residentSince = residentSince,
        upcomingCollection = upcomingCollection
    )
}

@Composable
fun MyBarangayDialog(
    accessToken: String,
    onDismiss: () -> Unit,
    onViewSchedule: () -> Unit
) {
    var barangay by remember {
        mutableStateOf(MyBarangayData())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(accessToken) {
        isLoading = true

        try {
            barangay = loadMyBarangay(accessToken)
        } catch (e: Exception) {
            barangay = MyBarangayData(
                name = "My Barangay",
                error = e.message ?: "Unable to load barangay details."
            )
        } finally {
            isLoading = false
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = true
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.White,
                    RoundedCornerShape(18.dp)
                )
                .padding(22.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .size(width = 44.dp, height = 4.dp)
                    .background(
                        EcoGreen,
                        RoundedCornerShape(50)
                    )
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            EcoLightGreen,
                            RoundedCornerShape(50)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Barangay",
                        tint = EcoGreen,
                        modifier = Modifier.size(23.dp)
                    )
                }

                Spacer(modifier = Modifier.size(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "My Barangay",
                        color = EcoDarkGreen,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "Your assigned community and resident information.",
                        color = EcoTextGreen,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = EcoLightGreen
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    EcoDarkGreen,
                                    RoundedCornerShape(50)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = barangay.name.take(2).uppercase(),
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.size(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = barangay.name,
                                color = EcoDarkGreen,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = barangay.address,
                                color = EcoTextGreen,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.White)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Resident since",
                                color = EcoTextGreen,
                                fontSize = 10.sp
                            )

                            Text(
                                text = barangay.residentSince,
                                color = EcoDarkGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "Resident ID",
                                color = EcoTextGreen,
                                fontSize = 10.sp
                            )

                            Text(
                                text = barangay.residentId,
                                color = EcoDarkGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            if (isLoading) {
                Spacer(modifier = Modifier.height(16.dp))

                CircularProgressIndicator(
                    modifier = Modifier
                        .size(28.dp)
                        .align(Alignment.CenterHorizontally),
                    color = EcoGreen
                )
            }

            if (barangay.error.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = barangay.error,
                    color = Color(0xFFB3261E),
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            BarangayInfoRow(
                icon = Icons.Default.Person,
                label = "Barangay Captain",
                value = barangay.captainName
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )  {
                IconButton(
                    onClick = onViewSchedule,
                    modifier = Modifier
                        .size(46.dp)
                        .background(
                            EcoLightGreen,
                            RoundedCornerShape(12.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Recycling,
                        contentDescription = "Open collection schedule",
                        tint = EcoDarkGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.size(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Waste collection",
                        color = EcoTextGreen,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = barangay.upcomingCollection,
                        color = EcoDarkGreen,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(0.6f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EcoDarkGreen
                    ),
                    shape = RoundedCornerShape(9.dp)
                ) {
                    Text(text = "Close")
                }
            }
        }
    }
}

@Composable
private fun BarangayInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .background(
                    EcoLightGreen,
                    RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = EcoDarkGreen,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.size(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                color = EcoTextGreen,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = value,
                color = EcoDarkGreen,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
