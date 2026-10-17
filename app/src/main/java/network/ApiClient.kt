
package edp.app.ecotrack.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

object ApiClient {

    // Android Emulator address for your local backend
    private const val BASE_URL = "http://10.0.2.2:3000"

    private val client = OkHttpClient()

    // Test the connection to the backend
    suspend fun testConnection(): String =
        withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url("$BASE_URL/api/health")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                val body = response.body?.string().orEmpty()

                if (!response.isSuccessful) {
                    throw IOException(
                        "Backend returned HTTP ${response.code}: $body"
                    )
                }

                body
            }
        }

    // Log in through the backend
    suspend fun login(
        email: String,
        password: String
    ): JSONObject = withContext(Dispatchers.IO) {

        val requestBody = JSONObject()
            .put("email", email.trim())
            .put("password", password)
            .toString()
            .toRequestBody(
                "application/json; charset=utf-8".toMediaType()
            )

        val request = Request.Builder()
            .url("$BASE_URL/api/login")
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            val body = response.body?.string().orEmpty()

            val result = try {
                JSONObject(body)
            } catch (_: Exception) {
                throw IOException(
                    "The server returned an invalid response."
                )
            }

            if (!response.isSuccessful) {
                val message = result.optString(
                    "message",
                    "Login failed. Please try again."
                )

                throw IOException(message)
            }

            if (result.optString("status") != "OK") {
                throw IOException(
                    result.optString(
                        "message",
                        "Login failed. Please try again."
                    )
                )
            }

            result
        }
    }
}
