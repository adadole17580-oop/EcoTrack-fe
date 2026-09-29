package edp.app.ecotrack.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import edp.app.ecotrack.screens.analytics.AnalyticsScreen
import edp.app.ecotrack.screens.collectors.CollectorsScreen
import edp.app.ecotrack.screens.dashboard.AdminInfo
import edp.app.ecotrack.screens.dashboard.DashboardScreen
import edp.app.ecotrack.screens.notifications.NotificationsScreen
import edp.app.ecotrack.screens.profile.ProfileScreen
import edp.app.ecotrack.screens.residents.ResidentsScreen
import edp.app.ecotrack.screens.schedule.ScheduleScreen
import edp.app.ecotrack.screens.waste_guide.WasteGuideScreen
import edp.app.ecotrack.screens.waste_report.WasteReportScreen

object Routes {

    const val DASHBOARD = "dashboard"
    const val WASTE_REPORT = "waste_report"
    const val COLLECTORS = "collectors"
    const val RESIDENTS = "residents"
    const val SCHEDULE = "schedule"
    const val ANALYTICS = "analytics"
    const val WASTE_GUIDE = "waste_guide"
    const val NOTIFICATIONS = "notifications"
    const val PROFILE = "profile"
}

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD
    ) {

        // =====================================================
        // DASHBOARD
        // =====================================================

        composable(Routes.DASHBOARD) {

            DashboardScreen(

                admin = AdminInfo(
                    role = "ADMINISTRATOR",
                    name = "System Admin",
                    email = "admin@ecotrack.gov.ph"
                ),

                // VIEW ALL
                onViewAllClick = {
                    navController.navigate(
                        Routes.WASTE_REPORT
                    )
                },

                // BELL
                onNotificationClick = {
                    navController.navigate(
                        Routes.NOTIFICATIONS
                    )
                },

                // PROFILE
                onProfileClick = {
                    navController.navigate(
                        Routes.PROFILE
                    )
                },

                // SIDEBAR
                onWasteReportClick = {
                    navController.navigate(
                        Routes.WASTE_REPORT
                    )
                },

                onCollectorsClick = {
                    navController.navigate(
                        Routes.COLLECTORS
                    )
                },

                onResidentsClick = {
                    navController.navigate(
                        Routes.RESIDENTS
                    )
                },

                onSchedulesClick = {
                    navController.navigate(
                        Routes.SCHEDULE
                    )
                },

                onAnalyticsClick = {
                    navController.navigate(
                        Routes.ANALYTICS
                    )
                },

                onWasteGuideClick = {
                    navController.navigate(
                        Routes.WASTE_GUIDE
                    )
                }
            )
        }

        // =====================================================
        // WASTE REPORT
        // =====================================================

        composable(Routes.WASTE_REPORT) {

            WasteReportScreen(

                onNotificationClick = {
                    navController.navigate(
                        Routes.NOTIFICATIONS
                    )
                },

                onProfileClick = {
                    navController.navigate(
                        Routes.PROFILE
                    )
                },

                onDashboardClick = {
                    navController.navigate(
                        Routes.DASHBOARD
                    )
                },

                onCollectorsClick = {
                    navController.navigate(
                        Routes.COLLECTORS
                    )
                },

                onResidentsClick = {
                    navController.navigate(
                        Routes.RESIDENTS
                    )
                },

                onScheduleClick = {
                    navController.navigate(
                        Routes.SCHEDULE
                    )
                },

                onAnalyticsClick = {
                    navController.navigate(
                        Routes.ANALYTICS
                    )
                },

                onWasteGuideClick = {
                    navController.navigate(
                        Routes.WASTE_GUIDE
                    )
                },

                // These will be connected to the Figma popups next.
                onReviewClick = {
                    // Review popup
                },

                onAssignClick = {
                    // Assign popup
                },

                onViewClick = {
                    // View popup
                }
            )
        }

        // =====================================================
        // COLLECTORS
        // =====================================================

        composable(Routes.COLLECTORS) {

            CollectorsScreen(

                // BELL
                onNotificationClick = {
                    navController.navigate(
                        Routes.NOTIFICATIONS
                    )
                },

                // PROFILE
                onProfileClick = {
                    navController.navigate(
                        Routes.PROFILE
                    )
                },

                // SIDEBAR
                onDashboardClick = {
                    navController.navigate(
                        Routes.DASHBOARD
                    )
                },

                onWasteReportClick = {
                    navController.navigate(
                        Routes.WASTE_REPORT
                    )
                },

                onResidentsClick = {
                    navController.navigate(
                        Routes.RESIDENTS
                    )
                },

                onScheduleClick = {
                    navController.navigate(
                        Routes.SCHEDULE
                    )
                },

                onAnalyticsClick = {
                    navController.navigate(
                        Routes.ANALYTICS
                    )
                },

                onWasteGuideClick = {
                    navController.navigate(
                        Routes.WASTE_GUIDE
                    )
                }
            )
        }

        // =====================================================
        // RESIDENTS
        // =====================================================

        composable(Routes.RESIDENTS) {

            ResidentsScreen(

                // BELL
                onNotificationClick = {
                    navController.navigate(
                        Routes.NOTIFICATIONS
                    )
                },

                // PROFILE
                onProfileClick = {
                    navController.navigate(
                        Routes.PROFILE
                    )
                },

                // SIDEBAR
                onDashboardClick = {
                    navController.navigate(
                        Routes.DASHBOARD
                    )
                },

                onWasteReportClick = {
                    navController.navigate(
                        Routes.WASTE_REPORT
                    )
                },

                onCollectorsClick = {
                    navController.navigate(
                        Routes.COLLECTORS
                    )
                },

                onScheduleClick = {
                    navController.navigate(
                        Routes.SCHEDULE
                    )
                },

                onAnalyticsClick = {
                    navController.navigate(
                        Routes.ANALYTICS
                    )
                },

                onWasteGuideClick = {
                    navController.navigate(
                        Routes.WASTE_GUIDE
                    )
                }
            )
        }

        // =====================================================
        // SCHEDULE
        // =====================================================

        composable(Routes.SCHEDULE) {

            ScheduleScreen(

                // BELL
                onNotificationClick = {
                    navController.navigate(
                        Routes.NOTIFICATIONS
                    )
                },

                // PROFILE
                onProfileClick = {
                    navController.navigate(
                        Routes.PROFILE
                    )
                },

                // SIDEBAR
                onDashboardClick = {
                    navController.navigate(
                        Routes.DASHBOARD
                    )
                },

                onWasteReportClick = {
                    navController.navigate(
                        Routes.WASTE_REPORT
                    )
                },

                onCollectorsClick = {
                    navController.navigate(
                        Routes.COLLECTORS
                    )
                },

                onResidentsClick = {
                    navController.navigate(
                        Routes.RESIDENTS
                    )
                },

                onAnalyticsClick = {
                    navController.navigate(
                        Routes.ANALYTICS
                    )
                },

                onWasteGuideClick = {
                    navController.navigate(
                        Routes.WASTE_GUIDE
                    )
                }
            )
        }

        // =====================================================
        // ANALYTICS
        // =====================================================

        composable(Routes.ANALYTICS) {
            AnalyticsScreen(

                // BELL
                onNotificationClick = {
                    navController.navigate(
                        Routes.NOTIFICATIONS
                    )
                },

                // PROFILE
                onProfileClick = {
                    navController.navigate(
                        Routes.PROFILE
                    )
                },

                // DASHBOARD
                onDashboardClick = {
                    navController.navigate(
                        Routes.DASHBOARD
                    )
                },

                // WASTE REPORT
                onWasteReportClick = {
                    navController.navigate(
                        Routes.WASTE_REPORT
                    )
                },

                // COLLECTORS
                onCollectorsClick = {
                    navController.navigate(
                        Routes.COLLECTORS
                    )
                },

                // RESIDENTS
                onResidentsClick = {
                    navController.navigate(
                        Routes.RESIDENTS
                    )
                },

                // SCHEDULES
                onScheduleClick = {
                    navController.navigate(
                        Routes.SCHEDULE
                    )
                },

                // WASTE GUIDE
                onWasteGuideClick = {
                    navController.navigate(
                        Routes.WASTE_GUIDE
                    )
                }
            )
        }

        // =====================================================
        // WASTE GUIDE
        // =====================================================

        composable(Routes.WASTE_GUIDE) {
            WasteGuideScreen(

                // BELL
                onNotificationClick = {
                    navController.navigate(
                        Routes.NOTIFICATIONS
                    )
                },

                // PROFILE
                onProfileClick = {
                    navController.navigate(
                        Routes.PROFILE
                    )
                },

                // DASHBOARD
                onDashboardClick = {
                    navController.navigate(
                        Routes.DASHBOARD
                    )
                },

                // WASTE REPORT
                onWasteReportClick = {
                    navController.navigate(
                        Routes.WASTE_REPORT
                    )
                },

                // COLLECTORS
                onCollectorsClick = {
                    navController.navigate(
                        Routes.COLLECTORS
                    )
                },

                // RESIDENTS
                onResidentsClick = {
                    navController.navigate(
                        Routes.RESIDENTS
                    )
                },

                // SCHEDULES
                onScheduleClick = {
                    navController.navigate(
                        Routes.SCHEDULE
                    )
                },

                // ANALYTICS
                onAnalyticsClick = {
                    navController.navigate(
                        Routes.ANALYTICS
                    )
                }
            )
        }

        // =====================================================
        // NOTIFICATIONS
        // =====================================================

        composable(Routes.NOTIFICATIONS) {
            NotificationsScreen(

                // DASHBOARD
                onDashboardClick = {
                    navController.navigate(
                        Routes.DASHBOARD
                    )
                },

                // WASTE REPORT
                onWasteReportClick = {
                    navController.navigate(
                        Routes.WASTE_REPORT
                    )
                },

                // COLLECTORS
                onCollectorsClick = {
                    navController.navigate(
                        Routes.COLLECTORS
                    )
                },

                // RESIDENTS
                onResidentsClick = {
                    navController.navigate(
                        Routes.RESIDENTS
                    )
                },

                // SCHEDULES
                onScheduleClick = {
                    navController.navigate(
                        Routes.SCHEDULE
                    )
                },

                // ANALYTICS
                onAnalyticsClick = {
                    navController.navigate(
                        Routes.ANALYTICS
                    )
                },

                // WASTE GUIDE
                onWasteGuideClick = {
                    navController.navigate(
                        Routes.WASTE_GUIDE
                    )
                },

                // BELL
                onNotificationClick = {
                    navController.navigate(
                        Routes.NOTIFICATIONS
                    )
                },

                // PROFILE
                onProfileClick = {
                    navController.navigate(
                        Routes.PROFILE
                    )
                }
            )
        }

        // =====================================================
        // MY PROFILE
        // =====================================================

        composable(Routes.PROFILE) {

            ProfileScreen(

                onDashboardClick = {
                    navController.navigate(
                        Routes.DASHBOARD
                    )
                },

                onWasteReportClick = {
                    navController.navigate(
                        Routes.WASTE_REPORT
                    )
                },

                onCollectorsClick = {
                    navController.navigate(
                        Routes.COLLECTORS
                    )
                },

                onResidentsClick = {
                    navController.navigate(
                        Routes.RESIDENTS
                    )
                },

                onScheduleClick = {
                    navController.navigate(
                        Routes.SCHEDULE
                    )
                },

                onAnalyticsClick = {
                    navController.navigate(
                        Routes.ANALYTICS
                    )
                },

                onWasteGuideClick = {
                    navController.navigate(
                        Routes.WASTE_GUIDE
                    )
                },

                onNotificationClick = {
                    navController.navigate(
                        Routes.NOTIFICATIONS
                    )
                }
            )
        }
    }
}