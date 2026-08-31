package pl.michalkowol

import assertk.all
import assertk.assertFailure
import assertk.assertions.contains
import assertk.assertions.hasMessage
import assertk.assertions.isInstanceOf
import assertk.assertions.prop
import java.time.ZonedDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pl.michalkowol.jira.JiraRepository
import pl.michalkowol.jira.JiraWorklogException
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

    @Test
    fun `it should report task key and jira response when worklog is rejected`() {
        // given
        val cookie = "cookie"
        val task = Task(
            key = "WTAI-774",
            comment = "Review",
            start = ZonedDateTime.parse("2017-01-13T13:00:00Z"),
            end = ZonedDateTime.parse("2017-01-13T14:00:00Z")
        )
        val client = JiraHttpWebClientFake()
        client.configureFailure(HttpStatus.NOT_FOUND, """{"errorMessages":["Issue does not exist or you do not have permission to see it."],"errors":{}}""")
        val tempoClient = JiraRepository(client)

        // when / then
        assertFailure { tempoClient.create(task, cookie) }
            .isInstanceOf<JiraWorklogException>()
            .all {
                hasMessage("""Failed to create Jira worklog [taskKey=WTAI-774, status=404, response={"errorMessages":["Issue does not exist or you do not have permission to see it."],"errors":{}}]""")
                prop(JiraWorklogException::requestBody).contains(""""timeSpentSeconds": 3600""")
            }
    }
}
