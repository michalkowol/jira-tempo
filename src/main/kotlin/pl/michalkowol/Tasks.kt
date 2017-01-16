package pl.michalkowol

data class Task(val key: String, val comment: String, val date: String, val timeInSec: Int)

class TaskCsvFactory {
    fun fromCsv(csv: String): List<Task> {
        val rows = csv.lines()
        return fromRows(rows)
    }

    internal fun fromRows(rows: List<String>): List<Task> = rows.map { fromRow(it) }

    internal fun fromRow(row: String): Task {
        val cells = row.split("\t")
        val key = cells[0].split("\\s".toRegex()).first()
        val comment = cells[1]
        val date = cells[2]
        val timeInSec = Integer.parseInt(cells[3])
        val task = Task(key, comment, date, timeInSec)
        return task
    }
}