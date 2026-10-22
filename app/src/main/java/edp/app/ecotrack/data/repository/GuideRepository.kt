package edp.app.ecotrack.data.repository

import edp.app.ecotrack.data.model.GuideItem
import edp.app.ecotrack.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GuideRepository {

    suspend fun getGuides(): Result<List<GuideItem>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = ApiClient.getResidentGuide()
                val guidesArray = response.optJSONArray("guides")
                val guides = mutableListOf<GuideItem>()

                if (guidesArray != null) {
                    for (i in 0 until guidesArray.length()) {
                        val item = guidesArray.getJSONObject(i)

                        guides.add(
                            GuideItem(
                                guideId = item.optInt("guide_id"),
                                wasteType = item.optString("waste_type"),
                                description = item.optString("description"),
                                disposalMethod = item.optString("disposal_method")
                            )
                        )
                    }
                }

                Result.success(guides)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
