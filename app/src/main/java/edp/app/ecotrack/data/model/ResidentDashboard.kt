package edp.app.ecotrack.data.model

data class ResidentDashboardResponse(
    val message: String = "",
    val user: ResidentProfile = ResidentProfile(),
    val dashboard: ResidentDashboardData = ResidentDashboardData()
)

data class ResidentProfile(
    val id: String = "",
    val full_name: String = "",
    val email: String = "",
    val role: String = "",
    val phone: String = "",
    val barangay: String = ""
)

data class ResidentDashboardData(
    val upcoming_collections: Int = 0,
    val total_reports: Int = 0,
    val pending_reports: Int = 0
)
