package pl.michalkowol.jira

class Tempo(private val tempoClient: TempoClient) {

    fun logTasks(username: String, password: String, tasks: List<Task>): List<Int> {
        val ids = tasks.map { task -> tempoClient.create(username, password, task) }
        return ids
    }

    fun delete(username: String, password: String, workflowId: Int): Int {
        return tempoClient.delete(username, password, workflowId)
    }

}
