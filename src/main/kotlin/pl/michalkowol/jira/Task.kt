package pl.michalkowol.jira

import java.time.ZonedDateTime

data class Task(val key: String, val comment: String, val start: ZonedDateTime, val end: ZonedDateTime)
