package edp.app.ecotrack.data

import edp.app.ecotrack.model.Task

interface TaskRepository {

    fun getTasks(): List<Task>

    fun updateTask(task: Task)
}