package pl.michalkowol

class Tempo(private val tempoClient: TempoClient) {

    fun logTasks(username: String, password: String, tasks: List<Task>): List<Int> {
        val ids = logAllTasksWithKeys(username, password, tasks)
        return ids
    }

    internal fun logAllTasksWithKeys(username: String, password: String, tasks: List<Task>): List<Int> {
        val taskIds = tasks.map { task -> tempoClient.create(username, password, task) }
        return taskIds
    }

    fun delete(username: String, password: String, workflowId: Int): Int {
        return tempoClient.delete(username, password, workflowId)
    }
}