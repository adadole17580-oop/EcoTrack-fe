package edp.app.ecotrack.screens.auth

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import edp.app.ecotrack.components.auth.AuthButton
import edp.app.ecotrack.components.auth.AuthPage
import edp.app.ecotrack.components.auth.AuthTextField
import edp.app.ecotrack.network.ApiClient
import kotlinx.coroutines.launch
import org.json.JSONArray

private data class RegistrationBarangay(
    val id: Int,
    val name: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBackToLogin: () -> Unit
) {
    var fullName by rememberSaveable { mutableStateOf("") }
    var mobile by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var acceptedTerms by rememberSaveable { mutableStateOf(false) }
    var message by rememberSaveable { mutableStateOf("") }
    var isError by rememberSaveable { mutableStateOf(true) }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    var barangays by remember { mutableStateOf<List<RegistrationBarangay>>(emptyList()) }
    var selectedBarangayId by rememberSaveable { mutableStateOf(-1) }
    var selectedBarangayName by rememberSaveable { mutableStateOf("") }
    var barangayMenuExpanded by remember { mutableStateOf(false) }
    var isLoadingBarangays by remember { mutableStateOf(true) }
    var barangayLoadError by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            val result = ApiClient.getBarangays()
            val array: JSONArray = result.optJSONArray("barangays") ?: JSONArray()

            barangays = buildList {
                for (index in 0 until array.length()) {
                    val item = array.optJSONObject(index) ?: continue
                    val id = item.optInt("barangay_id", -1)
                    val name = item.optString("barangay_name").trim()

                    if (id > 0 && name.isNotBlank()) {
                        add(RegistrationBarangay(id, name))
                    }
                }
            }

            if (barangays.isEmpty()) {
                barangayLoadError = "No barangays are available."
            }
        } catch (exception: Exception) {
            barangayLoadError = exception.message
                ?: "Unable to load barangays. Please try again."
        } finally {
            isLoadingBarangays = false
        }
    }

    AuthPage(
        title = "Create your Account",
        subtitle = "Fill in your information. All fields are required.",
        topLabel = "WELCOME TO ECOTRACK!"
    ) {
        AuthTextField(
            label = "Full Name",
            value = fullName,
            onValueChange = { fullName = it },
            placeholder = "Enter your full name"
        )

        AuthTextField(
            label = "Mobile Number",
            value = mobile,
            onValueChange = {
                mobile = it.filter(Char::isDigit).take(15)
            },
            placeholder = "Enter mobile number",
            keyboardType = KeyboardType.Phone
        )

        AuthTextField(
            label = "Email Address",
            value = email,
            onValueChange = { email = it },
            placeholder = "Enter email address",
            keyboardType = KeyboardType.Email
        )

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Barangay",
                fontSize = 12.sp
            )

            ExposedDropdownMenuBox(
                expanded = barangayMenuExpanded,
                onExpandedChange = {
                    if (!isLoadingBarangays && barangays.isNotEmpty()) {
                        barangayMenuExpanded = it
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = when {
                        isLoadingBarangays -> "Loading barangays..."
                        selectedBarangayName.isNotBlank() -> selectedBarangayName
                        else -> "Select your barangay"
                    },
                    onValueChange = {},
                    readOnly = true,
                    enabled = !isLoadingBarangays && barangays.isNotEmpty(),
                    trailingIcon = {
                        if (isLoadingBarangays) {
                            CircularProgressIndicator()
                        } else {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = barangayMenuExpanded
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(
                            androidx.compose.material3.MenuAnchorType.PrimaryNotEditable
                        ),
                    singleLine = true,
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        focusedContainerColor = Color(0xFFF3FAF5),
                        unfocusedContainerColor = Color(0xFFF3FAF5),
                        disabledContainerColor = Color(0xFFF3FAF5)
                    )
                )

                ExposedDropdownMenu(
                    expanded = barangayMenuExpanded,
                    onDismissRequest = { barangayMenuExpanded = false }
                ) {
                    for (item in barangays) {
                        DropdownMenuItem(
                            text = {
                                Text(text = item.name)
                            },
                            onClick = {
                                selectedBarangayId = item.id
                                selectedBarangayName = item.name
                                barangayMenuExpanded = false
                                barangayLoadError = ""
                            }
                        )
                    }
                }
            }

            if (barangayLoadError.isNotBlank()) {
                Text(
                    text = barangayLoadError,
                    color = Color.Red,
                    fontSize = 12.sp
                )

                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            isLoadingBarangays = true
                            barangayLoadError = ""

                            try {
                                val result = ApiClient.getBarangays()
                                val array = result.optJSONArray("barangays")
                                    ?: JSONArray()

                                barangays = buildList {
                                    for (index in 0 until array.length()) {
                                        val item = array.optJSONObject(index)
                                            ?: continue
                                        val id = item.optInt("barangay_id", -1)
                                        val name = item.optString("barangay_name")
                                            .trim()

                                        if (id > 0 && name.isNotBlank()) {
                                            add(RegistrationBarangay(id, name))
                                        }
                                    }
                                }

                                if (barangays.isEmpty()) {
                                    barangayLoadError = "No barangays are available."
                                }
                            } catch (exception: Exception) {
                                barangayLoadError = exception.message
                                    ?: "Unable to load barangays. Please try again."
                            } finally {
                                isLoadingBarangays = false
                            }
                        }
                    }
                ) {
                    Text("Retry loading barangays")
                }
            }
        }

        AuthTextField(
            label = "Home Address",
            value = address,
            onValueChange = { address = it },
            placeholder = "House number, street, and sitio or purok"
        )

        AuthTextField(
            label = "Password",
            value = password,
            onValueChange = { password = it },
            placeholder = "Enter at least 8 characters",
            isPassword = true
        )

        AuthTextField(
            label = "Confirm Password",
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = "Confirm password",
            isPassword = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = acceptedTerms,
                onCheckedChange = { acceptedTerms = it }
            )

            Text(
                text = "I confirm that the information above is accurate and agree to use EcoTrack responsibly.",
                fontSize = 11.sp
            )
        }

        if (message.isNotBlank()) {
            Text(
                text = message,
                color = if (isError) Color.Red else Color(0xFF24652A),
                fontSize = 12.sp
            )
        }

        AuthButton(
            text = if (isLoading) "Creating Account..." else "Create Account",
            onClick = {
                if (isLoading) {
                    return@AuthButton
                }

                message = ""
                isError = true

                when {
                    fullName.isBlank() -> {
                        message = "Please enter your full name."
                    }

                    mobile.isBlank() -> {
                        message = "Please enter your mobile number."
                    }

                    email.isBlank() -> {
                        message = "Please enter your email address."
                    }

                    !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> {
                        message = "Please enter a valid email address."
                    }

                    selectedBarangayId <= 0 -> {
                        message = "Please select your barangay."
                    }

                    address.isBlank() -> {
                        message = "Please enter your home address."
                    }

                    password.length < 8 -> {
                        message = "Password must be at least 8 characters."
                    }

                    password != confirmPassword -> {
                        message = "Passwords do not match."
                    }

                    !acceptedTerms -> {
                        message = "Please confirm the information above."
                    }

                    else -> {
                        coroutineScope.launch {
                            isLoading = true

                            try {
                                val result = ApiClient.register(
                                    fullName = fullName,
                                    email = email.trim(),
                                    password = password,
                                    phone = mobile,
                                    barangayId = selectedBarangayId,
                                    address = address
                                )

                                message = result.optString(
                                    "message",
                                    "Account created successfully."
                                )

                                isError = false
                                password = ""
                                confirmPassword = ""
                            } catch (exception: Exception) {
                                message = exception.message
                                    ?: "Registration failed. Please try again."

                                isError = true
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                }
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Already have an account?",
                fontSize = 11.sp
            )

            TextButton(
                onClick = onBackToLogin
            ) {
                Text(
                    text = "Log In",
                    fontSize = 11.sp
                )
            }
        }
    }
}