package pl.michalkowol

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TaskFactorySpec {

    @Test
    @DisplayName("it should parse one line in csv to task")
    fun fromRow() {
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
    @DisplayName("it should parse one line with quotes and comma inside comment in csv to task")
    fun fromRowWithComma() {
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
    @DisplayName("it should parse one line with extra column in csv to task")
    fun fromRowWithExtraColumns() {
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
    @DisplayName("it should parse line with key as empty value")
    fun withoutKey() {
        // given
        val row = "\tcomment ok\t2016-08-26\t600"
        // when
        val task = TaskCsvFactory().fromRow(row)
        // then
        assertEquals(null, task.key)
        assertEquals("comment ok", task.comment)

        assertEquals("2016-08-26", task.date)
        assertEquals(600, task.timeInSec)
    }

    @Test
    @DisplayName("it should log to first key")
    fun withTwoKeys() {
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