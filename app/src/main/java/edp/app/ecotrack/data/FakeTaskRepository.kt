package edp.app.ecotrack.data

import edp.app.ecotrack.model.Task

class FakeTaskRepository : TaskRepository {

    private val tasks = SampleData.tasks.toMutableList()

    override fun getTasks(): List<Task> {
        return tasks.toList()
    }

    override fun updateTask(task: Task) {
        val index = tasks.indexOfFirst { it.id == task.id }

        if (index != -1) {
            tasks[index] = task
        }
    }
}