package edp.app.ecotrack.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edp.app.ecotrack.ui.theme.EcoGreen
import edp.app.ecotrack.ui.theme.EcoWhite

@Composable
fun EcoTrackTopBar(
    title: String,
    subtitle: String? = null
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(EcoGreen)
            .statusBarsPadding()
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 14.dp,
                bottom = 18.dp
            )
    ) {

        Text(
            text = title,
            color = EcoWhite,
            style = MaterialTheme.typography.headlineSmall
        )

        if (subtitle != null) {

            Text(
                text = subtitle,
                color = EcoWhite.copy(alpha = 0.72f),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 3.dp)
            )
        }
    }
}