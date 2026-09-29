package edp.app.ecotrack.navigation

import androidx.compose.runtime.Composable
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

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {

            HomeScreen(
                onScheduleClick = {
                    navController.navigate("schedule")
                },

                onReportClick = {
                    navController.navigate("report")
                },

                onGuideClick = {
                    navController.navigate("guide")
                },

                onProfileClick = {
                    navController.navigate("profile")
                },

                onNotificationClick = {
                    navController.navigate("notifications")
                },

                onViewAllReportsClick = {
                    navController.navigate("report")
                }
            )
        }

        composable("schedule") {

            ScheduleScreen()
        }

        composable("report") {

            ReportScreen()
        }

        composable("guide") {

            GuideScreen()
        }

        composable("profile") {

            ProfileScreen(
                onNotificationsClick = {
                    navController.navigate("notifications")
                }
            )
        }

        composable("notifications") {

            NotificationScreen(
                onBackToHome = {
                    navController.navigate("home")
                }
            )
        }
    }
}