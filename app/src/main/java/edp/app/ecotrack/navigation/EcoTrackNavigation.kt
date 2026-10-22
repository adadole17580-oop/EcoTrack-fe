
package edp.app.ecotrack.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel
import edp.app.ecotrack.viewmodel.ResidentDashboardViewModel
import edp.app.ecotrack.screens.residents.GuideScreen
import edp.app.ecotrack.screens.residents.HomeScreen
import edp.app.ecotrack.screens.residents.NotificationScreen
import edp.app.ecotrack.screens.residents.ProfileScreen
import edp.app.ecotrack.screens.residents.ReportDetailsScreen
import edp.app.ecotrack.screens.residents.ReportScreen
import edp.app.ecotrack.screens.residents.ScheduleScreen

@Composable
fun EcoTrackNavigation(
    navController: NavHostController,
    accessToken: String,
    onLogout: () -> Unit
) {
    var isSlideAction by remember { mutableStateOf(false) }
    var selectedReportId by remember { mutableStateOf<Long?>(null) }

    val dashboardViewModel: ResidentDashboardViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "home",
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(
            route = "home",
            enterTransition = {
                if (isSlideAction) {
                    slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(
                            300,
                            easing = FastOutSlowInEasing
                        )
                    )
                } else {
                    EnterTransition.None
                }
            },
            exitTransition = {
                if (isSlideAction) {
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(
                            300,
                            easing = FastOutSlowInEasing
                        )
                    )
                } else {
                    ExitTransition.None
                }
            }
        ) {
            HomeScreen(
                accessToken = accessToken,
                dashboardViewModel = dashboardViewModel,
                onNotificationsClick = {
                    isSlideAction = true
                    navController.navigate("notifications")
                },
                onViewAllReportsClick = {
                    isSlideAction = true
                    navController.navigate("report")
                },
                onScheduleClick = {
                    isSlideAction = true
                    navController.navigate("schedule")
                },
                onViewSchedule = {
                    isSlideAction = true
                    navController.navigate("schedule")
                },
                onQuickActionClick = { action: String ->
                    isSlideAction = true

                    when (action) {
                        "schedule" -> navController.navigate("schedule")
                        "report" -> navController.navigate("report")
                        "guide" -> navController.navigate("guide")
                        "profile" -> navController.navigate("profile")
                    }
                },
                onReportDetailsClick = { reportId ->
                    selectedReportId = reportId
                }
            )
        }

        composable(
            route = "schedule",
            enterTransition = {
                if (isSlideAction) {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(
                            300,
                            easing = FastOutSlowInEasing
                        )
                    )
                } else {
                    EnterTransition.None
                }
            },
            exitTransition = {
                if (isSlideAction) {
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(
                            300,
                            easing = FastOutSlowInEasing
                        )
                    )
                } else {
                    ExitTransition.None
                }
            }
        ) {
            ScheduleScreen(
                accessToken = accessToken
            )
        }

        composable(
            route = "report",
            enterTransition = {
                if (isSlideAction) {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(
                            300,
                            easing = FastOutSlowInEasing
                        )
                    )
                } else {
                    EnterTransition.None
                }
            },
            exitTransition = {
                if (isSlideAction) {
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(
                            300,
                            easing = FastOutSlowInEasing
                        )
                    )
                } else {
                    ExitTransition.None
                }
            }
        ) {
            ReportScreen(
                accessToken = accessToken,
                onReportClick = { reportId ->
                    selectedReportId = reportId
                }
            )
        }

        composable(
            route = "guide",
            enterTransition = {
                if (isSlideAction) {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(
                            300,
                            easing = FastOutSlowInEasing
                        )
                    )
                } else {
                    EnterTransition.None
                }
            },
            exitTransition = {
                if (isSlideAction) {
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(
                            300,
                            easing = FastOutSlowInEasing
                        )
                    )
                } else {
                    ExitTransition.None
                }
            }
        ) {
            GuideScreen()
        }

        composable(
            route = "profile",
            enterTransition = {
                if (isSlideAction) {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(
                            300,
                            easing = FastOutSlowInEasing
                        )
                    )
                } else {
                    EnterTransition.None
                }
            },
            exitTransition = {
                if (isSlideAction) {
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(
                            300,
                            easing = FastOutSlowInEasing
                        )
                    )
                } else {
                    ExitTransition.None
                }
            }
        ) {
            ProfileScreen(
                accessToken = accessToken,
                onNotificationsClick = {
                    isSlideAction = true
                    navController.navigate("notifications")
                },
                onViewSchedule = {
                    navController.navigate("schedule")
                },
                onLogout = onLogout,
                dashboardViewModel = dashboardViewModel
            )
        }

        composable(
            route = "notifications",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(
                        300,
                        easing = FastOutSlowInEasing
                    )
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(
                        300,
                        easing = FastOutSlowInEasing
                    )
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(
                        300,
                        easing = FastOutSlowInEasing
                    )
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(
                        300,
                        easing = FastOutSlowInEasing
                    )
                )
            }
        ) {
            NotificationScreen(
                accessToken = accessToken,
                onBackToHome = {
                    isSlideAction = false
                    navController.popBackStack()
                },
                onNotificationClick = { notificationInfo ->
                    val destination = when {
                        notificationInfo.contains(
                            "collection reminder",
                            ignoreCase = true
                        ) ||
                                notificationInfo.contains(
                                    "collection schedule",
                                    ignoreCase = true
                                ) ||
                                notificationInfo.contains(
                                    "schedule",
                                    ignoreCase = true
                                ) -> "schedule"

                        notificationInfo.contains(
                            "report",
                            ignoreCase = true
                        ) ||
                                notificationInfo.contains(
                                    "resolved",
                                    ignoreCase = true
                                ) ||
                                notificationInfo.contains(
                                    "progress",
                                    ignoreCase = true
                                ) ||
                                notificationInfo.contains(
                                    "pending",
                                    ignoreCase = true
                                ) -> "report"

                        notificationInfo.contains(
                            "profile",
                            ignoreCase = true
                        ) ||
                                notificationInfo.contains(
                                    "account",
                                    ignoreCase = true
                                ) -> "profile"

                        notificationInfo.contains(
                            "waste guide",
                            ignoreCase = true
                        ) ||
                                notificationInfo.contains(
                                    "segregation",
                                    ignoreCase = true
                                ) ||
                                notificationInfo.contains(
                                    "guide",
                                    ignoreCase = true
                                ) -> "guide"

                        else -> null
                    }

                    if (destination != null) {
                        isSlideAction = true
                        navController.navigate(destination) {
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }

    selectedReportId?.let { reportId ->
        ReportDetailsScreen(
            accessToken = accessToken,
            reportId = reportId,
            onDismiss = {
                selectedReportId = null
            }
        )
    }
}
