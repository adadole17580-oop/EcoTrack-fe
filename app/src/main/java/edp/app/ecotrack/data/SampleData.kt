package edp.app.ecotrack.data

import edp.app.ecotrack.model.Task
import edp.app.ecotrack.model.TaskStatus

object SampleData {

    val tasks = listOf(
        Task(
            id = 1,
            title = "Household Waste Collection",
            description = "Collect household waste from the assigned collection point.",
            location = "Barangay Bulua",
            distanceKm = 0.8,
            wasteType = "Biodegradable",
            reportedTime = "Today 8:00 AM",
            isNew = true,
            status = TaskStatus.ASSIGNED
        ),
        Task(
            id = 2,
            title = "Recyclable Waste Pickup",
            description = "Collect recyclable materials from the assigned area.",
            location = "Barangay Bulua",
            distanceKm = 1.5,
            wasteType = "Recyclable",
            reportedTime = "Today 9:15 AM",
            isNew = true,
            status = TaskStatus.ASSIGNED
        ),
        Task(
            id = 3,
            title = "Mixed Waste Collection",
            description = "Collect mixed waste from the assigned collection point.",
            location = "Barangay Bulua",
            distanceKm = 2.3,
            wasteType = "Non-Biodegradable",
            reportedTime = "Today 10:30 AM",
            isNew = false,
            status = TaskStatus.ASSIGNED
        ),
        Task(
            id = 4,
            title = "Food Waste Collection",
            description = "Collect separated food waste from the assigned area.",
            location = "Barangay Bulua",
            distanceKm = 3.1,
            wasteType = "Food Waste",
            reportedTime = "Today 11:00 AM",
            isNew = false,
            status = TaskStatus.ASSIGNED
        )
    )
}