package pl.michalkowol

import com.softwareberg.HttpHeader
import com.softwareberg.SimpleHttpClient
import org.slf4j.LoggerFactory
import spark.Request
import spark.Response
import spark.Spark.*
import java.io.PrintWriter
import java.io.StringWriter

fun main(args: Array<String>) {
    val taskCsvFactory = TaskCsvFactory()
    val httpClient = SimpleHttpClient.create()
    val tempoClient = TempoClient(httpClient)
    val tempo = Tempo(tempoClient)
    Boot(taskCsvFactory, tempo).start()
}

class Boot(private val taskCsvFactory: TaskCsvFactory, private val tempo: Tempo) {

    private val log = LoggerFactory.getLogger(Boot::class.java)

    fun start() {
        port(assignedPort())
        delete("/worklog", this::delete)
        post("/worklog", this::createWorklog)

        exception(Exception::class.java, { e, request, response ->
            log.error(request.url(), e)
            val errorMsgWriter = StringWriter()
            e.printStackTrace(PrintWriter(errorMsgWriter))
            val errorMsg = errorMsgWriter.toString()
            response.type("text/plain")
            response.status(500)
            response.body(errorMsg)
        })
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

    private fun assignedPort(): Int {
        val envs = System.getenv()
        return envs["PORT"]?.toInt() ?: 8080
    }
}
