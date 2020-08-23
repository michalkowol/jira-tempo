package pl.michalkowol.jira

import java.time.Duration
import java.time.LocalTime

@Suppress("MagicNumber")
class TaskCsvFactory {

    fun fromCsv(csv: String): List<Task> {
        val rows = csv.lines()
        return fromRows(rows)
    }

    private fun fromRows(rows: List<String>): List<Task> = rows.map { fromRow(it) }

    private fun fromRow(row: String): Task {
        val cells = row.split("\t")
        val key = cells[0].split("\\s".toRegex()).first()
        val comment = cells[1]
        val date = cells[2]
        val start = LocalTime.parse(cells[3])
        val end = LocalTime.parse(cells[4])
        val duration = Duration.between(start, end)
        return Task(key, comment, date, duration)
    }
}
