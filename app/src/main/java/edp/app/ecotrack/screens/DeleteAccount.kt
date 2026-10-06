package edp.app.ecotrack.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
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
fun DeleteAccountDialog(
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {

    var confirmationText by remember {
        mutableStateOf("")
    }

    val canDelete = confirmationText == "DELETE"

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
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(
                        EcoGreen,
                        RoundedCornerShape(50)
                    )
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier
                        .background(
                            Color(0xFFFFEEEE),
                            RoundedCornerShape(10.dp)
                        )
                        .padding(9.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Warning",
                        tint = Color(0xFFD9534F)
                    )
                }

                Spacer(
                    modifier = Modifier.padding(horizontal = 5.dp)
                )

                Column {

                    Text(
                        text = "Delete Account?",
                        color = Color(0xFFD9534F),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = "This permanently removes your resident profile, reports, and notification history.",
                        color = EcoTextGreen,
                        fontSize = 10.sp,
                        lineHeight = 14.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "⚠  Deleting your account cannot be undone.",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFFFEEEE),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(9.dp),
                color = Color(0xFFD9534F),
                fontSize = 10.sp
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Text(
                text = "Type DELETE to confirm",
                color = EcoDarkGreen,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            OutlinedTextField(
                value = confirmationText,
                onValueChange = {
                    confirmationText = it
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = {
                    Text(
                        text = "DELETE",
                        fontSize = 12.sp
                    )
                },
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {

                    Text(
                        text = "Keep account",
                        fontSize = 11.sp,
                        color = EcoDarkGreen
                    )
                }

                Button(
                    onClick = onDelete,
                    enabled = canDelete,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFB83A36),
                        disabledContainerColor = Color(0xFFE8C8C6)
                    )
                ) {

                    Text(
                        text = "Delete account",
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}