
package edp.app.ecotrack.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

object ApiClient {

    private const val BASE_URL = "http://10.0.2.2:3000"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType =
        "application/json; charset=utf-8".toMediaType()

    // COMMON HTTP REQUEST
    private suspend fun request(
        method: String,
        endpoint: String,
        body: JSONObject? = null,
        accessToken: String? = null
    ): JSONObject = withContext(Dispatchers.IO) {

        val requestBody = body?.toString()
            ?.toRequestBody(jsonMediaType)

        val builder = Request.Builder()
            .url("$BASE_URL$endpoint")
            .addHeader("Accept", "application/json")

        if (!accessToken.isNullOrBlank()) {
            builder.addHeader(
                "Authorization",
                "Bearer $accessToken"
            )
        }

        when (method.uppercase()) {
            "GET" -> builder.get()

            "POST" -> builder.post(
                requestBody ?: "{}".toRequestBody(jsonMediaType)
            )

            "PUT" -> builder.put(
                requestBody ?: "{}".toRequestBody(jsonMediaType)
            )

            "DELETE" -> {
                if (requestBody != null) {
                    builder.delete(requestBody)
                } else {
                    builder.delete()
                }
            }

            else -> throw IOException(
                "Unsupported HTTP method: $method"
            )
        }

        client.newCall(builder.build()).execute().use { response ->

            val responseText = response.body?.string().orEmpty()

            val jsonResponse = try {
                if (responseText.isBlank()) {
                    JSONObject()
                } else {
                    JSONObject(responseText)
                }
            } catch (e: Exception) {
                JSONObject().put("message", responseText)
            }

            if (!response.isSuccessful) {
                val message = jsonResponse.optString(
                    "message",
                    "Request failed: HTTP ${response.code}"
                )

                val details = jsonResponse.optString("details", "")
                val code = jsonResponse.optString("code", "")

                val fullMessage = buildString {
                    append(message)

                    if (details.isNotBlank()) {
                        append("\nDetails: ")
                        append(details)
                    }

                    if (code.isNotBlank()) {
                        append("\nCode: ")
                        append(code)
                    }

                    append("\nHTTP: ")
                    append(response.code)
                }

                throw IOException(fullMessage)
            }

            jsonResponse
        }
    }

    // HEALTH CHECK
    suspend fun checkHealth(): JSONObject =
        request("GET", "/api/health")

    suspend fun testConnection(): JSONObject =
        checkHealth()

    // LOGIN
    suspend fun login(
        email: String,
        password: String
    ): JSONObject {
        val body = JSONObject().apply {
            put("email", email)
            put("password", password)
        }

        return request(
            method = "POST",
            endpoint = "/api/login",
            body = body
        )
    }

    // SUPABASE CONNECTION TEST
    suspend fun testSupabase(): JSONObject =
        request("GET", "/api/test-supabase")

    // DASHBOARD
    suspend fun getDashboardStats(
        accessToken: String
    ): JSONObject =
        request(
            method = "GET",
            endpoint = "/api/dashboard/stats",
            accessToken = accessToken
        )

    // WASTE REPORTS
    suspend fun getWasteReports(
        accessToken: String
    ): JSONObject =
        request(
            method = "GET",
            endpoint = "/api/reports",
            accessToken = accessToken
        )

    // NOTIFICATIONS
    suspend fun getNotifications(
        accessToken: String
    ): JSONObject =
        request(
            method = "GET",
            endpoint = "/api/notifications",
            accessToken = accessToken
        )

    // ASSIGN COLLECTOR
    suspend fun assignCollector(
        accessToken: String,
        reportId: String,
        collectorId: String
    ): JSONObject {
        val numericReportId = reportId
            .removePrefix("#")
            .toIntOrNull()
            ?: throw IOException("Invalid report ID.")

        val numericCollectorId = collectorId
            .removePrefix("#")
            .toIntOrNull()
            ?: throw IOException("Invalid collector ID.")

        val body = JSONObject().apply {
            put("reportId", numericReportId)
            put("collectorId", numericCollectorId)
        }

        return request(
            method = "POST",
            endpoint = "/api/reports/assign",
            body = body,
            accessToken = accessToken
        )
    }

    // APPROVE / RESOLVE WASTE REPORT
    suspend fun resolveReport(
        accessToken: String,
        reportId: String
    ): JSONObject {
        val numericReportId = reportId
            .removePrefix("#")
            .toIntOrNull()
            ?: throw IOException("Invalid report ID.")

        val body = JSONObject().apply {
            put("reportId", numericReportId)
        }

        return request(
            method = "POST",
            endpoint = "/api/reports/resolve",
            body = body,
            accessToken = accessToken
        )
    }

    // REJECT WASTE REPORT
    suspend fun rejectWasteReport(
        accessToken: String,
        reportId: String
    ): JSONObject {
        val numericReportId = reportId
            .removePrefix("#")
            .toIntOrNull()
            ?: throw IOException("Invalid report ID.")

        val body = JSONObject().apply {
            put("reportId", numericReportId)
        }

        return request(
            method = "POST",
            endpoint = "/api/reports/reject",
            body = body,
            accessToken = accessToken
        )
    }

    // SEND WASTE REPORT FOR ADMIN VERIFICATION
    suspend fun sendReportForVerification(
        accessToken: String,
        reportId: String
    ): JSONObject {
        val numericReportId = reportId
            .removePrefix("#")
            .toIntOrNull()
            ?: throw IOException("Invalid report ID.")

        val body = JSONObject().apply {
            put("reportId", numericReportId)
        }

        return request(
            method = "POST",
            endpoint = "/api/reports/for-verification",
            body = body,
            accessToken = accessToken
        )
    }

    // RESIDENTS
    suspend fun getResidents(
        accessToken: String
    ): JSONObject =
        request(
            method = "GET",
            endpoint = "/api/residents",
            accessToken = accessToken
        )

    // COLLECTORS
    suspend fun getCollectors(
        accessToken: String
    ): JSONObject =
        request(
            method = "GET",
            endpoint = "/api/collectors",
            accessToken = accessToken
        )

    // UPLOAD COLLECTOR PROFILE IMAGE
    suspend fun uploadCollectorProfileImage(
        accessToken: String,
        collectorId: String,
        imageFile: File,
        imageMimeType: String
    ): JSONObject = withContext(Dispatchers.IO) {

        val imageBody = imageFile.asRequestBody(
            imageMimeType.toMediaType()
        )

        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "image",
                imageFile.name,
                imageBody
            )
            .build()

        val request = Request.Builder()
            .url("$BASE_URL/api/collectors/$collectorId/profile-image")
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer $accessToken")
            .post(multipartBody)
            .build()

        client.newCall(request).execute().use { response ->

            val responseText = response.body?.string().orEmpty()

            val jsonResponse = try {
                if (responseText.isBlank()) {
                    JSONObject()
                } else {
                    JSONObject(responseText)
                }
            } catch (e: Exception) {
                JSONObject().put("message", responseText)
            }

            if (!response.isSuccessful) {
                throw IOException(
                    jsonResponse.optString(
                        "message",
                        "Image upload failed: HTTP ${response.code}"
                    )
                )
            }

            jsonResponse
        }
    }

    // COLLECTOR TASK HISTORY
    suspend fun getCollectorTaskHistory(
        accessToken: String,
        collectorId: String
    ): JSONObject =
        request(
            method = "GET",
            endpoint = "/api/collectors/$collectorId/task-history",
            accessToken = accessToken
        )

    // ADD COLLECTOR
    suspend fun addCollector(
        accessToken: String,
        name: String,
        email: String,
        password: String,
        phone: String,
        barangayId: Int? = null
    ): JSONObject {
        val body = JSONObject().apply {
            put("name", name)
            put("email", email)
            put("password", password)
            put("phone", phone)

            if (barangayId != null) {
                put("barangay_id", barangayId)
            }
        }

        return request(
            method = "POST",
            endpoint = "/api/collectors",
            body = body,
            accessToken = accessToken
        )
    }

    // SCHEDULES - LOAD GROUPED SCHEDULES
    suspend fun getSchedules(
        accessToken: String
    ): JSONObject =
        request(
            method = "GET",
            endpoint = "/api/schedules",
            accessToken = accessToken
        )

    // SCHEDULES - ADD GROUP
    suspend fun addScheduleGroup(
        accessToken: String,
        barangayName: String,
        collectionDays: List<String>,
        collectionTime: String,
        collectorId: String = "",
        wasteType: String = "Biodegradable"
    ): JSONObject {
        val body = JSONObject().apply {
            put("barangay_name", barangayName)
            put("collection_days", JSONArray(collectionDays))
            put("collection_time", collectionTime)
            put("waste_type", wasteType)

            if (collectorId.isNotBlank()) {
                put("collector_id", collectorId)
            }
        }

        return request(
            method = "POST",
            endpoint = "/api/schedules",
            body = body,
            accessToken = accessToken
        )
    }

    // SCHEDULES - UPDATE GROUP
    suspend fun updateScheduleGroup(
        accessToken: String,
        scheduleGroupId: String,
        barangayName: String,
        collectionDays: List<String>,
        collectionTime: String,
        collectorId: String = "",
        wasteType: String = "Biodegradable"
    ): JSONObject {
        val body = JSONObject().apply {
            put("barangay_name", barangayName)
            put("collection_days", JSONArray(collectionDays))
            put("collection_time", collectionTime)
            put("waste_type", wasteType)

            if (collectorId.isNotBlank()) {
                put("collector_id", collectorId)
            }
        }

        return request(
            method = "PUT",
            endpoint = "/api/schedules/$scheduleGroupId",
            body = body,
            accessToken = accessToken
        )
    }

    // SCHEDULES - DELETE GROUP
    suspend fun deleteScheduleGroup(
        accessToken: String,
        scheduleGroupId: String
    ): JSONObject =
        request(
            method = "DELETE",
            endpoint = "/api/schedules/$scheduleGroupId",
            accessToken = accessToken
        )

    // EXISTING SINGLE-DAY SCHEDULE METHODS
    suspend fun addSchedule(
        accessToken: String,
        barangayId: Int,
        collectionDay: String,
        collectionTime: String,
        wasteType: String
    ): JSONObject {
        val body = JSONObject().apply {
            put("barangay_id", barangayId)
            put("collection_day", collectionDay)
            put("collection_time", collectionTime)
            put("waste_type", wasteType)
        }

        return request(
            method = "POST",
            endpoint = "/api/schedules",
            body = body,
            accessToken = accessToken
        )
    }

    suspend fun updateSchedule(
        accessToken: String,
        scheduleId: String,
        barangayId: Int,
        collectionDay: String,
        collectionTime: String,
        wasteType: String
    ): JSONObject {
        val body = JSONObject().apply {
            put("barangay_id", barangayId)
            put("collection_day", collectionDay)
            put("collection_time", collectionTime)
            put("waste_type", wasteType)
        }

        return request(
            method = "PUT",
            endpoint = "/api/schedules/$scheduleId",
            body = body,
            accessToken = accessToken
        )
    }

    suspend fun deleteSchedule(
        accessToken: String,
        scheduleId: String
    ): JSONObject =
        request(
            method = "DELETE",
            endpoint = "/api/schedules/$scheduleId",
            accessToken = accessToken
        )

    // WASTE GUIDE - LOAD CATEGORIES
    suspend fun getWasteGuide(
        accessToken: String
    ): JSONObject =
        request(
            method = "GET",
            endpoint = "/api/waste-guide",
            accessToken = accessToken
        )

    // WASTE GUIDE - ADD CATEGORY
    suspend fun addWasteGuideCategory(
        accessToken: String,
        wasteType: String,
        description: String,
        disposalMethod: String,
        examples: List<String>
    ): JSONObject {
        val body = JSONObject().apply {
            put("waste_type", wasteType)
            put("description", description)
            put("disposal_method", disposalMethod)
            put("examples", JSONArray(examples))
        }

        return request(
            method = "POST",
            endpoint = "/api/waste-guide",
            body = body,
            accessToken = accessToken
        )
    }

    suspend fun updateWasteGuideCategory(
        accessToken: String,
        guideId: Int,
        wasteType: String,
        description: String,
        disposalMethod: String,
        examples: List<String>
    ): JSONObject {
        val body = JSONObject().apply {
            put("waste_type", wasteType)
            put("description", description)
            put("disposal_method", disposalMethod)
            put("examples", JSONArray(examples))
        }

        return request(
            method = "PUT",
            endpoint = "/api/waste-guide/$guideId",
            body = body,
            accessToken = accessToken
        )
    }

    // WASTE GUIDE - DELETE CATEGORY
    suspend fun deleteWasteGuideCategory(
        accessToken: String,
        guideId: Int
    ): JSONObject =
        request(
            method = "DELETE",
            endpoint = "/api/waste-guide/$guideId",
            accessToken = accessToken
        )

    // ADMIN PROFILE
    // Loads the authenticated admin's profile, including contact_number.
    suspend fun getAdminProfile(
        accessToken: String
    ): JSONObject =
        request(
            method = "GET",
            endpoint = "/api/profile",
            accessToken = accessToken
        )


    // UPDATE ADMIN PROFILE
    // Save the admin's name and phone number to Supabase.
    suspend fun updateAdminProfile(
        accessToken: String,
        fullName: String,
        phone: String
    ): JSONObject {
        val body = JSONObject().apply {
            put("fullName", fullName.trim())
            put("phone", phone.trim())
        }

        return request(
            method = "PUT",
            endpoint = "/api/profile",
            body = body,
            accessToken = accessToken
        )
    }


    // CHANGE ADMIN PASSWORD
    suspend fun changePassword(
        accessToken: String,
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): JSONObject {
        val body = JSONObject().apply {
            put("currentPassword", currentPassword)
            put("newPassword", newPassword)
            put("confirmPassword", confirmPassword)
        }

        return request(
            method = "POST",
            endpoint = "/api/profile/change-password",
            body = body,
            accessToken = accessToken
        )
    }
}
