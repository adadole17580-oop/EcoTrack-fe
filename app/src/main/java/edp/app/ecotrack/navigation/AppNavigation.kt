package edp.app.ecotrack.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import edp.app.ecotrack.login.LoginScreen
import edp.app.ecotrack.login.ForgotPasswordScreen
import edp.app.ecotrack.login.OtpVerificationScreen
import edp.app.ecotrack.login.ResetPasswordScreen
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import edp.app.ecotrack.network.ApiClient

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
    const val LOGIN = "login"
    const val FORGOT_PASSWORD = "forgot_password"
    const val OTP = "otp_verification"
    const val RESET_PASSWORD = "reset_password"

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
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {


        // =====================================================
        // LOGIN
        // =====================================================

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginClick = { email, password, onResult ->

                    scope.launch {
                        try {
                            // Authenticate through the backend.
                            val response = ApiClient.login(
                                email = email,
                                password = password
                            )

                            val user = response.optJSONObject("user")
                            val role = user?.optString("role").orEmpty()

                            if (role == "admin") {
                                // Login successful: close the login screen.
                                navController.navigate(Routes.DASHBOARD) {
                                    popUpTo(Routes.LOGIN) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }

                                onResult(null)
                            } else {
                                onResult("Admin access only.")
                            }

                        } catch (e: Exception) {
                            onResult(
                                e.message ?: "Login failed. Please try again."
                            )
                        }
                    }
                },

                onForgotPasswordClick = {
                    navController.navigate(Routes.FORGOT_PASSWORD)
                }
            )
        }


        // =====================================================
        // FORGOT PASSWORD
        // =====================================================

        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                onContinueClick = { email ->
                    val encodedEmail = Uri.encode(email.trim())
                    navController.navigate(
                        "${Routes.OTP}?email=$encodedEmail"
                    )
                },
                onBackToLoginClick = {
                    navController.popBackStack()
                }
            )
        }

        // =====================================================
        // OTP VERIFICATION
        // =====================================================

        composable(
            route = "${Routes.OTP}?email={email}",
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email").orEmpty()

            OtpVerificationScreen(
                email = email,
                onVerifyClick = { code ->
                    // FRONTEND DEMO ONLY:
                    // Continue when a five-digit code is entered.
                    // Actual OTP verification needs a backend.
                    if (code.length == 5 && code.all { it.isDigit() }) {
                        navController.navigate(Routes.RESET_PASSWORD)
                    }
                },
                onResendCodeClick = {
                    // Connect to the backend when OTP delivery is implemented.
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // =====================================================
        // RESET PASSWORD + SUCCESS SCREEN
        // Both screens are handled inside ResetPasswordScreen.
        // =====================================================

        composable(Routes.RESET_PASSWORD) {
            ResetPasswordScreen(
                onFinishClick = { newPassword ->
                    // Frontend demo only.
                    // The password is not saved to an account yet.
                },
                onBackToLoginClick = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

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
                onViewAllClick = {
                    navController.navigate(Routes.WASTE_REPORT)
                },
                onNotificationClick = {
                    navController.navigate(Routes.NOTIFICATIONS)
                },
                onProfileClick = {
                    navController.navigate(Routes.PROFILE)
                },
                onWasteReportClick = {
                    navController.navigate(Routes.WASTE_REPORT)
                },
                onCollectorsClick = {
                    navController.navigate(Routes.COLLECTORS)
                },
                onResidentsClick = {
                    navController.navigate(Routes.RESIDENTS)
                },
                onSchedulesClick = {
                    navController.navigate(Routes.SCHEDULE)
                },
                onAnalyticsClick = {
                    navController.navigate(Routes.ANALYTICS)
                },
                onWasteGuideClick = {
                    navController.navigate(Routes.WASTE_GUIDE)
                }
            )
        }

        // =====================================================
        // WASTE REPORT
        // =====================================================

        composable(Routes.WASTE_REPORT) {
            WasteReportScreen(
                onNotificationClick = {
                    navController.navigate(Routes.NOTIFICATIONS)
                },
                onProfileClick = {
                    navController.navigate(Routes.PROFILE)
                },
                onDashboardClick = {
                    navController.navigate(Routes.DASHBOARD)
                },
                onCollectorsClick = {
                    navController.navigate(Routes.COLLECTORS)
                },
                onResidentsClick = {
                    navController.navigate(Routes.RESIDENTS)
                },
                onScheduleClick = {
                    navController.navigate(Routes.SCHEDULE)
                },
                onAnalyticsClick = {
                    navController.navigate(Routes.ANALYTICS)
                },
                onWasteGuideClick = {
                    navController.navigate(Routes.WASTE_GUIDE)
                },
                onReviewClick = {
                    // Existing review popup placeholder.
                },
                onAssignClick = {
                    // Existing assign popup placeholder.
                },
                onViewClick = {
                    // Existing view popup placeholder.
                }
            )
        }

        // =====================================================
        // COLLECTORS
        // =====================================================

        composable(Routes.COLLECTORS) {
            CollectorsScreen(
                onNotificationClick = {
                    navController.navigate(Routes.NOTIFICATIONS)
                },
                onProfileClick = {
                    navController.navigate(Routes.PROFILE)
                },
                onDashboardClick = {
                    navController.navigate(Routes.DASHBOARD)
                },
                onWasteReportClick = {
                    navController.navigate(Routes.WASTE_REPORT)
                },
                onResidentsClick = {
                    navController.navigate(Routes.RESIDENTS)
                },
                onScheduleClick = {
                    navController.navigate(Routes.SCHEDULE)
                },
                onAnalyticsClick = {
                    navController.navigate(Routes.ANALYTICS)
                },
                onWasteGuideClick = {
                    navController.navigate(Routes.WASTE_GUIDE)
                }
            )
        }

        // =====================================================
        // RESIDENTS
        // =====================================================

        composable(Routes.RESIDENTS) {
            ResidentsScreen(
                onNotificationClick = {
                    navController.navigate(Routes.NOTIFICATIONS)
                },
                onProfileClick = {
                    navController.navigate(Routes.PROFILE)
                },
                onDashboardClick = {
                    navController.navigate(Routes.DASHBOARD)
                },
                onWasteReportClick = {
                    navController.navigate(Routes.WASTE_REPORT)
                },
                onCollectorsClick = {
                    navController.navigate(Routes.COLLECTORS)
                },
                onScheduleClick = {
                    navController.navigate(Routes.SCHEDULE)
                },
                onAnalyticsClick = {
                    navController.navigate(Routes.ANALYTICS)
                },
                onWasteGuideClick = {
                    navController.navigate(Routes.WASTE_GUIDE)
                }
            )
        }

        // =====================================================
        // SCHEDULE
        // =====================================================

        composable(Routes.SCHEDULE) {
            ScheduleScreen(
                onNotificationClick = {
                    navController.navigate(Routes.NOTIFICATIONS)
                },
                onProfileClick = {
                    navController.navigate(Routes.PROFILE)
                },
                onDashboardClick = {
                    navController.navigate(Routes.DASHBOARD)
                },
                onWasteReportClick = {
                    navController.navigate(Routes.WASTE_REPORT)
                },
                onCollectorsClick = {
                    navController.navigate(Routes.COLLECTORS)
                },
                onResidentsClick = {
                    navController.navigate(Routes.RESIDENTS)
                },
                onAnalyticsClick = {
                    navController.navigate(Routes.ANALYTICS)
                },
                onWasteGuideClick = {
                    navController.navigate(Routes.WASTE_GUIDE)
                }
            )
        }

        // =====================================================
        // ANALYTICS
        // =====================================================

        composable(Routes.ANALYTICS) {
            AnalyticsScreen(
                onNotificationClick = {
                    navController.navigate(Routes.NOTIFICATIONS)
                },
                onProfileClick = {
                    navController.navigate(Routes.PROFILE)
                },
                onDashboardClick = {
                    navController.navigate(Routes.DASHBOARD)
                },
                onWasteReportClick = {
                    navController.navigate(Routes.WASTE_REPORT)
                },
                onCollectorsClick = {
                    navController.navigate(Routes.COLLECTORS)
                },
                onResidentsClick = {
                    navController.navigate(Routes.RESIDENTS)
                },
                onScheduleClick = {
                    navController.navigate(Routes.SCHEDULE)
                },
                onWasteGuideClick = {
                    navController.navigate(Routes.WASTE_GUIDE)
                }
            )
        }

        // =====================================================
        // WASTE GUIDE
        // =====================================================

        composable(Routes.WASTE_GUIDE) {
            WasteGuideScreen(
                onNotificationClick = {
                    navController.navigate(Routes.NOTIFICATIONS)
                },
                onProfileClick = {
                    navController.navigate(Routes.PROFILE)
                },
                onDashboardClick = {
                    navController.navigate(Routes.DASHBOARD)
                },
                onWasteReportClick = {
                    navController.navigate(Routes.WASTE_REPORT)
                },
                onCollectorsClick = {
                    navController.navigate(Routes.COLLECTORS)
                },
                onResidentsClick = {
                    navController.navigate(Routes.RESIDENTS)
                },
                onScheduleClick = {
                    navController.navigate(Routes.SCHEDULE)
                },
                onAnalyticsClick = {
                    navController.navigate(Routes.ANALYTICS)
                }
            )
        }

        // =====================================================
        // NOTIFICATIONS
        // =====================================================

        composable(Routes.NOTIFICATIONS) {
            NotificationsScreen(
                onDashboardClick = {
                    navController.navigate(Routes.DASHBOARD)
                },
                onWasteReportClick = {
                    navController.navigate(Routes.WASTE_REPORT)
                },
                onCollectorsClick = {
                    navController.navigate(Routes.COLLECTORS)
                },
                onResidentsClick = {
                    navController.navigate(Routes.RESIDENTS)
                },
                onScheduleClick = {
                    navController.navigate(Routes.SCHEDULE)
                },
                onAnalyticsClick = {
                    navController.navigate(Routes.ANALYTICS)
                },
                onWasteGuideClick = {
                    navController.navigate(Routes.WASTE_GUIDE)
                },
                onNotificationClick = {
                    navController.navigate(Routes.NOTIFICATIONS)
                },
                onProfileClick = {
                    navController.navigate(Routes.PROFILE)
                }
            )
        }

        // =====================================================
        // MY PROFILE
        // =====================================================

        composable(Routes.PROFILE) {
            ProfileScreen(
                onDashboardClick = {
                    navController.navigate(Routes.DASHBOARD)
                },
                onWasteReportClick = {
                    navController.navigate(Routes.WASTE_REPORT)
                },
                onCollectorsClick = {
                    navController.navigate(Routes.COLLECTORS)
                },
                onResidentsClick = {
                    navController.navigate(Routes.RESIDENTS)
                },
                onScheduleClick = {
                    navController.navigate(Routes.SCHEDULE)
                },
                onAnalyticsClick = {
                    navController.navigate(Routes.ANALYTICS)
                },
                onWasteGuideClick = {
                    navController.navigate(Routes.WASTE_GUIDE)
                },
                onNotificationClick = {
                    navController.navigate(Routes.NOTIFICATIONS)
                }
            )
        }
    }
}
