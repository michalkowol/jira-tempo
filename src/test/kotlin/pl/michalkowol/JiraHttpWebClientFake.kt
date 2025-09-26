package pl.michalkowol

import org.springframework.http.ResponseEntity
import pl.michalkowol.jira.JiraWebClient

class JiraHttpWebClientFake : JiraWebClient {

    override fun sendWorklog(taskKey: String, cookie: String, body: String): ResponseEntity<String> {
        return ResponseEntity.ok("""{"ok": true}""")
    }
}
