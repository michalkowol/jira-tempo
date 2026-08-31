package pl.michalkowol.jira

class JiraWorklogException(
    val taskKey: String,
    val status: Int,
    val responseBody: String,
    val requestBody: String,
    cause: Throwable
) : Exception("Failed to create Jira worklog [taskKey=$taskKey, status=$status, response=$responseBody]", cause)
