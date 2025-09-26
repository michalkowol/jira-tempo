package pl.michalkowol

import java.time.ZonedDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import pl.michalkowol.jira.JiraRepository
import pl.michalkowol.jira.Task

class TempoClientSpec {

    @Test
    fun `it should create new task`() {
        // given
        val cookie = "cookie"
        val task = Task(
            key = "WTAI-774",
            comment = "Review",
            start = ZonedDateTime.parse("2017-01-13T13:00:00Z"),
            end = ZonedDateTime.parse("2017-01-13T14:00:00Z")
        )
        val tempoClient = JiraRepository(JiraHttpWebClientFake())

        // when
        val createId = tempoClient.create(task, cookie)

        // then
        assertEquals(200, createId)
    }
}
