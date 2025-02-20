package pl.michalkowol.jira

import com.softwareberg.HttpClient
import com.softwareberg.HttpHeader
import com.softwareberg.HttpMethod.POST
import com.softwareberg.HttpRequest
import org.slf4j.LoggerFactory
import java.net.HttpURLConnection.HTTP_CREATED
import java.net.HttpURLConnection.HTTP_OK
import java.time.Duration
import java.time.format.DateTimeFormatter

class TempoClient(private val httpClient: HttpClient) {

    private val log = LoggerFactory.getLogger(TempoClient::class.java)

    fun create(task: Task, cookie: String): Int {
        val body = createNewTaskBody(task)
        val headers = listOf(
            HttpHeader("Content-Type", "application/json"),
            HttpHeader("Cookie", cookie)
        )
        val request = HttpRequest(
            POST,
            "https://paramount.atlassian.net/rest/api/4/issue/${task.key}/worklog",
            headers,
            body
        )
        val response = httpClient.execute(request).join()
        log.debug("TempoClient [request={}, response={}]", request, response)
        return when (response.statusCode) {
            HTTP_OK, HTTP_CREATED -> response.statusCode
            else -> throw TempoException("Error to create task [task=$task]")
        }
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
