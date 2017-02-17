package pl.michalkowol

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.assertEquals
import org.junit.Test

class TempoSpec {

    private val taskA = Task("A", "desc A", "2017-01-16", 300)
    private val taskB = Task("B", "desc B", "2017-01-18", 300)
    private val taskC = Task("C", "desc C", "2017-01-19", 300)

    @Test
    fun `it should log all worklogs with key`() {
        // given
        val username = "user"
        val password = "pass"
        val tasks = listOf(taskA, taskB, taskC)
        val tempoClient = mock<TempoClient> {
            on { create(any(), any(), any()) } doReturn 200
        }
        val tempo = Tempo(tempoClient)
        // when
        val tasksIds = tempo.logAllTasksWithKeys(username, password, tasks)
        // then
        assertEquals(3, tasksIds.size)
        assertEquals(200, tasksIds[0])
    }
}