package pl.michalkowol

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.michalkowol.jira.Task
import pl.michalkowol.jira.Tempo
import pl.michalkowol.jira.TempoClient
import java.time.Duration

class TempoSpec {

    private val taskA = Task("A", "desc A", "2017-01-16", Duration.ofMinutes(5))
    private val taskB = Task("B", "desc B", "2017-01-18", Duration.ofMinutes(5))
    private val taskC = Task("C", "desc C", "2017-01-19", Duration.ofMinutes(5))
    private val taskNT = Task("NT", "desc C", "2017-01-19", Duration.ofMinutes(5))

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

    @Test
    fun `it should not log NT tasks`() {
        // given
        val username = "user"
        val password = "pass"
        val tasks = listOf(taskA, taskNT, taskB, taskC)
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
