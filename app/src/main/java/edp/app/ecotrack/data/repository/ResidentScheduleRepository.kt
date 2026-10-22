package edp.app.ecotrack.data.repository

import edp.app.ecotrack.data.model.ResidentSchedule
import edp.app.ecotrack.network.ApiClient

object ResidentScheduleRepository {

    suspend fun getSchedules(accessToken: String): List<ResidentSchedule> {
        val response = ApiClient.getResidentSchedules(accessToken)
        val schedulesArray = response.optJSONArray("schedules")
            ?: return emptyList()

        return List(schedulesArray.length()) { index ->
            val item = schedulesArray.getJSONObject(index)

            ResidentSchedule(
                scheduleId = item.optInt("schedule_id"),
                barangayId = item.optInt("barangay_id"),
                collectionDay = item.optString("collection_day"),
                collectionTime = item.optString("collection_time"),
                wasteType = item.optString("waste_type"),
                customAddress = item.optString("custom_address")
                    .takeIf { it.isNotBlank() && it != "null" }
            )
        }
    }
}