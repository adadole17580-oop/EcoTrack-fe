
package edp.app.ecotrack.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.json.JSONObject

import edp.app.ecotrack.login.LoginScreen
import edp.app.ecotrack.login.ForgotPasswordScreen
import edp.app.ecotrack.login.OtpVerificationScreen
import edp.app.ecotrack.login.ResetPasswordScreen
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
import edp.app.ecotrack.screens.waste_report.WasteReport

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

    // Current admin session token
    val accessToken = rememberSaveable {
        mutableStateOf("")
    }

    // Dashboard statistics
    val dashboardStatistics = remember {
        mutableStateOf<JSONObject?>(null)
    }

    val statisticsLoading = remember {
        mutableStateOf(false)
    }

    val statisticsError = remember {
        mutableStateOf("")
    }

    // Actual waste reports for the dashboard
    val recentWasteReports = remember {
        mutableStateOf<List<WasteReport>>(emptyList())
    }

    val reportsLoading = remember {
        mutableStateOf(false)
    }

    val reportsError = remember {
        mutableStateOf("")
    }

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {

        // LOGIN
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginClick = { email, password, onResult ->
                    scope.launch {
                        try {
                            Log.d("ECO_TRACK", "Attempting admin login")

                            val response = ApiClient.login(
                                email = email,
                                password = password
                            )

                            val user = response.optJSONObject("user")
                            val role = user?.optString("role").orEmpty()

                            val session = response.optJSONObject("session")
                            val token = session
                                ?.optString("access_token")
                                .orEmpty()

                            if (token.isBlank()) {
                                onResult(
                                    "Login succeeded, but no access token was returned."
                                )
                                return@launch
                            }

                            if (role.equals("admin", ignoreCase = true)) {
                                accessToken.value = token
                                dashboardStatistics.value = null
                                statisticsError.value = ""
                                recentWasteReports.value = emptyList()
                                reportsError.value = ""

                                Log.d(
                                    "ECO_TRACK",
                                    "Admin login successful; token received"
                                )

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
                            Log.e(
                                "ECO_TRACK",
                                "Login error: ${e.message}",
                                e
                            )

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

        // FORGOT PASSWORD
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

        // OTP VERIFICATION
        composable(
            route = "${Routes.OTP}?email={email}",
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments
                ?.getString("email")
                .orEmpty()

            OtpVerificationScreen(
                email = email,
                onVerifyClick = { code ->
                    // Demo only: connect real OTP verification later.
                    if (code.length == 5 && code.all { it.isDigit() }) {
                        navController.navigate(Routes.RESET_PASSWORD)
                    }
                },
                onResendCodeClick = {
                    // Connect to backend when OTP delivery is implemented.
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // RESET PASSWORD
        composable(Routes.RESET_PASSWORD) {
            ResetPasswordScreen(
                onFinishClick = { newPassword ->
                    // Demo only: this does not save the password yet.
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

        // DASHBOARD
        composable(Routes.DASHBOARD) {

            LaunchedEffect(accessToken.value) {
                val token = accessToken.value

                if (token.isNotBlank()) {

                    // Load dashboard statistics
                    statisticsLoading.value = true
                    statisticsError.value = ""

                    try {
                        val response = ApiClient.getDashboardStats(
                            accessToken = token
                        )

                        Log.d(
                            "ECO_TRACK",
                            "Dashboard response: $response"
                        )

                        if (response.optString("status") == "OK") {
                            dashboardStatistics.value = response
                        } else {
                            dashboardStatistics.value = null
                            statisticsError.value = response.optString(
                                "message",
                                "Unable to load dashboard statistics."
                            )
                        }
                    } catch (e: Exception) {
                        Log.e(
                            "ECO_TRACK",
                            "Dashboard statistics request failed",
                            e
                        )

                        dashboardStatistics.value = null
                        statisticsError.value = e.message
                            ?: "Unable to load dashboard statistics."
                    } finally {
                        statisticsLoading.value = false
                    }

                    // Load actual waste reports
                    reportsLoading.value = true
                    reportsError.value = ""

                    try {
                        val response = ApiClient.getWasteReports(
                            accessToken = token
                        )

                        Log.d(
                            "ECO_TRACK",
                            "Recent waste reports response: $response"
                        )

                        if (response.optString("status") == "OK") {
                            val reportsArray =
                                response.optJSONArray("reports")

                            val loadedReports =
                                mutableListOf<WasteReport>()

                            if (reportsArray != null) {
                                for (i in 0 until reportsArray.length()) {
                                    val item =
                                        reportsArray.optJSONObject(i)
                                            ?: continue

                                    val reportId =
                                        item.optString("report_id", "")

                                    loadedReports.add(
                                        WasteReport(
                                            reportId = "#$reportId",
                                            resident = item.optString(
                                                "resident_name",
                                                "Unknown Resident"
                                            ),
                                            category = item.optString(
                                                "report_type",
                                                "Unknown"
                                            ),
                                            location = item.optString(
                                                "location_details",
                                                "Location unavailable"
                                            ),
                                            date = item.optString(
                                                "created_at",
                                                ""
                                            ),
                                            assignedTo = item.optString(
                                                "assigned_collector_name",
                                                "—"
                                            ).ifBlank { "—" },
                                            status = item.optString(
                                                "status",
                                                "Pending"
                                            )
                                        )
                                    )
                                }
                            }

                            recentWasteReports.value = loadedReports

                            Log.d(
                                "ECO_TRACK",
                                "Loaded ${loadedReports.size} recent waste reports"
                            )
                        } else {
                            recentWasteReports.value = emptyList()
                            reportsError.value = response.optString(
                                "message",
                                "Unable to load waste reports."
                            )
                        }
                    } catch (e: Exception) {
                        Log.e(
                            "ECO_TRACK",
                            "Recent waste reports request failed",
                            e
                        )

                        recentWasteReports.value = emptyList()
                        reportsError.value = e.message
                            ?: "Unable to load waste reports."
                    } finally {
                        reportsLoading.value = false
                    }

                } else {
                    Log.e(
                        "ECO_TRACK",
                        "Dashboard opened without an access token"
                    )

                    dashboardStatistics.value = null
                    statisticsError.value = "Please log in again."

                    recentWasteReports.value = emptyList()
                    reportsError.value = "Please log in again."
                }
            }

            DashboardScreen(
                admin = AdminInfo(
                    role = "ADMINISTRATOR",
                    name = "System Admin",
                    email = "admin@ecotrack.gov.ph"
                ),
                dashboardStatistics = dashboardStatistics.value,
                statisticsLoading = statisticsLoading.value,
                statisticsError = statisticsError.value,

                recentWasteReports = recentWasteReports.value,
                reportsLoading = reportsLoading.value,
                reportsError = reportsError.value,

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

        // WASTE REPORT
        composable(Routes.WASTE_REPORT) {
            WasteReportScreen(
                accessToken = accessToken.value,
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
                }
            )
        }

        // COLLECTORS
        composable(Routes.COLLECTORS) {
            CollectorsScreen(
                accessToken = accessToken.value,
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

        // RESIDENTS
        composable(Routes.RESIDENTS) {
            ResidentsScreen(
                accessToken = accessToken.value,
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

        // SCHEDULE
        composable(Routes.SCHEDULE) {
            ScheduleScreen(
                accessToken = accessToken.value,
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

        // ANALYTICS
        composable(Routes.ANALYTICS) {
            AnalyticsScreen(
                accessToken = accessToken.value,
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

        // WASTE GUIDE
        composable(Routes.WASTE_GUIDE) {
            WasteGuideScreen(
                accessToken = accessToken.value,
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

        // NOTIFICATIONS
        composable(Routes.NOTIFICATIONS) {
            NotificationsScreen(
                accessToken = accessToken.value,

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

        // MY PROFILE
        composable(Routes.PROFILE) {
            ProfileScreen(
                accessToken = accessToken.value,

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

                // LOG OUT
                onLogoutClick = {
                    // Clear the current app session
                    accessToken.value = ""

                    // Clear data loaded for the previous admin session
                    dashboardStatistics.value = null
                    statisticsError.value = ""
                    recentWasteReports.value = emptyList()
                    reportsError.value = ""

                    // Return to Sign In and remove protected screens
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
