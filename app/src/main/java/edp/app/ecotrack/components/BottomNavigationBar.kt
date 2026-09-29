package edp.app.ecotrack.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.Report
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BottomNavigationBar(
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {

    NavigationBar {

        NavigationBarItem(
            selected = selectedItem == "Home",
            onClick = {
                onItemSelected("Home")
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home"
                )
            },
            label = {
                Text("Home")
            }
        )

        NavigationBarItem(
            selected = selectedItem == "Schedule",
            onClick = {
                onItemSelected("Schedule")
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Schedule"
                )
            },
            label = {
                Text("Schedule")
            }
        )

        NavigationBarItem(
            selected = selectedItem == "Report",
            onClick = {
                onItemSelected("Report")
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Report,
                    contentDescription = "Report"
                )
            },
            label = {
                Text("Report")
            }
        )

        NavigationBarItem(
            selected = selectedItem == "Guide",
            onClick = {
                onItemSelected("Guide")
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Recycling,
                    contentDescription = "Guide"
                )
            },
            label = {
                Text("Guide")
            }
        )

        NavigationBarItem(
            selected = selectedItem == "Profile",
            onClick = {
                onItemSelected("Profile")
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.PersonOutline,
                    contentDescription = "Profile"
                )
            },
            label = {
                Text("Profile")
            }
        )
    }
}