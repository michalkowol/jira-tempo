package pl.michalkowol

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.softwareberg.HttpClient
import com.softwareberg.HttpResponse
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.michalkowol.jira.Task
import pl.michalkowol.jira.TempoClient
import java.time.ZonedDateTime
import java.util.concurrent.CompletableFuture

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
        val tempoClient = TempoClient(createHttpClientMock())

        // when
        val createId = tempoClient.create(task, cookie)

        // then
        assertEquals(200, createId)
    }

    private fun createHttpClientMock(): HttpClient = mock {
        on { execute(any()) } doReturn CompletableFuture.completedFuture(HttpResponse(200))
    }
}
