package pl.michalkowol.jira

class Tempo(private val tempoClient: TempoClient) {

    fun logTasks(tasks: List<Task>, cookie: String): List<Int> {
        return tasks
            .filterNot { task -> task.key == "NT" }
            .map { task -> tempoClient.create(task, cookie) }
    }
}
