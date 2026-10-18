package edp.app.ecotrack

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import edp.app.ecotrack.navigation.EcoTrackNavigation

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleAuthDeepLink(intent)

        setContent {
            EcoTrackTheme {
                AuthNavigation()
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleAuthDeepLink(intent)
    }

    private fun handleAuthDeepLink(intent: Intent?) {
        val uri = intent?.data ?: return

        if (uri.scheme == "ecotrack" &&
            uri.host == "auth" &&
            uri.path == "/callback"
        ) {
        }
    }
}
