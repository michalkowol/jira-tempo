package pl.michalkowol.jira

import com.softwareberg.HttpHeader
import pl.michalkowol.web.Controller
import pl.michalkowol.web.errors.BadRequestException
import spark.Request
import spark.Response
import spark.Spark.delete
import spark.Spark.post
import java.net.HttpURLConnection.HTTP_NO_CONTENT

@Suppress("ThrowsCount")
class TempoController(
    private val taskCsvFactory: TaskCsvFactory,
    private val tempo: Tempo
) : Controller {

    override fun start() {
        delete("/worklog", this::delete)
        post("/worklog", this::createWorklog)
        post("/worklogFromForm", this::worklogFromForm)
    }

    private fun worklogFromForm(request: Request, response: Response) {
        response.status(HTTP_NO_CONTENT)
        val queryMap = request.queryMap()
        val username = queryMap["username"].value() ?: throw BadRequestException("username")
        val password = queryMap["password"].value() ?: throw BadRequestException("password")
        val csv = queryMap["tasks"].value() ?: throw BadRequestException("tasks")
        val tasks = taskCsvFactory.fromCsv(csv)
        tempo.logTasks(username, password, tasks)
    }

    private fun createWorklog(request: Request, response: Response): String {
        response.type("text/plain")
        val csv = request.body()
        val auth = request.headers("Authorization")
        val (username, password) = HttpHeader.basicAuth(auth)
        val tasks = taskCsvFactory.fromCsv(csv)
        val ids = tempo.logTasks(username, password, tasks)
        return "Created ${ids.size} tasks"
    }

    private fun delete(request: Request, response: Response): String {
        response.type("text/plain")
        val auth = request.headers("Authorization")
        val (username, password) = HttpHeader.basicAuth(auth)
        val ids = request.body().lines().map(Integer::parseInt)
        ids.forEach { tempo.delete(username, password, it) }
        return "Deleted ${ids.size} tasks"
    }
}
