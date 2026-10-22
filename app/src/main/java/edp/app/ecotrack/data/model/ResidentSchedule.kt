package edp.app.ecotrack.data.model

data class ResidentSchedule(
    val scheduleId: Int,
    val barangayId: Int,
    val collectionDay: String,
    val collectionTime: String,
    val wasteType: String,
    val customAddress: String? = null
)
