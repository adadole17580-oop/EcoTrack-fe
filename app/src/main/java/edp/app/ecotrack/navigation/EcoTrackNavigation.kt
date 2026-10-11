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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edp.app.ecotrack.screens.GuideScreen
import edp.app.ecotrack.screens.HomeScreen
import edp.app.ecotrack.screens.NotificationScreen
import edp.app.ecotrack.screens.ProfileScreen
import edp.app.ecotrack.screens.ReportScreen
import edp.app.ecotrack.screens.ScheduleScreen

@Composable
fun EcoTrackNavigation(
    navController: NavHostController
) {

    var isSlideAction by remember {
        mutableStateOf(false)
    }

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
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    )
                } else {
                    EnterTransition.None
                }
            },
            exitTransition = {
                if (isSlideAction) {
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    )
                } else {
                    ExitTransition.None
                }
            }
        ) {

            HomeScreen(
                onScheduleClick = {
                    isSlideAction = true
                    navController.navigate("schedule")
                },

                onReportClick = {
                    isSlideAction = true
                    navController.navigate("report")
                },

                onGuideClick = {
                    isSlideAction = true
                    navController.navigate("guide")
                },

                onProfileClick = {
                    isSlideAction = true
                    navController.navigate("profile")
                },

                onNotificationClick = {
                    isSlideAction = true
                    navController.navigate("notifications")
                },

                onViewAllReportsClick = {
                    isSlideAction = true
                    navController.navigate("report")
                }
            )
        }

        composable(
            route = "schedule",
            enterTransition = {
                if (isSlideAction) {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    )
                } else {
                    EnterTransition.None
                }
            },
            exitTransition = {
                if (isSlideAction) {
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    )
                } else {
                    ExitTransition.None
                }
            }
        ) {

            ScheduleScreen()
        }

        composable(
            route = "report",
            enterTransition = {
                if (isSlideAction) {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    )
                } else {
                    EnterTransition.None
                }
            },
            exitTransition = {
                if (isSlideAction) {
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    )
                } else {
                    ExitTransition.None
                }
            }
        ) {

            ReportScreen()
        }

        composable(
            route = "guide",
            enterTransition = {
                if (isSlideAction) {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    )
                } else {
                    EnterTransition.None
                }
            },
            exitTransition = {
                if (isSlideAction) {
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
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
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    )
                } else {
                    EnterTransition.None
                }
            },
            exitTransition = {
                if (isSlideAction) {
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    )
                } else {
                    ExitTransition.None
                }
            }
        ) {

            ProfileScreen(
                onNotificationsClick = {
                    isSlideAction = true
                    navController.navigate("notifications")
                }
            )
        }

        composable(
            route = "notifications",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            }
        ) {

            NotificationScreen(
                onBackToHome = {
                    isSlideAction = false
                    navController.navigate("home")
                }
            )
        }
    }
}