package edp.app.ecotrack.screens.residents

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.CancellationSignal
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import edp.app.ecotrack.network.ApiClient
import edp.app.ecotrack.ui.theme.EcoDarkGreen
import edp.app.ecotrack.ui.theme.EcoGreen
import edp.app.ecotrack.ui.theme.EcoSubmitGreen
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import org.json.JSONObject
import kotlin.coroutines.resume

private data class DetectedLocation(
    val latitude: Double,
    val longitude: Double,
    val address: String
)

private suspend fun detectDeviceLocation(
    context: Context
): DetectedLocation = withContext(Dispatchers.IO) {
    val fineGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val coarseGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    if (!fineGranted && !coarseGranted) {
        throw IllegalStateException("Location permission is required.")
    }

    val locationManager = context.getSystemService(
        Context.LOCATION_SERVICE
    ) as LocationManager

    val providers = listOf(
        LocationManager.NETWORK_PROVIDER,
        LocationManager.GPS_PROVIDER
    ).filter { provider ->
        try {
            locationManager.isProviderEnabled(provider)
        } catch (_: Exception) {
            false
        }
    }

    var detected: Location? = null

    for (provider in providers) {
        detected = try {
            withTimeoutOrNull(15000L) {
                suspendCancellableCoroutine { continuation ->
                    val cancellationSignal = CancellationSignal()

                    continuation.invokeOnCancellation {
                        cancellationSignal.cancel()
                    }

                    try {
                        locationManager.getCurrentLocation(
                            provider,
                            cancellationSignal,
                            context.mainExecutor
                        ) { location ->
                            if (continuation.isActive) {
                                continuation.resume(location)
                            }
                        }
                    } catch (e: SecurityException) {
                        if (continuation.isActive) {
                            continuation.resume(null)
                        }
                    }
                }
            }
        } catch (_: Exception) {
            null
        }

        if (detected != null) break
    }

    val location = detected ?: throw IllegalStateException(
        "Unable to detect your location. Turn on GPS and try again."
    )

    val address = try {
        if (Geocoder.isPresent()) {
            @Suppress("DEPRECATION")
            Geocoder(context).getFromLocation(
                location.latitude,
                location.longitude,
                1
            )?.firstOrNull()?.let { formatAddress(it) }
        } else {
            null
        }
    } catch (_: Exception) {
        null
    }

    DetectedLocation(
        latitude = location.latitude,
        longitude = location.longitude,
        address = address.orEmpty()
    )
}

private fun formatAddress(address: Address): String {
    val parts = mutableListOf<String>()

    for (index in 0..address.maxAddressLineIndex) {
        address.getAddressLine(index)?.let { line ->
            if (line.isNotBlank() && line !in parts) {
                parts.add(line)
            }
        }
    }

    if (parts.isEmpty()) {
        listOf(
            address.subLocality,
            address.locality,
            address.subAdminArea,
            address.adminArea
        ).forEach { part ->
            if (!part.isNullOrBlank() && part !in parts) {
                parts.add(part)
            }
        }
    }

    return parts.joinToString(", ")
}

@Composable
fun NewReportScreen(
    accessToken: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedIssue by remember {
        mutableStateOf("Illegal Dumping")
    }

    var locationText by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var photoUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var selectedBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var latitude by remember {
        mutableStateOf<Double?>(null)
    }

    var longitude by remember {
        mutableStateOf<Double?>(null)
    }

    var isLocating by remember {
        mutableStateOf(false)
    }

    var isSubmitting by remember {
        mutableStateOf(false)
    }

    var submitSuccess by remember {
        mutableStateOf(false)
    }

    var statusMessage by remember {
        mutableStateOf("")
    }

    suspend fun refreshLocation() {
        isLocating = true
        statusMessage = ""

        try {
            val result = detectDeviceLocation(context)
            latitude = result.latitude
            longitude = result.longitude

            if (result.address.isNotBlank()) {
                locationText = result.address
            } else {
                locationText = String.format(
                    java.util.Locale.US,
                    "%.6f, %.6f",
                    result.latitude,
                    result.longitude
                )
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            latitude = null
            longitude = null
            statusMessage = e.message
                ?: "Could not detect your location. Please try again."
        } finally {
            isLocating = false
        }
    }

    val locationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val granted =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                        permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (granted) {
                scope.launch {
                    refreshLocation()
                }
            } else {
                statusMessage = "Allow location access to attach GPS coordinates to your report."
            }
        }

    val photoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            photoUri = uri
        }

    LaunchedEffect(photoUri) {
        selectedBitmap = null

        val uri = photoUri

        if (uri != null) {
            selectedBitmap = withContext(Dispatchers.IO) {
                context.contentResolver.openInputStream(uri)?.use {
                    BitmapFactory.decodeStream(it)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        val fineGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineGranted || coarseGranted) {
            refreshLocation()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    val hasPhoto = photoUri != null

    val backendIssue = when (selectedIssue) {
        "Overflowing Bin" -> "Overflowing Garbage"
        "Missed Collection" -> "Uncollected Garbage"
        "Unsegregated Waste" -> "Other"
        else -> "Illegal Dumping"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(EcoDarkGreen)
                .padding(
                    start = 12.dp,
                    end = 24.dp,
                    top = 28.dp,
                    bottom = 20.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                enabled = !isSubmitting
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "New Report",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = "Submit a waste management issue in your area",
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "ISSUE TYPE",
                color = EcoDarkGreen,
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IssueButton(
                        text = "Illegal Dumping",
                        selected = selectedIssue == "Illegal Dumping",
                        onClick = { selectedIssue = "Illegal Dumping" },
                        modifier = Modifier.weight(1f)
                    )

                    IssueButton(
                        text = "Overflowing Bin",
                        selected = selectedIssue == "Overflowing Bin",
                        onClick = { selectedIssue = "Overflowing Bin" },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IssueButton(
                        text = "Missed Collection",
                        selected = selectedIssue == "Missed Collection",
                        onClick = { selectedIssue = "Missed Collection" },
                        modifier = Modifier.weight(1f)
                    )

                    IssueButton(
                        text = "Unsegregated Waste",
                        selected = selectedIssue == "Unsegregated Waste",
                        onClick = { selectedIssue = "Unsegregated Waste" },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "LOCATION / BARANGAY ZONE",
                color = EcoDarkGreen,
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = locationText,
                onValueChange = { locationText = it },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = EcoGreen
                    )
                },
                placeholder = {
                    Text("Waiting for GPS location...")
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EcoGreen,
                    unfocusedBorderColor = Color(0xFFC8DEC2),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = when {
                        isLocating -> "Detecting your location..."
                        latitude != null && longitude != null ->
                            "GPS coordinates detected"
                        else -> "GPS location not detected"
                    },
                    modifier = Modifier.weight(1f),
                    color = if (latitude != null && longitude != null) {
                        EcoDarkGreen
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    style = MaterialTheme.typography.bodySmall
                )

                Button(
                    onClick = {
                        val fineGranted = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED

                        val coarseGranted = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED

                        if (fineGranted || coarseGranted) {
                            scope.launch {
                                refreshLocation()
                            }
                        } else {
                            locationPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                    },
                    enabled = !isLocating && !isSubmitting,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EcoGreen
                    )
                ) {
                    if (isLocating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Detect GPS")
                    }
                }
            }

            if (latitude != null && longitude != null) {
                Text(
                    text = String.format(
                        java.util.Locale.US,
                        "Latitude: %.6f  |  Longitude: %.6f",
                        latitude!!,
                        longitude!!
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "DESCRIPTION",
                color = EcoDarkGreen,
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                placeholder = {
                    Text("Describe the waste issue or details...")
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EcoGreen,
                    unfocusedBorderColor = Color(0xFFC8DEC2),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "ATTACH PHOTO",
                color = EcoDarkGreen,
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clickable(enabled = !isSubmitting && !submitSuccess) {
                        photoPickerLauncher.launch("image/*")
                    },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (hasPhoto) {
                        Color.White
                    } else {
                        EcoSubmitGreen.copy(alpha = 0.5f)
                    }
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 0.dp
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedBitmap != null) {
                        Image(
                            bitmap = selectedBitmap!!.asImageBitmap(),
                            contentDescription = "Selected photo",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(14.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddAPhoto,
                                contentDescription = "Upload photo",
                                tint = EcoDarkGreen,
                                modifier = Modifier.size(36.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Tap to upload a photo",
                                color = EcoDarkGreen,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "PNG or JPG up to 10MB",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (statusMessage.isNotBlank()) {
                Text(
                    text = statusMessage,
                    color = if (submitSuccess) {
                        EcoDarkGreen
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    when {
                        description.isBlank() -> {
                            statusMessage = "Please describe the waste issue."
                            submitSuccess = false
                        }

                        locationText.isBlank() -> {
                            statusMessage = "Please detect or enter the location."
                            submitSuccess = false
                        }

                        latitude == null || longitude == null -> {
                            statusMessage = "Detect your GPS location before submitting."
                            submitSuccess = false
                        }

                        accessToken.isBlank() -> {
                            statusMessage = "Your session has expired. Please log in again."
                            submitSuccess = false
                        }

                        else -> {
                            scope.launch {
                                isSubmitting = true
                                statusMessage = ""
                                submitSuccess = false

                                try {
                                    ApiClient.submitResidentReport(
                                        context = context,
                                        accessToken = accessToken,
                                        reportType = backendIssue,
                                        description = description.trim(),
                                        photoUri = photoUri,
                                        locationDetails = locationText.trim(),
                                        latitude = latitude,
                                        longitude = longitude
                                    )

                                    submitSuccess = true
                                    statusMessage = "Report submitted successfully!"
                                } catch (e: CancellationException) {
                                    throw e
                                } catch (e: Exception) {
                                    statusMessage = e.message
                                        ?.takeIf { it.isNotBlank() }
                                        ?: "Unable to submit the report. Please try again."
                                } finally {
                                    isSubmitting = false
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !isSubmitting && !submitSuccess,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EcoGreen,
                    disabledContainerColor = EcoSubmitGreen,
                    disabledContentColor = EcoDarkGreen
                )
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Submitting...")
                } else {
                    Text(
                        text = "Submit Report",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            if (submitSuccess) {
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EcoDarkGreen
                    )
                ) {
                    Text("Back to Reports")
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun IssueButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                EcoGreen
            } else {
                EcoSubmitGreen.copy(alpha = 0.6f)
            }
        ),
        border = if (selected) {
            BorderStroke(1.dp, EcoDarkGreen)
        } else {
            null
        },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Box(
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 12.dp
            ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = if (selected) {
                    Color.White
                } else {
                    EcoDarkGreen
                },
                style = MaterialTheme.typography.bodySmall,
                fontWeight = if (selected) {
                    FontWeight.Bold
                } else {
                    FontWeight.Medium
                }
            )
        }
    }
}