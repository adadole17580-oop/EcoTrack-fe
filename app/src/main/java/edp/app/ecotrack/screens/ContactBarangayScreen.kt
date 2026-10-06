package edp.app.ecotrack.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import edp.app.ecotrack.ui.theme.EcoDarkGreen
import edp.app.ecotrack.ui.theme.EcoGreen
import edp.app.ecotrack.ui.theme.EcoLightGreen
import edp.app.ecotrack.ui.theme.EcoTextGreen

data class BarangayMessage(
    val text: String,
    val time: String,
    val isFromUser: Boolean
)

@Composable
fun ContactBarangayDialog(
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        ContactBarangayScreen(
            onBack = onDismiss
        )
    }
}

@Composable
fun ContactBarangayScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current


    val barangayPhoneNumber = "0888801234"

    var messageText by remember {
        mutableStateOf("")
    }


    val messages = remember {
        mutableStateListOf<BarangayMessage>()
    }

    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {

        if (messages.isNotEmpty()) {

            listState.animateScrollToItem(
                messages.size - 1
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoLightGreen)
            .imePadding()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(EcoDarkGreen)
                .padding(
                    horizontal = 10.dp,
                    vertical = 10.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBack
            ) {

                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(25.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "BB",
                    color = EcoDarkGreen,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }


            Spacer(
                modifier = Modifier.width(11.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Barangay Bulua Hall",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF7BE495))
                    )

                    Spacer(
                        modifier = Modifier.width(5.dp)
                    )

                    Text(
                        text = "Online",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 11.sp
                    )
                }
            }

            IconButton(
                onClick = {

                    val intent = Intent(
                        Intent.ACTION_DIAL,
                        Uri.parse(
                            "tel:$barangayPhoneNumber"
                        )
                    )

                    context.startActivity(intent)
                }
            ) {

                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Call Barangay Hall",
                    tint = Color.White,
                    modifier = Modifier.size(23.dp)
                )
            }

            IconButton(
                onClick = {

                }
            ) {

                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = Color.White
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    top = 14.dp,
                    end = 16.dp
                ),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(14.dp)
            ) {

                Text(
                    text = "Barangay Bulua Hall",
                    color = EcoDarkGreen,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = EcoGreen,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(5.dp)
                    )

                    Text(
                        text = "Zone 7, Bulua, Cagayan de Oro City",
                        color = EcoTextGreen,
                        fontSize = 10.sp
                    )
                }

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Phone",
                        tint = EcoGreen,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(5.dp)
                    )

                    Text(
                        text = "(088) 880 1234",
                        color = EcoTextGreen,
                        fontSize = 10.sp
                    )
                }
            }
        }

        if (messages.isEmpty()) {

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.ChatBubbleOutline,
                            contentDescription = "No messages",
                            tint = EcoGreen,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Text(
                        text = "No conversations yet",
                        color = EcoDarkGreen,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text = "Send a message to Barangay Bulua Hall to report concerns or ask about local services.",
                        color = EcoTextGreen,
                        fontSize = 12.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        lineHeight = 16.sp
                    )
                }
            }
        } else {

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(
                        horizontal = 12.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(9.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    top = 14.dp,
                    bottom = 12.dp
                )
            ) {

                item {

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "Today",
                            color = EcoTextGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                items(messages) { message ->

                    MessageBubble(
                        message = message
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(
                    horizontal = 10.dp,
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
                        text = "Type a message...",
                        color = EcoTextGreen,
                        fontSize = 13.sp
                    )
                },
                shape = RoundedCornerShape(24.dp),
                singleLine = true
            )


            Spacer(
                modifier = Modifier.width(7.dp)
            )

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(
                        if (messageText.isNotBlank()) {
                            EcoGreen
                        } else {
                            Color(0xFFB7C8BC)
                        }
                    )
                    .then(
                        if (messageText.isNotBlank()) {
                            Modifier
                        } else {
                            Modifier
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {

                IconButton(
                    onClick = {

                        if (messageText.isNotBlank()) {

                            messages.add(
                                BarangayMessage(
                                    text = messageText.trim(),
                                    time = "Now",
                                    isFromUser = true
                                )
                            )

                            messageText = ""
                        }
                    },
                    enabled = messageText.isNotBlank()
                ) {

                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send message",
                        tint = Color.White,
                        modifier = Modifier.size(21.dp)
                    )
                }
            }
        }
    }
}
@Composable
private fun MessageBubble(
    message: BarangayMessage
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isFromUser) {
            Arrangement.End
        } else {
            Arrangement.Start
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth(
                    if (message.isFromUser) {
                        0.78f
                    } else {
                        0.80f
                    }
                )
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (message.isFromUser) {
                            16.dp
                        } else {
                            4.dp
                        },
                        bottomEnd = if (message.isFromUser) {
                            4.dp
                        } else {
                            16.dp
                        }
                    )
                )
                .background(
                    if (message.isFromUser) {
                        Color(0xFFE0F0E2)
                    } else {
                        Color.White
                    }
                )
                .border(
                    width = if (message.isFromUser) {
                        0.dp
                    } else {
                        1.dp
                    },
                    color = if (message.isFromUser) {
                        Color.Transparent
                    } else {
                        Color(0xFFD8E4DA)
                    },
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (message.isFromUser) {
                            16.dp
                        } else {
                            4.dp
                        },
                        bottomEnd = if (message.isFromUser) {
                            4.dp
                        } else {
                            16.dp
                        }
                    )
                )
                .padding(
                    horizontal = 13.dp,
                    vertical = 10.dp
                )
        ) {

            Text(
                text = message.text,
                color = Color(0xFF25352A),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (message.isFromUser) {
                    Arrangement.End
                } else {
                    Arrangement.Start
                },
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = message.time,
                    color = EcoTextGreen,
                    fontSize = 9.sp
                )

                if (message.isFromUser) {

                    Spacer(
                        modifier = Modifier.width(5.dp)
                    )

                    Text(
                        text = "✓✓",
                        color = EcoGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}