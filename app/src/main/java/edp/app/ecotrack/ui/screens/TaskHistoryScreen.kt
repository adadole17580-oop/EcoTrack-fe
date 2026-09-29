package edp.app.ecotrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edp.app.ecotrack.model.Task
import edp.app.ecotrack.model.TaskStatus
import edp.app.ecotrack.ui.theme.EcoBackground
import edp.app.ecotrack.ui.theme.EcoGreen
import edp.app.ecotrack.ui.theme.EcoGreenLight
import edp.app.ecotrack.ui.theme.EcoSecondaryText
import edp.app.ecotrack.ui.theme.EcoText
import edp.app.ecotrack.ui.theme.EcoWhite

@Composable
fun TaskHistoryScreen(
    tasks: List<Task>,
    onBack: () -> Unit,
    onTaskClick: (Task) -> Unit
) {
    val waitingTasks = tasks
        .filter {
            it.status == TaskStatus.WAITING_FOR_VERIFICATION
        }
        .sortedByDescending { it.id }

    val completedTasks = tasks
        .filter {
            it.status == TaskStatus.COMPLETED
        }
        .sortedByDescending { it.id }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoBackground)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 16.dp,
                    top = 18.dp,
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
                    tint = EcoText,
                    modifier = Modifier.size(23.dp)
                )
            }

            Column(
                modifier = Modifier.padding(start = 5.dp)
            ) {
                Text(
                    text = "Task History",
                    color = EcoText,
                    fontSize = 20.sp,
                    lineHeight = 25.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Completed and submitted tasks",
                    color = EcoSecondaryText,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(
                top = 16.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (waitingTasks.isNotEmpty()) {
                item {
                    HistorySectionHeader(
                        title = "Waiting for Verification",
                        subtitle = "Submitted tasks waiting for admin review.",
                        icon = Icons.Outlined.HourglassTop
                    )
                }

                items(
                    items = waitingTasks,
                    key = { task -> "waiting_${task.id}" }
                ) { task ->
                    HistoryTaskCard(
                        task = task,
                        onClick = {
                            onTaskClick(task)
                        }
                    )
                }
            }

            if (completedTasks.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.size(8.dp))

                    HistorySectionHeader(
                        title = "Completed",
                        subtitle = "Tasks already verified by the administrator.",
                        icon = Icons.Outlined.CheckCircle
                    )
                }

                items(
                    items = completedTasks,
                    key = { task -> "completed_${task.id}" }
                ) { task ->
                    HistoryTaskCard(
                        task = task,
                        onClick = {
                            onTaskClick(task)
                        }
                    )
                }
            }

            if (
                waitingTasks.isEmpty() &&
                completedTasks.isEmpty()
            ) {
                item {
                    EmptyHistoryState()
                }
            }
        }
    }
}

@Composable
private fun HistorySectionHeader(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 3.dp,
                bottom = 3.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = EcoGreen,
            modifier = Modifier.size(23.dp)
        )

        Column(
            modifier = Modifier.padding(start = 10.dp)
        ) {
            Text(
                text = title,
                color = EcoText,
                fontSize = 15.sp,
                lineHeight = 19.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = subtitle,
                color = EcoSecondaryText,
                fontSize = 10.sp,
                lineHeight = 14.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
private fun HistoryTaskCard(
    task: Task,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
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
            val completed =
                task.status == TaskStatus.COMPLETED

            Icon(
                imageVector = if (completed) {
                    Icons.Outlined.CheckCircle
                } else {
                    Icons.Outlined.HourglassTop
                },
                contentDescription = null,
                tint = EcoGreen,
                modifier = Modifier.size(27.dp)
            )

            Column(
                modifier = Modifier
                    .padding(start = 13.dp)
                    .weight(1f)
            ) {
                Text(
                    text = task.title,
                    color = EcoText,
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    modifier = Modifier.padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = EcoSecondaryText,
                        modifier = Modifier.size(15.dp)
                    )

                    Text(
                        text = task.location,
                        color = EcoSecondaryText,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Text(
                    text = "TSK-${task.id.toString().padStart(3, '0')}",
                    color = EcoSecondaryText,
                    fontSize = 9.sp,
                    modifier = Modifier.padding(top = 5.dp)
                )
            }

            HistoryStatusPill(
                status = task.status
            )
        }
    }
}

@Composable
private fun HistoryStatusPill(
    status: TaskStatus
) {
    val text = when (status) {
        TaskStatus.WAITING_FOR_VERIFICATION -> "Waiting"
        TaskStatus.COMPLETED -> "Completed"
        TaskStatus.ASSIGNED -> "Assigned"
    }

    Text(
        text = text,
        color = EcoGreen,
        fontSize = 9.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .background(
                EcoGreenLight,
                RoundedCornerShape(50.dp)
            )
            .padding(
                horizontal = 9.dp,
                vertical = 5.dp
            )
    )
}

@Composable
private fun EmptyHistoryState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 70.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.CheckCircle,
            contentDescription = null,
            tint = EcoGreen,
            modifier = Modifier.size(48.dp)
        )

        Text(
            text = "No task history yet",
            color = EcoText,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 12.dp)
        )

        Text(
            text = "Completed and submitted tasks will appear here.",
            color = EcoSecondaryText,
            fontSize = 11.sp,
            modifier = Modifier.padding(top = 5.dp)
        )
    }
}