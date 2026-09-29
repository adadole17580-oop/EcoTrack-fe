package edp.app.ecotrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import edp.app.ecotrack.ui.navigation.AppNavigation
import edp.app.ecotrack.ui.theme.EcoTrackTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            EcoTrackTheme {
                AppNavigation()
            }
        }
    }
}