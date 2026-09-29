package edp.app.ecotrack.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import edp.app.ecotrack.ui.theme.EcoGreen
import edp.app.ecotrack.ui.theme.EcoGreenLight
import edp.app.ecotrack.ui.theme.EcoSecondaryText
import edp.app.ecotrack.ui.theme.EcoText
import edp.app.ecotrack.ui.theme.EcoWhite

private data class ChatMessage(
    val message: String,
    val fromCollector: Boolean
)

@Composable
fun ContactAdminScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current

    var messageText by remember {
        mutableStateOf("")
    }

    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                message = "Hello! How can I help you?",
                fromCollector = false
            )
        )
    }

    val adminNumber = "09170000000"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .imePadding()
    ) {

        /*
         * HEADER
         */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 10.dp,
                    top = 6.dp,
                    bottom = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBack
            ) {

                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Back",
                    tint = EcoText
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Contact Admin",
                    style = MaterialTheme.typography.titleLarge,
                    color = EcoText
                )

                Text(
                    text = "Chat with the administrator",
                    style = MaterialTheme.typography.bodySmall,
                    color = EcoSecondaryText,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            IconButton(
                onClick = {

                    val intent = Intent(
                        Intent.ACTION_DIAL,
                        Uri.parse("tel:$adminNumber")
                    )

                    context.startActivity(intent)
                }
            ) {

                Icon(
                    imageVector = Icons.Outlined.Call,
                    contentDescription = "Call admin",
                    tint = EcoGreen
                )
            }
        }

        /*
         * ADMIN INFO
         */
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 4.dp
                ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = EcoGreenLight
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            color = EcoWhite,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "A",
                        style = MaterialTheme.typography.titleMedium,
                        color = EcoGreen
                    )
                }

                Column(
                    modifier = Modifier.padding(start = 12.dp)
                ) {

                    Text(
                        text = "Administrator",
                        style = MaterialTheme.typography.titleMedium,
                        color = EcoText
                    )

                    Text(
                        text = "Available for support",
                        style = MaterialTheme.typography.bodySmall,
                        color = EcoSecondaryText,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }

        /*
         * CHAT
         */
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(
                items = messages
            ) { chatMessage ->

                ChatBubble(
                    message = chatMessage
                )
            }
        }

        /*
         * MESSAGE INPUT
         */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                ),
            verticalAlignment = Alignment.Bottom
        ) {

            OutlinedTextField(
                value = messageText,
                onValueChange = {
                    messageText = it
                },
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        text = "Type a message..."
                    )
                },
                maxLines = 4,
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(
                modifier = Modifier.width(6.dp)
            )

            IconButton(
                onClick = {

                    if (messageText.isNotBlank()) {

                        messages.add(
                            ChatMessage(
                                message = messageText.trim(),
                                fromCollector = true
                            )
                        )

                        messageText = ""
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = EcoGreen,
                        shape = CircleShape
                    )
            ) {

                Icon(
                    imageVector = Icons.Outlined.Send,
                    contentDescription = "Send message",
                    tint = EcoWhite
                )
            }
        }
    }
}

@Composable
private fun ChatBubble(
    message: ChatMessage
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.fromCollector) {
            Arrangement.End
        } else {
            Arrangement.Start
        }
    ) {

        Box(
            modifier = Modifier
                .background(
                    color = if (message.fromCollector) {
                        EcoGreen
                    } else {
                        EcoWhite
                    },
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (message.fromCollector) {
                            16.dp
                        } else {
                            4.dp
                        },
                        bottomEnd = if (message.fromCollector) {
                            4.dp
                        } else {
                            16.dp
                        }
                    )
                )
                .padding(
                    horizontal = 14.dp,
                    vertical = 10.dp
                )
        ) {

            Text(
                text = message.message,
                style = MaterialTheme.typography.bodyMedium,
                color = if (message.fromCollector) {
                    EcoWhite
                } else {
                    EcoText
                }
            )
        }
    }
}