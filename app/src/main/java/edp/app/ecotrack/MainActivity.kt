package edp.app.ecotrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import edp.app.ecotrack.components.residents.BottomNavigationBar
import edp.app.ecotrack.navigation.AuthNavigation
import edp.app.ecotrack.navigation.EcoTrackNavigation
import edp.app.ecotrack.ui.theme.EcoTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcoTrackTheme {
                AuthNavigation()
            }
        }
    }
}

@Composable
fun ResidentAppShell(
    accessToken: String,
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val selectedItem = when (currentRoute) {
        "home" -> "Home"
        "schedule" -> "Schedule"
        "report" -> "Report"
        "guide" -> "Guide"
        "profile" -> "Profile"
        else -> ""
    }

    Scaffold(
        bottomBar = {
            if (currentRoute in listOf(
                    "home",
                    "schedule",
                    "report",
                    "guide",
                    "profile"
                )
            ) {
                BottomNavigationBar(
                    selectedItem = selectedItem,
                    onItemSelected = { item ->
                        val route = when (item) {
                            "Home" -> "home"
                            "Schedule" -> "schedule"
                            "Report" -> "report"
                            "Guide" -> "guide"
                            "Profile" -> "profile"
                            else -> "home"
                        }

                        if (currentRoute != route) {
                            navController.navigate(route) {
                                popUpTo("home") {
                                    saveState = false
                                }
                                launchSingleTop = true
                                restoreState = false
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            EcoTrackNavigation(
                navController = navController,
                accessToken = accessToken,
                onLogout = onLogout
            )
        }
    }
}
