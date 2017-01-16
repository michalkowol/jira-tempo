package pl.michalkowol

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class TempoClientSpec {

    private fun <T> any(): T {
        return Mockito.any<T>()
    }

    @Test
    @DisplayName("it should delete task by id")
    fun delete() {
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
    @DisplayName("it should create new task")
    fun create() {
        // given
        val httpClient = createHttpClientMock()
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
        on { execute(any()) } doReturn HttpResponse(200)
    }
}