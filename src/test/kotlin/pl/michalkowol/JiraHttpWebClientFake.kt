package pl.michalkowol

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import pl.michalkowol.jira.JiraWebClient

class JiraHttpWebClientFake : JiraWebClient {

    private var failure: HttpClientErrorException? = null

    override fun sendWorklog(taskKey: String, cookie: String, body: String): ResponseEntity<String> {
        failure?.let { throw it }
        return ResponseEntity.ok("""{"ok": true}""")
    }

    fun configureFailure(status: HttpStatus, responseBody: String) {
        failure = HttpClientErrorException(status, status.reasonPhrase, responseBody.toByteArray(), null)
    }
}
