package edp.app.ecotrack.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ApiClient {
    // For Android Emulator: use "http://10.0.2.2:3000/" to reach localhost:3000 on host machine.
    // For physical Android device: use host machine's Wi-Fi IP (e.g., "http://192.168.0.248:3000/").
    var BASE_URL = "http://127.0.0.1:3000/"
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
        barangay: String
    ): JSONObject = withContext(Dispatchers.IO) {
        val json = JSONObject().apply {
            put("full_name", fullName.trim())
            put("email", email.trim())
            put("password", password)
            put("phone", phone.trim())
            put("barangay", barangay.trim())
        }

        postRequest("/api/auth/register", json, "Registration")
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
