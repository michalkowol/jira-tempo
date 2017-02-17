package pl.michalkowol

import com.softwareberg.HttpClient
import com.softwareberg.HttpHeader
import com.softwareberg.HttpMethod.DELETE
import com.softwareberg.HttpMethod.POST
import com.softwareberg.HttpRequest
import org.slf4j.LoggerFactory
import java.net.HttpURLConnection

class TempoClient(private val httpClient: HttpClient) {

    private val log = LoggerFactory.getLogger(TempoClient::class.java)

    fun delete(username: String, password: String, workflowId: Int): Int {
        val basicAuthHeader = HttpHeader.basicAuth(username, password)
        val response = httpClient.execute(HttpRequest(DELETE, "https://jira.mtvi.com/rest/tempo-timesheets/3/worklogs/$workflowId", listOf(basicAuthHeader)))
        if (response.statusCode != HttpURLConnection.HTTP_OK) {
            throw Exception("Cannot delete task with worklog [id=$workflowId]")
        } else {
            return workflowId
        }
    }

    fun create(username: String, password: String, task: Task): Int {
        val body ="""
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
            }""".trimIndent()
        val basicAuthHeader = HttpHeader.basicAuth(username, password)
        val contentType = HttpHeader("Content-Type", "application/json")
        val headers = listOf(basicAuthHeader, contentType)
        val createRequest = HttpRequest(POST, "https://jira.mtvi.com/rest/tempo-timesheets/3/worklogs", headers, body)
        log.debug("Request: {}", createRequest)
        val response = httpClient.execute(createRequest)
        if (response.statusCode != HttpURLConnection.HTTP_OK) {
            log.error("Response: {}", response.toString())
            throw Exception("Error to create task [task=$task]")
        }
        return response.statusCode
    }
}