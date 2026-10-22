package edp.app.ecotrack.data.repository

import edp.app.ecotrack.data.model.ResidentDashboardData
import edp.app.ecotrack.data.model.ResidentDashboardResponse
import edp.app.ecotrack.data.model.ResidentProfile
import edp.app.ecotrack.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class ResidentDashboardRepository {

    suspend fun getDashboard(accessToken: String): ResidentDashboardResponse =
        withContext(Dispatchers.IO) {
            val json = ApiClient.getResidentDashboard(accessToken)
            val userJson = json.optJSONObject("user") ?: JSONObject()
            val dashboardJson = json.optJSONObject("dashboard") ?: JSONObject()

            ResidentDashboardResponse(
                message = json.optString("message"),
                user = ResidentProfile(
                    id = userJson.optString("id"),
                    full_name = userJson.optString("full_name"),
                    email = userJson.optString("email"),
                    role = userJson.optString("role"),
                    phone = userJson.optString("phone"),
                    barangay = userJson.optString("barangay"),
                    address = userJson.optString("address")
                ),
                dashboard = ResidentDashboardData(
                    upcoming_collections = dashboardJson.optInt("upcoming_collections"),
                    total_reports = dashboardJson.optInt("total_reports"),
                    pending_reports = dashboardJson.optInt("pending_reports")
                )
            )
        }
}
