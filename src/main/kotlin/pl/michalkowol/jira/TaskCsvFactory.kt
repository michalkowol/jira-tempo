package pl.michalkowol.jira

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
        val timeInSec = Integer.parseInt(cells[3])
        return Task(key, comment, date, timeInSec)
    }
}
