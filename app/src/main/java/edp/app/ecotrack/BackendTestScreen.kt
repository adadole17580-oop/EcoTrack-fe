
package edp.app.ecotrack

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edp.app.ecotrack.network.ApiClient
import kotlinx.coroutines.launch

@Composable
fun BackendTestScreen() {
    var result by remember {
        mutableStateOf("Tap the button to test your backend.")
    }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "EcoTrack Backend Test",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            enabled = !isLoading,
            onClick = {
                scope.launch {
                    isLoading = true
                    result = try {
                        ApiClient.testConnection()
                    } catch (e: Exception) {
                        "Connection failed: ${e.message ?: "Unknown error"}"
                    }
                    isLoading = false
                }
            }
        ) {
            Text("Test Backend Connection")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(12.dp))
        }

        Text(text = result)
    }
}
