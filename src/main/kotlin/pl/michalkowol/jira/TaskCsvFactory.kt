package pl.michalkowol.jira

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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
        val startTime = cells[3]
        val endTime = cells[4]
        val start = parseAsZonedDatetime(date, startTime)
        val end = parseAsZonedDatetime(date, endTime)
        return Task(key, comment, start, end)
    }

    private fun parseAsZonedDatetime(dateText: String, timeText: String): ZonedDateTime {
        val date = LocalDate.parse(dateText, DateTimeFormatter.ISO_DATE)
        val time = LocalTime.parse(timeText, DateTimeFormatter.ISO_TIME)
        return ZonedDateTime.of(date, time, ZoneId.of("America/New_York"))
    }
}
