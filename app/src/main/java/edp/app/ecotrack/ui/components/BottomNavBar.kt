package edp.app.ecotrack.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Recycling
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edp.app.ecotrack.ui.theme.EcoSecondaryText
import edp.app.ecotrack.ui.theme.EcoText
import edp.app.ecotrack.ui.theme.EcoWhite

enum class BottomDestination {
    TASKS,
    GUIDE,
    SCHEDULE,
    PROFILE
}

@Composable
fun BottomNavBar(
    currentDestination: BottomDestination,
    onDestinationSelected: (BottomDestination) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(EcoWhite)
            .navigationBarsPadding()
            .padding(
                horizontal = 8.dp,
                vertical = 9.dp
            ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(
            selected = currentDestination == BottomDestination.TASKS,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Assignment,
                    contentDescription = "My Tasks",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = "My Tasks",
            onClick = {
                onDestinationSelected(BottomDestination.TASKS)
            }
        )

        BottomNavItem(
            selected = currentDestination == BottomDestination.GUIDE,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Recycling,
                    contentDescription = "Waste Guide",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = "Guide",
            onClick = {
                onDestinationSelected(BottomDestination.GUIDE)
            }
        )

        BottomNavItem(
            selected = currentDestination == BottomDestination.SCHEDULE,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = "Collection Schedule",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = "Schedule",
            onClick = {
                onDestinationSelected(BottomDestination.SCHEDULE)
            }
        )

        BottomNavItem(
            selected = currentDestination == BottomDestination.PROFILE,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = "Profile",
            onClick = {
                onDestinationSelected(BottomDestination.PROFILE)
            }
        )
    }
}

@Composable
private fun BottomNavItem(
    selected: Boolean,
    icon: @Composable () -> Unit,
    label: String,
    onClick: () -> Unit
) {
    val contentColor = if (selected) {
        EcoText
    } else {
        EcoSecondaryText
    }

    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(
                horizontal = 16.dp,
                vertical = 6.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor
        ) {
            icon()
        }

        Text(
            text = label,
            color = contentColor,
            style = MaterialTheme.typography.labelSmall
        )
    }
}