package pl.michalkowol

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.softwareberg.HttpClient
import com.softwareberg.HttpResponse
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.michalkowol.jira.Task
import pl.michalkowol.jira.TempoClient
import java.util.concurrent.CompletableFuture

class TempoClientSpec {

    @Test
    fun `it should delete task by id`() {
        // given
        val username = "user"
        val password = "pass"
        val workflowId = 1018754
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
        val task = Task(key = "WTAI-774", comment = "Review", date = "2017-01-13", timeInSec = 300)
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
