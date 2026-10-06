package edp.app.ecotrack.data

import edp.app.ecotrack.model.Task
import edp.app.ecotrack.model.TaskStatus

object SampleData {

    val tasks = listOf(
        Task(
            id = 1,
            title = "Household Waste Collection",
            description = "Collect household waste from the assigned collection point.",
            location = "Barangay Bulua – Zone 1",
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
            location = "Barangay Bulua – Zone 2",
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
            location = "Barangay Bulua – Zone 3",
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
            location = "Barangay Bulua – Zone 4",
            distanceKm = 3.1,
            wasteType = "Food Waste",
            reportedTime = "Today 11:00 AM",
            isNew = false,
            status = TaskStatus.ASSIGNED
        ),
        Task(
            id = 5,
            title = "Household Waste Collection",
            description = "Collect household waste from the assigned collection point.",
            location = "Barangay Bulua – Zone 5",
            distanceKm = 3.7,
            wasteType = "Biodegradable",
            reportedTime = "Today 12:00 PM",
            isNew = false,
            status = TaskStatus.ASSIGNED
        ),
        Task(
            id = 6,
            title = "Recyclable Waste Pickup",
            description = "Collect recyclable materials from the assigned area.",
            location = "Barangay Bulua – Zone 6",
            distanceKm = 4.2,
            wasteType = "Recyclable",
            reportedTime = "Today 1:00 PM",
            isNew = false,
            status = TaskStatus.ASSIGNED
        ),
        Task(
            id = 7,
            title = "Mixed Waste Collection",
            description = "Collect mixed waste from the assigned collection point.",
            location = "Barangay Bulua – Zone 7",
            distanceKm = 4.8,
            wasteType = "Non-Biodegradable",
            reportedTime = "Today 1:30 PM",
            isNew = false,
            status = TaskStatus.ASSIGNED
        ),
        Task(
            id = 8,
            title = "Food Waste Collection",
            description = "Collect separated food waste from the assigned area.",
            location = "Barangay Bulua – Zone 8",
            distanceKm = 5.4,
            wasteType = "Food Waste",
            reportedTime = "Today 2:00 PM",
            isNew = false,
            status = TaskStatus.ASSIGNED
        ),
        Task(
            id = 9,
            title = "Household Waste Collection",
            description = "Collect household waste from the assigned collection point.",
            location = "Barangay Bulua – Zone 9",
            distanceKm = 5.9,
            wasteType = "Biodegradable",
            reportedTime = "Today 2:30 PM",
            isNew = false,
            status = TaskStatus.ASSIGNED
        ),
        Task(
            id = 10,
            title = "Recyclable Waste Pickup",
            description = "Collect recyclable materials from the assigned area.",
            location = "Barangay Bulua – Zone 10",
            distanceKm = 6.5,
            wasteType = "Recyclable",
            reportedTime = "Today 3:00 PM",
            isNew = false,
            status = TaskStatus.ASSIGNED
        )
    )
}