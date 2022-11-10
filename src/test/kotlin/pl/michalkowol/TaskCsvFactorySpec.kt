package pl.michalkowol

import org.junit.Assert.assertEquals
import org.junit.Test
import pl.michalkowol.jira.TaskCsvFactory
import java.time.format.DateTimeParseException

class TaskCsvFactorySpec {

    @Test
    fun `it should parse one line in csv to task`() {
        // given
        val row = "WTAI-123\tcomment ok\t2016-08-26\t13:10\t13:20"

        // when
        val task = TaskCsvFactory().fromCsv(row).first()

        // then
        assertEquals("WTAI-123", task.key)
        assertEquals("comment ok", task.comment)
        assertEquals("2016-08-26T13:10-04:00[America/New_York]", task.start.toString())
        assertEquals("2016-08-26T13:20-04:00[America/New_York]", task.end.toString())
    }

    @Test
    fun `it should parse one line in csv to task (dates should have prefix 0 - 09_10 instead of 9_10)`() {
        // given
        val row = "WTAI-123\tcomment ok\t2016-08-26\t09:10\t13:20"

        // when
        val task = TaskCsvFactory().fromCsv(row).first()

        // then
        assertEquals("WTAI-123", task.key)
        assertEquals("comment ok", task.comment)
        assertEquals("2016-08-26T09:10-04:00[America/New_York]", task.start.toString())
        assertEquals("2016-08-26T13:20-04:00[America/New_York]", task.end.toString())
    }

    @Test(expected = DateTimeParseException::class)
    fun `it should fail on tasks without 0 prefix`() {
        // given
        val row = "WTAI-123\tcomment ok\t2016-08-26\t9:10\t13:20"

        // when
        TaskCsvFactory().fromCsv(row)
    }

    @Test
    fun `it should parse one line with quotes and comma inside comment in csv to task`() {
        // given
        val row = "WTAI-123\tcomment, ok\t2016-08-26\t12:00\t12:10"

        // when
        val task = TaskCsvFactory().fromCsv(row).first()

        // then
        assertEquals("WTAI-123", task.key)
        assertEquals("comment, ok", task.comment)
        assertEquals("2016-08-26T12:00-04:00[America/New_York]", task.start.toString())
        assertEquals("2016-08-26T12:10-04:00[America/New_York]", task.end.toString())
    }

    @Test
    fun `it should parse one line with extra column in csv to task`() {
        // given
        val row = "WTAI-123\tcomment ok\t2016-08-26\t12:00\t12:10\textra"

        // when
        val task = TaskCsvFactory().fromCsv(row).first()

        // then
        assertEquals("WTAI-123", task.key)
        assertEquals("comment ok", task.comment)
        assertEquals("2016-08-26T12:00-04:00[America/New_York]", task.start.toString())
        assertEquals("2016-08-26T12:10-04:00[America/New_York]", task.end.toString())
    }

    @Test
    fun `it should log to first key`() {
        // given
        val row = "WTAI-681 WTAI-428\tcomment ok\t2016-08-26\t12:00\t12:10"

        // when
        val task = TaskCsvFactory().fromCsv(row).first()

        // then
        assertEquals("WTAI-681", task.key)
        assertEquals("comment ok", task.comment)
        assertEquals("2016-08-26T12:00-04:00[America/New_York]", task.start.toString())
        assertEquals("2016-08-26T12:10-04:00[America/New_York]", task.end.toString())
    }
}
