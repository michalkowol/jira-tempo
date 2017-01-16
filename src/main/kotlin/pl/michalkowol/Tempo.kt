package pl.michalkowol

import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter

class Tempo(private val tempoClient: TempoClient) {

    fun logTasks(username: String, password: String, tasks: List<Task>): List<Int> {
        val idsA = logAllTasksWithKeys(username, password, tasks)
        val idsB = logAllTasksWithoutKeys(username, password, tasks)
        return idsA + idsB
    }

    internal fun logAllTasksWithKeys(username: String, password: String, tasks: List<Task>): List<Int> {
        val tasksWithKeys = tasks.filter { it.key != null }
        val taskIds = tasksWithKeys.map { task -> tempoClient.create(username, password, task) }
        return taskIds
    }

    internal fun logAllTasksWithoutKeys(username: String, password: String, tasks: List<Task>): List<Int> {
        val mappedTasks = mapTimeEqually(tasks)
        val taskIds = mappedTasks.map { task -> tempoClient.create(username, password, task) }
        return taskIds
    }

    internal fun mapTimeEqually(tasks: List<Task>): List<Task> {
        val (withKeys, withoutKeys) = tasks.partition { it.key != null }
        val byMonthWithKeys = groupByMonths(withKeys)
        val byMonthWithoutKeys = groupByMonths(withoutKeys)
        return byMonthWithoutKeys.flatMap {
            val month = it.key
            val withoutKeys = it.value
            val withKeysWithFallback = byMonthWithKeys[month] ?: listOf(Task("WTAI-389", null, withoutKeys.first().date, 0))
            val timeInSecsPerTask = withoutKeys.sumBy(Task::timeInSec) / withKeysWithFallback.size
            withoutKeys.flatMap { taskCommentDate ->
                withKeysWithFallback.map { taskKey ->
                    taskKey.copy(comment = taskCommentDate.comment, date = taskCommentDate.date, timeInSec = timeInSecsPerTask)
                }
            }
        }
    }

    private fun groupByMonths(tasks: List<Task>): Map<Month, List<Task>> {
        return tasks.groupBy { task -> LocalDate.parse(task.date, DateTimeFormatter.ISO_DATE).month }
    }

    fun delete(username: String, password: String, workflowId: Int): Int {
        return tempoClient.delete(username, password, workflowId)
    }
}