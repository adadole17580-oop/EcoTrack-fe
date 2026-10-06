package edp.app.ecotrack.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import edp.app.ecotrack.ui.theme.EcoDarkGreen
import edp.app.ecotrack.ui.theme.EcoGreen
import edp.app.ecotrack.ui.theme.EcoLightGreen
import edp.app.ecotrack.ui.theme.EcoTextGreen

@Composable
fun MyBarangayDialog(
    onDismiss: () -> Unit
) {

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = true
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.White,
                    RoundedCornerShape(18.dp)
                )
                .padding(22.dp)
        ) {

            Spacer(
                modifier = Modifier
                    .size(
                        width = 44.dp,
                        height = 4.dp
                    )
                    .background(
                        EcoGreen,
                        RoundedCornerShape(50)
                    )
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            EcoLightGreen,
                            RoundedCornerShape(50)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Barangay",
                        tint = EcoGreen,
                        modifier = Modifier.size(23.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.size(12.dp)
                )

                Column {

                    Text(
                        text = "My Barangay",
                        color = EcoDarkGreen,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = "Your assigned community and key resident information.",
                        color = EcoTextGreen,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        EcoLightGreen,
                        RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                EcoDarkGreen,
                                RoundedCornerShape(50)
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = "BB",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(
                        modifier = Modifier.size(12.dp)
                    )

                    Column {

                        Text(
                            text = "Barangay Bulua",
                            color = EcoDarkGreen,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Zone 10, Cagayan de Oro City",
                            color = EcoTextGreen,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.White)
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column {

                        Text(
                            text = "Resident since",
                            color = EcoTextGreen,
                            fontSize = 10.sp
                        )

                        Text(
                            text = "June 2021",
                            color = EcoDarkGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.End
                    ) {

                        Text(
                            text = "Household ID",
                            color = EcoTextGreen,
                            fontSize = 10.sp
                        )

                        Text(
                            text = "BLU-10-0428",
                            color = EcoDarkGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            BarangayInfoRow(
                icon = Icons.Default.Person,
                label = "Barangay Captain",
                value = "Hon. Pedro M. Abellanosa"
            )

            BarangayInfoRow(
                icon = Icons.Default.Recycling,
                label = "Waste collection",
                value = "Tuesday, Thursday & Saturday"
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = EcoDarkGreen
                    ),
                    shape = RoundedCornerShape(9.dp)
                ) {

                    Text(
                        text = "Close"
                    )
                }

                Button(
                    onClick = {
                        // Details screen can be connected later
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EcoDarkGreen
                    ),
                    shape = RoundedCornerShape(9.dp)
                ) {

                    Text(
                        text = "View details"
                    )
                }
            }
        }
    }
}

@Composable
private fun BarangayInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier
                .size(34.dp)
                .background(
                    EcoLightGreen,
                    RoundedCornerShape(9.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = EcoDarkGreen,
                modifier = Modifier.size(19.dp)
            )
        }

        Spacer(
            modifier = Modifier.size(12.dp)
        )

        Column {

            Text(
                text = label,
                color = EcoTextGreen,
                fontSize = 11.sp
            )

            Text(
                text = value,
                color = EcoDarkGreen,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}