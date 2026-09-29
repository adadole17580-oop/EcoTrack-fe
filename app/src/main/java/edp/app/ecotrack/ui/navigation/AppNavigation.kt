package edp.app.ecotrack.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import edp.app.ecotrack.data.FakeTaskRepository
import edp.app.ecotrack.model.Task
import edp.app.ecotrack.model.TaskStatus
import edp.app.ecotrack.ui.components.BottomDestination
import edp.app.ecotrack.ui.components.BottomNavBar
import edp.app.ecotrack.ui.screens.CollectionScheduleScreen
import edp.app.ecotrack.ui.screens.ContactAdminScreen
import edp.app.ecotrack.ui.screens.EditPasswordScreen
import edp.app.ecotrack.ui.screens.MyTasksScreen
import edp.app.ecotrack.ui.screens.ProfileScreen
import edp.app.ecotrack.ui.screens.TaskDetailScreen
import edp.app.ecotrack.ui.screens.TaskHistoryScreen
import edp.app.ecotrack.ui.screens.WasteGuideScreen
import edp.app.ecotrack.ui.theme.EcoBackground
import edp.app.ecotrack.ui.theme.EcoTrackTheme

@Composable
fun AppNavigation() {

    val taskRepository = remember {
        FakeTaskRepository()
    }

    var tasks by remember {
        mutableStateOf(taskRepository.getTasks())
    }

    var currentDestination by remember {
        mutableStateOf(BottomDestination.TASKS)
    }

    var selectedTask by remember {
        mutableStateOf<Task?>(null)
    }

    var showTaskDetail by remember {
        mutableStateOf(false)
    }

    var showEditPassword by remember {
        mutableStateOf(false)
    }

    var showContactAdmin by remember {
        mutableStateOf(false)
    }

    var showTaskHistory by remember {
        mutableStateOf(false)
    }

    fun openTask(task: Task) {
        val seenTask = task.copy(isNew = false)

        taskRepository.updateTask(seenTask)

        tasks = taskRepository.getTasks()
        selectedTask = seenTask
        showTaskDetail = true
    }

    fun submitTaskForVerification() {

        val task = selectedTask ?: return

        val updatedTask = task.copy(
            status = TaskStatus.WAITING_FOR_VERIFICATION,
            isNew = false
        )

        taskRepository.updateTask(updatedTask)

        tasks = taskRepository.getTasks()

        selectedTask = null
        showTaskDetail = false
        currentDestination = BottomDestination.TASKS
    }

    fun closeSubPage() {
        showTaskDetail = false
        showEditPassword = false
        showContactAdmin = false
        showTaskHistory = false
        selectedTask = null
    }

    val showingSubPage =
        showTaskDetail ||
                showEditPassword ||
                showContactAdmin ||
                showTaskHistory

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoBackground)
    ) {

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {

            when {
                showTaskDetail && selectedTask != null -> {
                    TaskDetailScreen(
                        task = selectedTask!!,
                        onBack = {
                            closeSubPage()
                        },
                        onMarkCompleted = {
                            submitTaskForVerification()
                        }
                    )
                }

                showEditPassword -> {
                    EditPasswordScreen(
                        onBack = {
                            closeSubPage()
                        }
                    )
                }

                showContactAdmin -> {
                    ContactAdminScreen(
                        onBack = {
                            closeSubPage()
                        }
                    )
                }

                showTaskHistory -> {
                    TaskHistoryScreen(
                        tasks = tasks,
                        onBack = {
                            closeSubPage()
                        },
                        onTaskClick = { task ->
                            openTask(task)
                        }
                    )
                }

                else -> {
                    when (currentDestination) {

                        BottomDestination.TASKS -> {
                            MyTasksScreen(
                                tasks = tasks,
                                onTaskClick = { task ->
                                    openTask(task)
                                }
                            )
                        }

                        BottomDestination.GUIDE -> {
                            WasteGuideScreen()
                        }

                        BottomDestination.SCHEDULE -> {
                            CollectionScheduleScreen()
                        }

                        BottomDestination.PROFILE -> {
                            ProfileScreen(
                                onCompletedTasksClick = {
                                    showTaskHistory = true
                                },
                                onEditPasswordClick = {
                                    showEditPassword = true
                                },
                                onContactAdminClick = {
                                    showContactAdmin = true
                                },
                                onLogoutClick = {
                                    // Login/logout will be connected to the backend later.
                                }
                            )
                        }
                    }
                }
            }
        }

        if (!showingSubPage) {
            BottomNavBar(
                currentDestination = currentDestination,
                onDestinationSelected = { destination ->
                    currentDestination = destination
                }
            )
        }
    }
}