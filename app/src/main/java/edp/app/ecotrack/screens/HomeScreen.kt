package edp.app.ecotrack.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edp.app.ecotrack.ui.theme.EcoDarkGreen
import edp.app.ecotrack.ui.theme.EcoPaleGreen
import edp.app.ecotrack.ui.theme.EcoTextGreen

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoPaleGreen)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Welcome to EcoTrack",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = EcoDarkGreen,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Track your waste, make a bigger impact.",
            fontSize = 14.sp,
            color = EcoTextGreen,
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}
