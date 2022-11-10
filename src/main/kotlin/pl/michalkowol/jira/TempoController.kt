package pl.michalkowol.jira

import pl.michalkowol.web.Controller
import pl.michalkowol.web.errors.BadRequestException
import spark.Request
import spark.Response
import spark.Spark.post
import java.net.HttpURLConnection.HTTP_NO_CONTENT

@Suppress("ThrowsCount")
class TempoController(
    private val taskCsvFactory: TaskCsvFactory,
    private val tempo: Tempo
) : Controller {

    override fun start() {
        post("/worklog", this::createWorklog)
        post("/worklogFromForm", this::worklogFromForm)
    }

    private fun worklogFromForm(request: Request, response: Response) {
        response.status(HTTP_NO_CONTENT)
        val queryMap = request.queryMap()
        val cookie = queryMap["cookie"].value() ?: throw BadRequestException("cookie")
        val csv = queryMap["tasks"].value() ?: throw BadRequestException("tasks")
        val tasks = taskCsvFactory.fromCsv(csv)
        tempo.logTasks(tasks, cookie)
    }

    private fun createWorklog(request: Request, response: Response): String {
        response.type("text/plain")
        val csv = request.body()
        val cookie = request.headers("Cookie")
        val tasks = taskCsvFactory.fromCsv(csv)
        val ids = tempo.logTasks(tasks, cookie)
        return "Created ${ids.size} tasks"
    }
}
