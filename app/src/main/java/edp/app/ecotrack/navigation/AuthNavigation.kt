package edp.app.ecotrack.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edp.app.ecotrack.screens.auth.ForgotPasswordScreen
import edp.app.ecotrack.screens.auth.LoginScreen
import edp.app.ecotrack.screens.auth.RegisterScreen
import edp.app.ecotrack.screens.auth.ResetPasswordScreen
import edp.app.ecotrack.screens.auth.VerifyOtpScreen
import edp.app.ecotrack.screens.auth.WelcomeScreen

private object AuthRoutes {
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgot-password"
    const val VERIFY_OTP = "verify-otp"
    const val RESET_PASSWORD = "reset-password"
}

@Composable
fun AuthNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AuthRoutes.WELCOME
    ) {
        composable(AuthRoutes.WELCOME) {
            WelcomeScreen(
                onGetStartedClick = {
                    navController.navigate(AuthRoutes.LOGIN)
                }
            )
        }

        composable(AuthRoutes.LOGIN) {
            LoginScreen(
                onRegisterClick = {
                    navController.navigate(AuthRoutes.REGISTER)
                },
                onForgotPasswordClick = {
                    navController.navigate(AuthRoutes.FORGOT_PASSWORD)
                },
                onLogin = { _, _ ->
                    // Connect this callback to your authentication API later.
                }
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
                onBack = {
                    navController.popBackStack()
                },
                onVerify = {
                    navController.navigate(AuthRoutes.RESET_PASSWORD)
                },
                onResendCode = {
                    // Connect this callback to your OTP API later.
                }
            )
        }

        composable(AuthRoutes.RESET_PASSWORD) {
            ResetPasswordScreen(
                onBackToLogin = {
                    navController.navigate(AuthRoutes.LOGIN) {
                        popUpTo(AuthRoutes.WELCOME)
                    }
                }
            )
        }
    }
}
