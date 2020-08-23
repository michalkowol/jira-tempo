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
        val deleteWorkflowRequest = HttpRequest(
            DELETE,
            "https://jira.mtvi.com/rest/tempo-timesheets/3/worklogs/$workflowId",
            listOf(basicAuthHeader)
        )
        log.debug("Request: {}", deleteWorkflowRequest)
        val response = httpClient.execute(deleteWorkflowRequest).join()

        when (response.statusCode) {
            HTTP_OK -> return workflowId
            else -> throw TempoException("Cannot delete task with worklog [id=$workflowId]")
        }
    }

    fun create(username: String, password: String, task: Task): Int {
        val body = createNewTaskBody(task, username)
        val headers = createNewTaskHeaders(username, password)
        val createWorkflowRequest = HttpRequest(
            POST,
            "https://jira.mtvi.com/rest/tempo-timesheets/3/worklogs",
            headers,
            body
        )
        log.debug("Request: {}", createWorkflowRequest)
        val response = httpClient.execute(createWorkflowRequest).join()

        when (response.statusCode) {
            HTTP_OK -> return response.statusCode
            else -> {
                log.error("Response: {}", response.toString())
                throw TempoException("Error to create task [task=$task]")
            }
        }
    }

    private fun createNewTaskHeaders(username: String, password: String): List<HttpHeader> {
        val basicAuthHeader = HttpHeader.basicAuth(username, password)
        val contentType = HttpHeader("Content-Type", "application/json")
        return listOf(basicAuthHeader, contentType)
    }

    private fun createNewTaskBody(task: Task, username: String): String {
        return """
        {
            "timeSpentSeconds": ${task.timeInSec},
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
