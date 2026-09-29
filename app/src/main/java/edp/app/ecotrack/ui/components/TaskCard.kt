package edp.app.ecotrack.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import edp.app.ecotrack.model.Task
import edp.app.ecotrack.model.TaskStatus
import edp.app.ecotrack.ui.theme.EcoGreen
import edp.app.ecotrack.ui.theme.EcoGreenLight
import edp.app.ecotrack.ui.theme.EcoSecondaryText
import edp.app.ecotrack.ui.theme.EcoText
import edp.app.ecotrack.ui.theme.EcoWhite

@Composable
fun TaskCard(
    task: Task,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(
            containerColor = EcoWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 21.dp,
                    end = 17.dp,
                    top = 17.dp,
                    bottom = 14.dp
                ),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = task.title,
                        color = EcoText,
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Row(
                        modifier = Modifier.padding(top = 9.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = "Location",
                            tint = EcoSecondaryText,
                            modifier = Modifier.size(15.dp)
                        )

                        Text(
                            text = task.location,
                            color = EcoSecondaryText,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }

                    Text(
                        text = "TSK-${task.id.toString().padStart(3, '0')}",
                        color = EcoSecondaryText,
                        fontSize = 11.sp,
                        lineHeight = 15.sp,
                        modifier = Modifier.padding(top = 7.dp)
                    )
                }

                if (task.isNew) {
                    Text(
                        text = "NEW",
                        color = EcoGreen,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .background(
                                EcoGreenLight,
                                RoundedCornerShape(50.dp)
                            )
                            .padding(
                                horizontal = 8.dp,
                                vertical = 4.dp
                            )
                    )
                }
            }

            when (task.status) {
                TaskStatus.ASSIGNED -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.End
                    ) {
                        Text(
                            text = "Tap to view & navigate →",
                            color = EcoGreen,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .background(
                                    EcoGreenLight,
                                    RoundedCornerShape(50.dp)
                                )
                                .padding(
                                    horizontal = 10.dp,
                                    vertical = 4.dp
                                )
                        )
                    }
                }

                TaskStatus.WAITING_FOR_VERIFICATION -> {
                    StatusLabel(
                        icon = Icons.Outlined.HourglassTop,
                        text = "Waiting for verification"
                    )
                }

                TaskStatus.COMPLETED -> {
                    StatusLabel(
                        icon = Icons.Outlined.CheckCircle,
                        text = "Completed"
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusLabel(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = EcoGreen,
            modifier = Modifier.size(14.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = text,
            color = EcoGreen,
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium
        )
    }
}