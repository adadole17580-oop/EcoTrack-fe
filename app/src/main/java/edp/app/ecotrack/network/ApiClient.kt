package edp.app.ecotrack.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import android.content.Context
import android.net.Uri
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

object ApiClient {
       var BASE_URL = "http://192.168.0.187:3000/"
    private const val TAG = "EcoTrackAPI"

    private val client = OkHttpClient()

    suspend fun login(
        email: String,
        password: String
    ): JSONObject = withContext(Dispatchers.IO) {
        val json = JSONObject().apply {
            put("email", email.trim())
            put("password", password)
        }

        postRequest("/api/auth/login", json, "Login")
    }

    suspend fun register(
        fullName: String,
        email: String,
        password: String,
        phone: String,
        barangayId: Int,
        address: String
    ): JSONObject = withContext(Dispatchers.IO) {
        val json = JSONObject().apply {
            put("full_name", fullName.trim())
            put("email", email.trim())
            put("password", password)
            put("phone", phone.trim())
            put("barangay_id", barangayId)
            put("address", address.trim())
        }

        postRequest("/api/auth/register", json, "Registration")
    }

    suspend fun getBarangays(): JSONObject = withContext(Dispatchers.IO) {
        val fullUrl = "${BASE_URL.trimEnd('/')}/api/auth/barangays"

        val request = Request.Builder()
            .url(fullUrl)
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            val responseText = response.body?.string().orEmpty()
            val result = if (responseText.isNotBlank()) {
                JSONObject(responseText)
            } else {
                JSONObject()
            }

            if (!response.isSuccessful) {
                throw IOException(
                    result.optString("message")
                        .ifBlank { "Unable to load barangays." }
                )
            }

            result
        }
    }

    suspend fun getResidentDashboard(accessToken: String): JSONObject =
        withContext(Dispatchers.IO) {
            val fullUrl =
                "${BASE_URL.trimEnd('/')}/api/residents/dashboard"

            val request = Request.Builder()
                .url(fullUrl)
                .header("Authorization", "Bearer ${accessToken.trim()}")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                val responseText = response.body?.string().orEmpty()

                val result = if (responseText.isNotBlank()) {
                    JSONObject(responseText)
                } else {
                    JSONObject()
                }

                if (!response.isSuccessful) {
                    val message = result.optString("message")
                        .ifBlank { "Unable to load resident dashboard." }

                    throw IOException(message)
                }

                result
            }
        }

    suspend fun getResidentSchedules(accessToken: String): JSONObject =
        withContext(Dispatchers.IO) {
            val fullUrl = "${BASE_URL.trimEnd('/')}/api/residents/schedules"

            val request = Request.Builder()
                .url(fullUrl)
                .header("Authorization", "Bearer ${accessToken.trim()}")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                val responseText = response.body?.string().orEmpty()
                val result = if (responseText.isNotBlank()) {
                    JSONObject(responseText)
                } else {
                    JSONObject()
                }

                if (!response.isSuccessful) {
                    val message = result.optString("message")
                        .ifBlank { "Unable to load collection schedules." }

                    throw IOException(message)
                }

                result
            }
        }

    suspend fun getResidentReports(
        accessToken: String
    ): JSONObject = withContext(Dispatchers.IO) {
        val fullUrl = "${BASE_URL.trimEnd('/')}/api/residents/reports"

        val request = Request.Builder()
            .url(fullUrl)
            .header("Authorization", "Bearer ${accessToken.trim()}")
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            val responseText = response.body?.string().orEmpty()
            val result = if (responseText.isNotBlank()) {
                JSONObject(responseText)
            } else {
                JSONObject()
            }

            if (!response.isSuccessful) {
                throw IOException(
                    result.optString("message").ifBlank {
                        "Unable to load your reports."
                    }
                )
            }

            result
        }
    }

    suspend fun getResidentReport(
        accessToken: String,
        reportId: Long
    ): JSONObject = withContext(Dispatchers.IO) {
        val fullUrl =
            "${BASE_URL.trimEnd('/')}/api/residents/reports/$reportId"

        val request = Request.Builder()
            .url(fullUrl)
            .header("Authorization", "Bearer ${accessToken.trim()}")
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            val responseText = response.body?.string().orEmpty()
            val result = if (responseText.isNotBlank()) {
                JSONObject(responseText)
            } else {
                JSONObject()
            }

            if (!response.isSuccessful) {
                throw IOException(
                    result.optString("message").ifBlank {
                        "Unable to load report details."
                    }
                )
            }

            result
        }
    }

    suspend fun getResidentNotifications(accessToken: String): JSONObject =
        withContext(Dispatchers.IO) {
            val fullUrl = "${BASE_URL.trimEnd('/')}/api/residents/notifications"

            val request = Request.Builder()
                .url(fullUrl)
                .header("Authorization", "Bearer ${accessToken.trim()}")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                val responseText = response.body?.string().orEmpty()

                val result = if (responseText.isNotBlank()) {
                    JSONObject(responseText)
                } else {
                    JSONObject()
                }

                if (!response.isSuccessful) {
                    val message = result.optString("message")
                        .ifBlank { "Unable to load your notifications." }

                    throw IOException(message)
                }

                result
            }
        }

    suspend fun getResidentGuide(): JSONObject = withContext(Dispatchers.IO) {
        val fullUrl = "${BASE_URL.trimEnd('/')}/api/residents/guide"

        val request = Request.Builder()
            .url(fullUrl)
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            val responseText = response.body?.string().orEmpty()

            val result = if (responseText.isNotBlank()) {
                JSONObject(responseText)
            } else {
                JSONObject()
            }

            if (!response.isSuccessful) {
                val message = result.optString("message")
                    .ifBlank { "Unable to load waste segregation guide." }

                throw IOException(message)
            }

            result
        }
    }


    suspend fun submitResidentReport(
        context: Context,
        accessToken: String,
        reportType: String,
        description: String,
        photoUri: Uri? = null,
        locationDetails: String? = null,
        latitude: Double? = null,
        longitude: Double? = null
    ): JSONObject = withContext(Dispatchers.IO) {
        val builder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("report_type", reportType)
            .addFormDataPart("description", description.trim())

        if (!locationDetails.isNullOrBlank()) {
            builder.addFormDataPart("location_details", locationDetails.trim())
        }

        if (latitude != null) {
            builder.addFormDataPart("latitude", latitude.toString())
        }

        if (longitude != null) {
            builder.addFormDataPart("longitude", longitude.toString())
        }

        if (photoUri != null) {
            val contentResolver = context.contentResolver
            val mimeType = contentResolver.getType(photoUri)
                ?: throw IOException("Unable to determine the photo type.")

            if (mimeType !in listOf("image/jpeg", "image/png", "image/webp")) {
                throw IOException("Please select a JPG, PNG, or WebP photo.")
            }

            val temporaryFile = File.createTempFile("ecotrack_report_", ".tmp", context.cacheDir)

            try {
                contentResolver.openInputStream(photoUri)?.use { input ->
                    temporaryFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                } ?: throw IOException("Unable to read the selected photo.")

                if (temporaryFile.length() > 8L * 1024 * 1024) {
                    throw IOException("The photo must not exceed 8 MB.")
                }

                val mediaType = mimeType.toMediaType()
                val fileBody = temporaryFile.asRequestBody(mediaType)

                builder.addFormDataPart(
                    "photo",
                    "report_photo.${mimeType.substringAfter("/")}",
                    fileBody
                )

                val request = Request.Builder()
                    .url("${BASE_URL.trimEnd('/')}/api/residents/reports")
                    .header("Authorization", "Bearer ${accessToken.trim()}")
                    .post(builder.build())
                    .build()

                client.newCall(request).execute().use { response ->
                    val responseText = response.body?.string().orEmpty()
                    val result = if (responseText.isNotBlank()) {
                        JSONObject(responseText)
                    } else {
                        JSONObject()
                    }

                    if (!response.isSuccessful) {
                        throw IOException(
                            result.optString("message").ifBlank {
                                "Unable to submit your report."
                            }
                        )
                    }

                    result
                }
            } finally {
                temporaryFile.delete()
            }
        } else {
            val request = Request.Builder()
                .url("${BASE_URL.trimEnd('/')}/api/residents/reports")
                .header("Authorization", "Bearer ${accessToken.trim()}")
                .post(builder.build())
                .build()

            client.newCall(request).execute().use { response ->
                val responseText = response.body?.string().orEmpty()
                val result = if (responseText.isNotBlank()) {
                    JSONObject(responseText)
                } else {
                    JSONObject()
                }

                if (!response.isSuccessful) {
                    throw IOException(
                        result.optString("message").ifBlank {
                            "Unable to submit your report."
                        }
                    )
                }

                result
            }
        }
    }


    suspend fun getResidentProfile(accessToken: String): JSONObject =
        withContext(Dispatchers.IO) {
            val fullUrl = "${BASE_URL.trimEnd('/')}/api/residents/profile"

            val request = Request.Builder()
                .url(fullUrl)
                .header("Authorization", "Bearer ${accessToken.trim()}")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                val responseText = response.body?.string().orEmpty()

                val result = if (responseText.isNotBlank()) {
                    JSONObject(responseText)
                } else {
                    JSONObject()
                }

                if (!response.isSuccessful) {
                    val message = result.optString("message")
                        .ifBlank { "Unable to load your profile." }

                    throw IOException(message)
                }

                result
            }
        }


    suspend fun changeResidentPassword(
        accessToken: String,
        currentPassword: String,
        newPassword: String
    ): JSONObject = withContext(Dispatchers.IO) {
        val fullUrl = "${BASE_URL.trimEnd('/')}/api/residents/change-password"

        val body = JSONObject().apply {
            put("current_password", currentPassword)
            put("new_password", newPassword)
        }

        val request = Request.Builder()
            .url(fullUrl)
            .header("Authorization", "Bearer ${accessToken.trim()}")
            .put(
                body.toString().toRequestBody(
                    "application/json; charset=utf-8".toMediaType()
                )
            )
            .build()

        client.newCall(request).execute().use { response ->
            val responseText = response.body?.string().orEmpty()
            val result = if (responseText.isNotBlank()) {
                JSONObject(responseText)
            } else {
                JSONObject()
            }

            if (!response.isSuccessful) {
                throw IOException(
                    result.optString("message")
                        .ifBlank { "Unable to change your password." }
                )
            }

            result
        }
    }
    suspend fun getResidentBarangays(accessToken: String): JSONObject =
        withContext(Dispatchers.IO) {
            val fullUrl = "${BASE_URL.trimEnd('/')}/api/residents/barangays"

            val request = Request.Builder()
                .url(fullUrl)
                .header("Authorization", "Bearer ${accessToken.trim()}")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                val responseText = response.body?.string().orEmpty()
                val result = if (responseText.isNotBlank()) {
                    JSONObject(responseText)
                } else {
                    JSONObject()
                }

                if (!response.isSuccessful) {
                    throw IOException(
                        result.optString("message")
                            .ifBlank { "Unable to load barangays." }
                    )
                }

                result
            }
        }

    suspend fun updateResidentProfile(
        accessToken: String,
        fullName: String,
        contactNumber: String,
        address: String,
        barangayId: Int
    ): JSONObject = withContext(Dispatchers.IO) {
        val fullUrl = "${BASE_URL.trimEnd('/')}/api/residents/profile"

        val body = JSONObject().apply {
            put("full_name", fullName.trim())
            put("contact_number", contactNumber.trim())
            put("address", address.trim())
            put("barangay_id", barangayId)
        }

        val request = Request.Builder()
            .url(fullUrl)
            .header("Authorization", "Bearer ${accessToken.trim()}")
            .put(
                body.toString().toRequestBody(
                    "application/json; charset=utf-8".toMediaType()
                )
            )
            .build()

        client.newCall(request).execute().use { response ->
            val responseText = response.body?.string().orEmpty()
            val result = if (responseText.isNotBlank()) {
                JSONObject(responseText)
            } else {
                JSONObject()
            }

            if (!response.isSuccessful) {
                throw IOException(
                    result.optString("message")
                        .ifBlank { "Unable to update your profile." }
                )
            }

            result
        }
    }

    suspend fun deleteResidentAccount(accessToken: String): JSONObject =
        withContext(Dispatchers.IO) {
            val fullUrl = "${BASE_URL.trimEnd('/')}/api/residents/account"

            val request = Request.Builder()
                .url(fullUrl)
                .header("Authorization", "Bearer ${accessToken.trim()}")
                .delete()
                .build()

            client.newCall(request).execute().use { response ->
                val responseText = response.body?.string().orEmpty()

                val result = if (responseText.isNotBlank()) {
                    JSONObject(responseText)
                } else {
                    JSONObject()
                }

                if (!response.isSuccessful) {
                    throw IOException(
                        result.optString("message")
                            .ifBlank { "Unable to delete your account. Please try again." }
                    )
                }

                result
            }
        }

    suspend fun forgotPassword(email: String): JSONObject = withContext(Dispatchers.IO) {
        val json = JSONObject().apply {
            put("email", email.trim())
        }

        postRequest("/api/auth/forgot-password", json, "Forgot Password")
    }

    suspend fun verifyRecoveryOtp(
        email: String,
        token: String
    ): JSONObject = withContext(Dispatchers.IO) {
        val json = JSONObject().apply {
            put("email", email.trim())
            put("token", token.trim())
        }

        postRequest("/api/auth/verify-recovery-otp", json, "Verify Recovery OTP")
    }

    suspend fun resendRecoveryOtp(email: String): JSONObject = withContext(Dispatchers.IO) {
        val json = JSONObject().apply {
            put("email", email.trim())
        }

        postRequest("/api/auth/forgot-password", json, "Resend Recovery OTP")
    }


    suspend fun resetPassword(
        accessToken: String,
        newPassword: String
    ): JSONObject = withContext(Dispatchers.IO) {
        val fullUrl = "${BASE_URL.trimEnd('/')}/api/auth/reset-password"

        val json = JSONObject().apply {
            put("new_password", newPassword)
        }

        val body = json.toString().toRequestBody(
            "application/json; charset=utf-8".toMediaType()
        )

        val request = Request.Builder()
            .url(fullUrl)
            .header("Authorization", "Bearer ${accessToken.trim()}")
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            val responseText = response.body?.string().orEmpty()

            val result = if (responseText.isNotBlank()) {
                JSONObject(responseText)
            } else {
                JSONObject()
            }

            if (!response.isSuccessful) {
                throw IOException(
                    result.optString("message")
                        .ifBlank { "Unable to reset your password. Please try again." }
                )
            }

            result
        }
    }

    private suspend fun postRequest(
        path: String,
        json: JSONObject,
        operation: String
    ): JSONObject = withContext(Dispatchers.IO) {
        val body = json.toString().toRequestBody(
            "application/json; charset=utf-8".toMediaType()
        )

        val fullUrl = "${BASE_URL.trimEnd('/')}/${path.trimStart('/')}"

        val request = Request.Builder()
            .url(fullUrl)
            .post(body)
            .build()

        Log.d(TAG, "Sending $operation request to ${request.url}")

        try {
            client.newCall(request).execute().use { response ->
                val responseText = response.body?.string().orEmpty()

                Log.d(TAG, "$operation response status: ${response.code}")

                val result = if (responseText.isNotBlank()) {
                    JSONObject(responseText)
                } else {
                    JSONObject()
                }

                if (!response.isSuccessful) {
                    val message = result.optString("message")
                        .ifBlank { result.optString("error") }
                        .ifBlank { "$operation failed. Please try again." }

                    throw IOException(message)
                }

                result
            }
        } catch (exception: Exception) {
            Log.e(TAG, "$operation request failed: ${exception.message}", exception)

            when (exception) {
                is ConnectException, is SocketTimeoutException, is UnknownHostException -> {
                    throw IOException(
                        "Unable to connect to $fullUrl. If testing on Android Emulator, use 10.0.2.2 instead of 192.168.x.x. Check if host server is running.",
                        exception
                    )
                }
                else -> throw exception
            }
        }
    }
}
