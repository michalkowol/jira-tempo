package pl.michalkowol.jira

import java.time.Duration

data class Task(val key: String, val comment: String, val date: String, val duration: Duration)
