package edp.app.ecotrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edp.app.ecotrack.model.Task
import edp.app.ecotrack.model.TaskStatus
import edp.app.ecotrack.ui.components.TaskCard
import edp.app.ecotrack.ui.theme.EcoBackground
import edp.app.ecotrack.ui.theme.EcoGreen
import edp.app.ecotrack.ui.theme.EcoGreenLight
import edp.app.ecotrack.ui.theme.EcoSecondaryText
import edp.app.ecotrack.ui.theme.EcoText
import edp.app.ecotrack.ui.theme.EcoWhite

@Composable
fun MyTasksScreen(
    tasks: List<Task>,
    onTaskClick: (Task) -> Unit
) {
    val assignedTasks = tasks
        .filter { it.status == TaskStatus.ASSIGNED }
        .sortedBy { it.distanceKm }

    val waitingTasks = tasks
        .filter { it.status == TaskStatus.WAITING_FOR_VERIFICATION }
        .sortedBy { it.distanceKm }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(EcoGreen)
                .statusBarsPadding()
                .padding(
                    start = 28.dp,
                    end = 24.dp,
                    top = 16.dp,
                    bottom = 28.dp
                )
        ) {
            Text(
                text = "Good morning 👋",
                color = EcoWhite,
                fontSize = 14.sp,
                lineHeight = 18.sp
            )

            Text(
                text = "Juan dela Cruz",
                color = EcoWhite,
                fontSize = 24.sp,
                lineHeight = 29.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 3.dp)
            )

            Text(
                text = "Garbage Collector • Brgy. San Jose",
                color = EcoWhite.copy(alpha = 0.88f),
                fontSize = 11.sp,
                lineHeight = 15.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(
                top = 28.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                TaskSummaryCard(
                    taskCount = assignedTasks.size
                )
            }

            if (assignedTasks.isNotEmpty()) {
                item {
                    Text(
                        text = "Assigned Tasks",
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

                items(
                    items = assignedTasks,
                    key = { task -> task.id }
                ) { task ->
                    TaskCard(
                        task = task,
                        onClick = {
                            onTaskClick(task)
                        }
                    )
                }
            }

            if (waitingTasks.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.size(6.dp))

                    WaitingSectionHeader(
                        taskCount = waitingTasks.size
                    )
                }

                items(
                    items = waitingTasks,
                    key = { task -> "waiting_${task.id}" }
                ) { task ->
                    TaskCard(
                        task = task,
                        onClick = {
                            onTaskClick(task)
                        }
                    )
                }
            }

            if (assignedTasks.isEmpty() && waitingTasks.isEmpty()) {
                item {
                    EmptyTasksState()
                }
            }
        }
    }
}

@Composable
private fun TaskSummaryCard(
    taskCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
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
                    horizontal = 18.dp,
                    vertical = 19.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        EcoGreenLight,
                        RoundedCornerShape(50)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Assignment,
                    contentDescription = "Assigned tasks",
                    tint = EcoText,
                    modifier = Modifier.size(27.dp)
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = "$taskCount tasks assigned",
                    color = EcoText,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Tap a task to see details",
                    color = EcoSecondaryText,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun WaitingSectionHeader(
    taskCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
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
                    vertical = 14.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.HourglassTop,
                contentDescription = "Waiting for verification",
                tint = EcoGreen,
                modifier = Modifier.size(23.dp)
            )

            Column(
                modifier = Modifier
                    .padding(start = 11.dp)
                    .weight(1f)
            ) {
                Text(
                    text = "Waiting for Verification",
                    color = EcoText,
                    fontSize = 15.sp,
                    lineHeight = 19.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "$taskCount task${if (taskCount == 1) "" else "s"} submitted for review",
                    color = EcoSecondaryText,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyTasksState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 70.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(
                    EcoGreenLight,
                    RoundedCornerShape(18.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = "No tasks",
                tint = EcoGreen,
                modifier = Modifier.size(32.dp)
            )
        }

        Text(
            text = "No active tasks",
            color = EcoText,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 14.dp)
        )

        Text(
            text = "New collection tasks will appear here.",
            color = EcoSecondaryText,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 5.dp)
        )
    }
}