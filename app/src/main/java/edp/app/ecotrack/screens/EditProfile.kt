package edp.app.ecotrack.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import edp.app.ecotrack.ui.theme.EcoDarkGreen
import edp.app.ecotrack.ui.theme.EcoGreen
import edp.app.ecotrack.ui.theme.EcoLightGreen
import edp.app.ecotrack.ui.theme.EcoTextGreen

@Composable
fun EditProfileDialog(
    onDismiss: () -> Unit,
    onSave: (
        fullName: String,
        email: String,
        mobile: String,
        address: String
    ) -> Unit
) {

    var fullName by remember {
        mutableStateOf("Shania Castro")
    }

    var email by remember {
        mutableStateOf("shaniacastro@gmail.com")
    }

    var mobile by remember {
        mutableStateOf("0917 814 2210")
    }

    var address by remember {
        mutableStateOf("Zone 10, Barangay Bulua")
    }

    Dialog(
        onDismissRequest = onDismiss
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.White,
                    RoundedCornerShape(18.dp)
                )
                .padding(18.dp)
        ) {

            Spacer(
                modifier = Modifier
                    .size(
                        width = 44.dp,
                        height = 3.dp
                    )
                    .background(
                        EcoGreen,
                        RoundedCornerShape(50)
                    )
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(EcoLightGreen),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = EcoDarkGreen,
                        modifier = Modifier.size(21.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.size(10.dp)
                )

                Column {

                    Text(
                        text = "Edit Profile",
                        color = EcoDarkGreen,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Keep your resident information current.",
                        color = EcoTextGreen,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(EcoLightGreen),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                ) {

                    Text(
                        text = "SC",
                        color = EcoDarkGreen,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(
                    modifier = Modifier.size(8.dp)
                )

                Column {

                    Text(
                        text = "Shania Castro",
                        color = EcoDarkGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Resident profile",
                        color = EcoGreen,
                        fontSize = 9.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            ProfileField(
                label = "Full name",
                value = fullName,
                onValueChange = {
                    fullName = it
                }
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            ProfileField(
                label = "Email address",
                value = email,
                onValueChange = {
                    email = it
                }
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            ProfileField(
                label = "Mobile number",
                value = mobile,
                onValueChange = {
                    mobile = it
                }
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            ProfileField(
                label = "Home address",
                value = address,
                onValueChange = {
                    address = it
                }
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Barangay assignment is verified by the hall.",
                color = EcoTextGreen,
                fontSize = 8.sp
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(7.dp)
            ) {

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {

                    Text(
                        text = "Cancel",
                        fontSize = 10.sp,
                        color = EcoDarkGreen
                    )
                }

                Button(
                    onClick = {

                        onSave(
                            fullName,
                            email,
                            mobile,
                            address
                        )

                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EcoDarkGreen
                    )
                ) {

                    Text(
                        text = "Save changes",
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {

    Column {

        Text(
            text = label,
            color = EcoDarkGreen,
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(
            modifier = Modifier.height(3.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp),
            singleLine = true,
            shape = RoundedCornerShape(7.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 10.sp
            )
        )
    }
}