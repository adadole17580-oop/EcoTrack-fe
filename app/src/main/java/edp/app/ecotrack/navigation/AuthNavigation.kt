package edp.app.ecotrack.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edp.app.ecotrack.network.ApiClient
import edp.app.ecotrack.navigation.EcoTrackNavigation
import androidx.navigation.NavHostController
import edp.app.ecotrack.ResidentAppShell
import edp.app.ecotrack.screens.auth.WelcomeScreen
import edp.app.ecotrack.screens.auth.LoginScreen
import edp.app.ecotrack.screens.auth.RegisterScreen
import edp.app.ecotrack.screens.auth.ForgotPasswordScreen
import edp.app.ecotrack.screens.auth.VerifyOtpScreen
import edp.app.ecotrack.screens.auth.ResetPasswordScreen
import kotlinx.coroutines.launch

private object AuthRoutes {
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgot_password"
    const val VERIFY_OTP = "verify_otp"
    const val RESET_PASSWORD = "reset_password"
    const val HOME = "home"
    const val COLLECTOR_HOME = "collector_home"
}

@Composable
fun AuthNavigation() {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    var residentAccessToken by rememberSaveable {
        mutableStateOf("")
    }

    var loginMessage by remember { mutableStateOf("") }
    var isLoggingIn by remember { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = AuthRoutes.WELCOME
    ) {
        composable(AuthRoutes.WELCOME) {
            WelcomeScreen(
                onGetStartedClick = {
                    loginMessage = ""
                    navController.navigate(AuthRoutes.LOGIN)
                }
            )
        }

        composable(AuthRoutes.LOGIN) {
            LoginScreen(
                onRegisterClick = {
                    loginMessage = ""
                    navController.navigate(AuthRoutes.REGISTER)
                },
                onForgotPasswordClick = {
                    loginMessage = ""
                    navController.navigate(AuthRoutes.FORGOT_PASSWORD)
                },
                
            onLogin = { email, password ->
                if (!isLoggingIn) {
                    coroutineScope.launch {
                        isLoggingIn = true
                        loginMessage = ""
            
                        try {
                            val result = ApiClient.login(email, password)
            
                            val accessToken = result.optString("access_token")
                            if (accessToken.isBlank()) {
                                throw Exception(
                                    "No access token was returned by the server."
                                )
                            }
            
                            val user = result.optJSONObject("user")
                                ?: throw Exception(
                                    "User information was not returned by the server."
                                )
            
                            val role = user.optString("role")
                                .trim()
                                .lowercase()
            
                            val destination = when (role) {
                                "resident" -> AuthRoutes.HOME
                                "garbage_collector" -> AuthRoutes.COLLECTOR_HOME
                                else -> throw Exception(
                                    "Your account has an unrecognized role: $role"
                                )
                            }

                            if (role == "resident") {
                                residentAccessToken = accessToken
                            }
            
                            navController.navigate(destination) {
                                popUpTo(AuthRoutes.WELCOME) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        } catch (exception: Exception) {
                            loginMessage = exception.message
                                ?: "Login failed. Please try again."
                        } finally {
                            isLoggingIn = false
                        }
                    }
                }
            },
                loginMessage = loginMessage,
                isLoading = isLoggingIn
            )
        }

        composable(AuthRoutes.REGISTER) {
            RegisterScreen(
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(AuthRoutes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                onBackToLogin = {
                    navController.popBackStack()
                },
                onContinue = { email ->
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("recovery_email", email)

                    navController.navigate(AuthRoutes.VERIFY_OTP)
                }
            )
        }


        composable(AuthRoutes.VERIFY_OTP) {
            val email = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<String>("recovery_email")
                .orEmpty()

            VerifyOtpScreen(
                email = email,
                onBack = { navController.popBackStack() },
                onVerify = { accessToken, refreshToken ->
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("recovery_access_token", accessToken)

                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("recovery_refresh_token", refreshToken)

                    navController.navigate(AuthRoutes.RESET_PASSWORD)
                },
                onResendCode = {}
            )
        }

        composable(AuthRoutes.RESET_PASSWORD) {
            val accessToken = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<String>("recovery_access_token")
                .orEmpty()

            ResetPasswordScreen(
                accessToken = accessToken,
                onBackToLogin = {
                    navController.popBackStack(
                        AuthRoutes.LOGIN,
                        inclusive = false
                    )
                }
            )
        }

        composable(AuthRoutes.HOME) {
            ResidentAppShell(
                accessToken = residentAccessToken,
                onLogout = {
                    residentAccessToken = ""

                    navController.navigate(AuthRoutes.WELCOME) {
                        popUpTo(AuthRoutes.HOME) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(AuthRoutes.COLLECTOR_HOME) {
            CollectorHomeScreen(
                onLogout = {
                    navController.navigate(AuthRoutes.WELCOME) {
                        popUpTo(0) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Composable
fun EcoTrackApp(
    onLogout: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "EcoTrack",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Resident Dashboard",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Welcome! You are logged in as a resident."
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onLogout
        ) {
            Text("Log Out")
        }
    }
}

@Composable
private fun CollectorHomeScreen(
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "EcoTrack",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Garbage Collector Dashboard",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Welcome! You are logged in as a garbage collector."
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onLogout
        ) {
            Text("Log Out")
        }
    }
}
