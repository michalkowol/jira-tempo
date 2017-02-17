package pl.michalkowol

import org.junit.Assert.assertEquals
import org.junit.Test

class TaskCsvFactorySpec {

    @Test
    fun `it should parse one line in csv to task`() {
        // given
        val row = "WTAI-123\tcomment ok\t2016-08-26\t600"
        // when
        val task = TaskCsvFactory().fromRow(row)
        // then
        assertEquals("WTAI-123", task.key)
        assertEquals("comment ok", task.comment)
        assertEquals("2016-08-26", task.date)
        assertEquals(600, task.timeInSec)
    }

    @Test
    fun `it should parse one line with quotes and comma inside comment in csv to task`() {
        // given
        val row = "WTAI-123\tcomment, ok\t2016-08-26\t600"
        // when
        val task = TaskCsvFactory().fromRow(row)
        // then
        assertEquals("WTAI-123", task.key)
        assertEquals("comment, ok", task.comment)
        assertEquals("2016-08-26", task.date)
        assertEquals(600, task.timeInSec)
    }

    @Test
    fun `it should parse one line with extra column in csv to task`() {
        // given
        val row = "WTAI-123\tcomment ok\t2016-08-26\t600\textra"
        // when
        val task = TaskCsvFactory().fromRow(row)
        // then
        assertEquals("WTAI-123", task.key)
        assertEquals("comment ok", task.comment)

        assertEquals("2016-08-26", task.date)
        assertEquals(600, task.timeInSec)
    }

    @Test
    fun `it should log to first key`() {
        // given
        val row = "WTAI-681 WTAI-428\tcomment ok\t2016-08-26\t600"
        // when
        val task = TaskCsvFactory().fromRow(row)
        // then
        assertEquals("WTAI-681", task.key)
        assertEquals("comment ok", task.comment)

        assertEquals("2016-08-26", task.date)
        assertEquals(600, task.timeInSec)
    }
}