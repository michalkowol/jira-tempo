package pl.michalkowol

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.michalkowol.jira.Task
import pl.michalkowol.jira.Tempo
import pl.michalkowol.jira.TempoClient

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
        val tasksIds = tempo.logTasks(username, password, tasks)
        // then
        assertEquals(3, tasksIds.size)
        assertEquals(200, tasksIds[0])
    }
}
