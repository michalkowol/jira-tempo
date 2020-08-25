package pl.michalkowol.jira

import com.softwareberg.HttpClient
import com.softwareberg.HttpHeader
import com.softwareberg.HttpMethod.DELETE
import com.softwareberg.HttpMethod.POST
import com.softwareberg.HttpRequest
import org.slf4j.LoggerFactory
import java.net.HttpURLConnection.HTTP_OK

@Suppress("UseIfInsteadOfWhen")
class TempoClient(private val httpClient: HttpClient) {

    private val log = LoggerFactory.getLogger(TempoClient::class.java)

    fun delete(username: String, password: String, workflowId: Int): Int {
        val basicAuthHeader = HttpHeader.basicAuth(username, password)
        val headers = listOf(basicAuthHeader)
        val request = HttpRequest(
            DELETE,
            "https://jira.mtvi.com/rest/tempo-timesheets/3/worklogs/$workflowId",
            headers
        )
        val response = httpClient.execute(request).join()
        log.debug("TempoClient [request={}, response={}]", request.copy(headers = listOf()), response.toString())

        when (response.statusCode) {
            HTTP_OK -> return workflowId
            else -> throw TempoException("Cannot delete task with worklog [id=$workflowId]")
        }
    }

    fun create(username: String, password: String, task: Task): Int {
        val basicAuthHeader = HttpHeader.basicAuth(username, password)
        val body = createNewTaskBody(task, username)
        val headers = listOf(basicAuthHeader, HttpHeader("Content-Type", "application/json"))
        val request = HttpRequest(
            POST,
            "https://jira.mtvi.com/rest/tempo-timesheets/3/worklogs",
            headers,
            body
        )
        val response = httpClient.execute(request).join()
        log.debug("TempoClient [request={}, response={}]", request.copy(headers = listOf()), response.toString())

        when (response.statusCode) {
            HTTP_OK -> return response.statusCode
            else -> throw TempoException("Error to create task [task=$task]")
        }
    }

    private fun createNewTaskBody(task: Task, username: String): String {
        return """
        {
            "timeSpentSeconds": ${task.duration.toSeconds()},
            "dateStarted": "${task.date}T00:00:00.000",
            "comment": "${task.comment}",
            "remainingEstimateSeconds": 0,
            "author": {
                "name": "$username"
            },
            "issue": {
                "key": "${task.key}"
            }
        }
        """.trimIndent()
    }
}
