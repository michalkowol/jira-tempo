package pl.michalkowol

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.michalkowol.jira.Task
import pl.michalkowol.jira.Tempo
import pl.michalkowol.jira.TempoClient
import java.time.ZonedDateTime

class TempoSpec {

    private val taskA = Task("A", "desc A", ZonedDateTime.parse("2017-01-16T09:05Z"), ZonedDateTime.parse("2017-01-16T09:10Z"))
    private val taskB = Task("B", "desc B", ZonedDateTime.parse("2017-01-17T09:05Z"), ZonedDateTime.parse("2017-01-16T09:10Z"))
    private val taskC = Task("C", "desc C", ZonedDateTime.parse("2017-01-18T09:05Z"), ZonedDateTime.parse("2017-01-16T09:10Z"))
    private val taskNT = Task("NT", "desc C", ZonedDateTime.parse("2017-01-19T09:05Z"), ZonedDateTime.parse("2017-01-16T09:10Z"))

    @Test
    fun `it should log all worklogs with key`() {
        // given
        val cookie = "cookie"
        val tasks = listOf(taskA, taskB, taskC)
        val tempoClient = mock<TempoClient> {
            on { create(any(), eq(cookie)) } doReturn 200
        }
        val tempo = Tempo(tempoClient)

        // when
        val tasksIds = tempo.logTasks(tasks, cookie)

        // then
        assertEquals(3, tasksIds.size)
        assertEquals(200, tasksIds[0])
    }

    @Test
    fun `it should not log NT tasks`() {
        // given
        val cookie = "cookie"
        val tasks = listOf(taskA, taskNT, taskB, taskC)
        val tempoClient = mock<TempoClient> {
            on { create(any(), eq(cookie)) } doReturn 200
        }
        val tempo = Tempo(tempoClient)

        // when
        val tasksIds = tempo.logTasks(tasks, cookie)

        // then
        assertEquals(3, tasksIds.size)
        assertEquals(200, tasksIds[0])
    }
}
