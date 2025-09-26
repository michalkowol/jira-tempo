package pl.michalkowol


import pl.michalkowol.jira.Task
import pl.michalkowol.jira.Tempo

import java.time.ZonedDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import pl.michalkowol.jira.JiraRepository

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
        val tempoClient = JiraRepository(JiraHttpWebClientFake())
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
        val tempoClient = JiraRepository(JiraHttpWebClientFake())
        val tempo = Tempo(tempoClient)

        // when
        val tasksIds = tempo.logTasks(tasks, cookie)

        // then
        assertEquals(3, tasksIds.size)
        assertEquals(200, tasksIds[0])
    }
}
