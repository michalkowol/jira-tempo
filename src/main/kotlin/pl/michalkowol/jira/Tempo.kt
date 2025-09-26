package pl.michalkowol.jira

import org.springframework.stereotype.Component

@Component
class Tempo(private val jiraRepository: JiraRepository) {

    fun logTasks(tasks: List<Task>, cookie: String): List<Int> {
        return tasks
            .filterNot { task -> task.key == "NT" }
            .map { task -> jiraRepository.create(task, cookie) }
    }
}
