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
import java.time.Duration
import java.util.concurrent.CompletableFuture

class TempoClientSpec {

    @Test
    fun `it should delete task by id`() {
        // given
        val username = "user"
        val password = "pass"
        val workflowId = 1_018_754
        val tempoClient = TempoClient(createHttpClientMock())

        // when
        val deletedId = tempoClient.delete(username, password, workflowId)

        // then
        assertEquals(workflowId, deletedId)
    }

    @Test
    fun `it should create new task`() {
        // given
        val username = "user"
        val password = "password"
        val task = Task(key = "WTAI-774", comment = "Review", date = "2017-01-13", duration = Duration.ofMinutes(5))
        val tempoClient = TempoClient(createHttpClientMock())

        // when
        val createId = tempoClient.create(username, password, task)

        // then
        assertEquals(200, createId)
    }

    private fun createHttpClientMock(): HttpClient = mock {
        on { execute(any()) } doReturn CompletableFuture.completedFuture(HttpResponse(200))
    }
}
