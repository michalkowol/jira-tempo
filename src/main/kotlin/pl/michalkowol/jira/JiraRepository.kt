package pl.michalkowol.jira

import java.time.Duration
import java.time.format.DateTimeFormatter
import org.springframework.stereotype.Component

@Component
class JiraRepository(private val client: JiraWebClient) {

    fun create(task: Task, cookie: String): Int {
        val body = createNewTaskBody(task)
        val response = client.sendWorklog(
            taskKey = task.key,
            cookie = cookie,
            body = body
        )
        return response.statusCode.value()
    }

    private fun createNewTaskBody(task: Task): String {
        return """
        {
            "timeSpentSeconds": ${formatTaskSeconds(task)},
            "started": "${formatStarted(task)}",
            "comment": {
                "type": "doc",
                "version": 1,
                "content": [
                    {
                        "type": "paragraph",
                        "content": [
                            {
                                "text": "${task.comment}",
                                "type": "text"
                            }
                        ]
                    }
                ]
            }
        }
        """.trimIndent()
    }

    private fun formatTaskSeconds(task: Task): Long = Duration.between(task.start, task.end).toSeconds()
    private fun formatStarted(task: Task): String = task.start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))
}
