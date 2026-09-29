package edp.app.ecotrack.model

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val location: String,
    val distanceKm: Double,
    val wasteType: String,
    val reportedTime: String,
    val isNew: Boolean = false,
    val status: TaskStatus = TaskStatus.ASSIGNED
)

enum class TaskStatus {
    ASSIGNED,
    WAITING_FOR_VERIFICATION,
    COMPLETED
}