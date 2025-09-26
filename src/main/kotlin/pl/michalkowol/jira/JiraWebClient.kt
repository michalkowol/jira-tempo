package pl.michalkowol.jira

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.service.annotation.PostExchange

interface JiraWebClient {

    @PostExchange("/rest/api/3/issue/{taskKey}/worklog", contentType = "application/json")
    fun sendWorklog(
        @PathVariable taskKey: String,
        @RequestHeader("Cookie") cookie: String,
        @RequestBody body: String
    ): ResponseEntity<String>
}
