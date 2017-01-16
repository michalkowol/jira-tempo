package pl.michalkowol

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TempoSpec {

    private val taskA = Task("A", "desc A", "2017-01-16", 300)
    private val taskB = Task(null, "desc B", "2017-01-18", 300)
    private val taskC = Task("C", "desc C", "2017-01-19", 300)
    private val taskD = Task("D", "desc D", "2017-01-19", 300)
    private val taskE = Task("E", "desc E", "2017-02-16", 300)
    private val taskF = Task("F", "desc F", "2017-03-16", 300)
    private val taskG = Task(null, "desc G", "2017-03-16", 300)
    private val taskH = Task(null, "desc H", "2017-03-17", 300)
    private val taskI = Task(null, "desc I", "2017-04-16", 300)

    @Test
    @DisplayName("it should log all worklogs with key")
    fun withKeys() {
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
        assertEquals(2, tasksIds.size)
        assertEquals(200, tasksIds[0])
    }

    @Test
    @DisplayName("it should log map it equally")
    fun mapTimeEqually() {
        // given
        val tasks = listOf(taskA, taskB, taskC)
        val tempoClient = mock<TempoClient>()
        val tempo = Tempo(tempoClient)
        // when
        val newTasks = tempo.mapTimeEqually(tasks)
        // then
        assertEquals(Task("A", "desc B", "2017-01-18", 150), newTasks[0])
        assertEquals(Task("C", "desc B", "2017-01-18", 150), newTasks[1])
    }

    @Test
    @DisplayName("it should log map it equally")
    fun mapTimeEquallyFull() {
        // given
        val tasks = listOf(taskA, taskB, taskC, taskD, taskE, taskF, taskG, taskH, taskI)
        val tempoClient = mock<TempoClient>()
        val tempo = Tempo(tempoClient)
        // when
        val newTasks = tempo.mapTimeEqually(tasks)
        // then
        assertEquals(6, newTasks.size)
        assertEquals(Task("A", "desc B", "2017-01-18", 100), newTasks[0])
        assertEquals(Task("C", "desc B", "2017-01-18", 100), newTasks[1])
        assertEquals(Task("D", "desc B", "2017-01-18", 100), newTasks[2])
        assertEquals(Task("F", "desc G", "2017-03-16", 600), newTasks[3])
        assertEquals(Task("F", "desc H", "2017-03-17", 600), newTasks[4])
        assertEquals(Task("WTAI-389", "desc I", "2017-04-16", 300), newTasks[5])
    }

    @Test
    @DisplayName("it should log all worklogs without key to workflow in this month")
    fun withoutKeys() {
        // given
        val username = "user"
        val password = "pass"
        val tasks = listOf(taskA, taskB, taskC)
        val tempoClient = mock<TempoClient> {
            on { create(any(), any(), any()) } doReturn 200
        }
        val tempo = Tempo(tempoClient)
        // when
        val tasksIds = tempo.logAllTasksWithoutKeys(username, password, tasks)
        // then
        assertEquals(2, tasksIds.size)
        assertEquals(200, tasksIds[0])
    }

    @Test
    @DisplayName("it should process real example")
    fun realExample() {
        // given
        val realData = """WTAI-681 WTAI-428	Populate ratings for TVPG	2017-01-02	32400	09:00	18:00	9h 0m	9.00
WTAI-426	dynamic config	2017-01-03	19800	10:30	16:00	5h 30m	5.50
WTAI-426	dynamic config	2017-01-03	1800	16:00	16:30	0h 30m	0.50
	Sprint	2017-01-03	1800	16:30	17:00	0h 30m	0.50
	Michal Ko & Co	2017-01-03	4200	17:00	18:10	1h 10m	1.17
WTAI-426 WTAI-551	dynamic config	2017-01-04	21900	12:10	18:15	6h 5m	6.08
WTAI-442	JIRA	2017-01-05	13200	09:20	13:00	3h 40m	3.67
WTAI-551	Dynamic fields (DB) (review)	2017-01-05	10800	13:00	16:00	3h 0m	3.00
WTAI-442	JIRA + workflow	2017-01-05	11400	16:00	19:10	3h 10m	3.17
WTAI-551	RatingANCINE, TVPG	2017-01-09	29700	09:15	17:30	8h 15m	8.25
WTAI-389	PTSX Load testing for Viswa	2017-01-09	2400	17:30	18:10	0h 40m	0.67
WTAI-552	JS in Java	2017-01-10	18600	13:50	19:00	5h 10m	5.17
WTAI-774	Review	2017-01-11	10200	11:00	13:50	2h 50m	2.83
WTAI-551	Review	2017-01-11	7800	13:50	16:00	2h 10m	2.17
	Sprint	2017-01-11	1500	16:00	16:25	0h 25m	0.42
WTAI-551	Review	2017-01-11	2100	16:25	17:00	0h 35m	0.58
	Worklog for tempo	2017-01-11	3600	17:00	18:00	1h 0m	1.00
WTAI-774	Review	2017-01-12	3000	11:00	11:50	0h 50m	0.83
	Sprint	2017-01-12	1800	16:00	16:30	0h 30m	0.50
WTAI-774	Review	2017-01-12	3000	16:30	17:20	0h 50m	0.83
WTAI-774	Review	2017-01-13	1800	08:00	08:30	0h 30m	0.50
WTAI-774	Review	2017-01-13	300	11:55	12:00	0h 5m	0.08
WTAI-774	Review	2017-01-13	1500	13:05	13:30	0h 25m	0.42"""
        val tempoClient = mock<TempoClient>()
        val tempo = Tempo(tempoClient)
        val tasks = TaskCsvFactory().fromCsv(realData)
        // when
        val newTasks = tempo.mapTimeEqually(tasks)
        // then
        assertEquals(90, newTasks.size)
        assertEquals(Task("A", "desc B", "2017-01-18", 100), newTasks[0])
        assertEquals(Task("C", "desc B", "2017-01-18", 100), newTasks[1])
        assertEquals(Task("D", "desc B", "2017-01-18", 100), newTasks[2])
        assertEquals(Task("F", "desc G", "2017-03-16", 600), newTasks[3])
        assertEquals(Task("F", "desc H", "2017-03-17", 600), newTasks[4])
        assertEquals(Task("WTAI-389", "desc I", "2017-04-16", 300), newTasks[5])
    }
}