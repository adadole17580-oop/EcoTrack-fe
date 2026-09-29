package edp.app.ecotrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edp.app.ecotrack.model.Task
import edp.app.ecotrack.model.TaskStatus
import edp.app.ecotrack.ui.theme.EcoGreen
import edp.app.ecotrack.ui.theme.EcoGreenLight
import edp.app.ecotrack.ui.theme.EcoSecondaryText
import edp.app.ecotrack.ui.theme.EcoText
import edp.app.ecotrack.ui.theme.EcoWhite

@Composable
fun TaskDetailScreen(
    task: Task,
    onBack: () -> Unit,
    onMarkCompleted: () -> Unit
) {

    var proofPhotoAttached by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 16.dp,
                    top = 6.dp,
                    bottom = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBack
            ) {

                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Back",
                    tint = EcoText
                )
            }

            Column(
                modifier = Modifier.padding(start = 4.dp)
            ) {

                Text(
                    text = "Task Details",
                    style = MaterialTheme.typography.titleLarge,
                    color = EcoText
                )

                Text(
                    text = "Collection task information",
                    style = MaterialTheme.typography.bodySmall,
                    color = EcoSecondaryText,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = EcoWhite
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(9.dp)
                ) {

                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = EcoText
                    )

                    Text(
                        text = task.wasteType,
                        style = MaterialTheme.typography.labelLarge,
                        color = EcoGreen,
                        modifier = Modifier
                            .background(
                                color = EcoGreenLight,
                                shape = RoundedCornerShape(50.dp)
                            )
                            .padding(
                                horizontal = 10.dp,
                                vertical = 5.dp
                            )
                    )

                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = EcoSecondaryText
                    )

                    Spacer(
                        modifier = Modifier.size(3.dp)
                    )

                    DetailInfoRow(
                        icon = Icons.Outlined.LocationOn,
                        text = task.location
                    )

                    DetailInfoRow(
                        icon = Icons.Outlined.Navigation,
                        text = "${task.distanceKm} km away"
                    )

                    Text(
                        text = "Reported: ${task.reportedTime}",
                        style = MaterialTheme.typography.bodySmall,
                        color = EcoSecondaryText
                    )

                    Button(
                        onClick = {
                            // Navigation/map functionality will be connected later.
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EcoGreen
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {

                        Icon(
                            imageVector = Icons.Outlined.Navigation,
                            contentDescription = "Navigate"
                        )

                        Text(
                            text = "Navigate to Location",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            when (task.status) {

                TaskStatus.ASSIGNED -> {

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = EcoWhite
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(18.dp)
                        ) {

                            Text(
                                text = "Proof of Cleanup",
                                style = MaterialTheme.typography.titleMedium,
                                color = EcoText
                            )

                            Text(
                                text = if (proofPhotoAttached) {
                                    "Proof photo attached. You can now submit the task."
                                } else {
                                    "Add one photo after the cleanup has been completed."
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = EcoSecondaryText,
                                modifier = Modifier.padding(top = 5.dp)
                            )

                            Button(
                                onClick = {
                                    proofPhotoAttached = true
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = EcoGreen
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {

                                Icon(
                                    imageVector = Icons.Outlined.CameraAlt,
                                    contentDescription = "Add photo"
                                )

                                Text(
                                    text = if (proofPhotoAttached) {
                                        "Photo Attached"
                                    } else {
                                        "Add Photo"
                                    },
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }

                    Button(
                        onClick = onMarkCompleted,
                        enabled = proofPhotoAttached,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EcoGreen,
                            disabledContainerColor = Color(0xFFD1D5D1),
                            disabledContentColor = Color(0xFF7D837D)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {

                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = "Complete task"
                        )

                        Text(
                            text = "Submit for Verification",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Text(
                        text = if (proofPhotoAttached) {
                            "Your task is ready to be submitted for admin verification."
                        } else {
                            "A proof photo is required before submitting this task."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = EcoSecondaryText,
                        modifier = Modifier.padding(
                            start = 4.dp,
                            end = 4.dp
                        )
                    )
                }

                TaskStatus.WAITING_FOR_VERIFICATION -> {
                    StatusMessageCard(
                        icon = Icons.Outlined.HourglassTop,
                        title = "Waiting for Verification",
                        message = "Your cleanup has been submitted and is waiting for admin verification."
                    )
                }

                TaskStatus.COMPLETED -> {

                    StatusMessageCard(
                        icon = Icons.Outlined.CheckCircle,
                        title = "Task Completed",
                        message = "This task has been verified by the administrator."
                    )
                }
            }

            Spacer(
                modifier = Modifier.size(8.dp)
            )
        }
    }
}

@Composable
private fun DetailInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = EcoSecondaryText,
            modifier = Modifier.size(19.dp)
        )

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = EcoText,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun StatusMessageCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    message: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = EcoWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = EcoGreen,
                modifier = Modifier.size(30.dp)
            )

            Column(
                modifier = Modifier.padding(start = 12.dp)
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = EcoText
                )

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = EcoSecondaryText,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}