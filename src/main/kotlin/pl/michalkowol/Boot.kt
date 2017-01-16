package pl.michalkowol

import org.asynchttpclient.DefaultAsyncHttpClient
import org.slf4j.LoggerFactory
import spark.Request
import spark.Response
import spark.Spark.*

fun main(args: Array<String>) {
    val taskCsvFactory = TaskCsvFactory()
    val asyncHttpClient = DefaultAsyncHttpClient()
    val tempoClient = TempoClient(SimpleHttpClient(asyncHttpClient))
    val tempo = Tempo(tempoClient)
    Boot(taskCsvFactory, asyncHttpClient, tempo).start()
}

class Boot(private val taskCsvFactory: TaskCsvFactory, private val asyncHttpClient: DefaultAsyncHttpClient, private val tempo: Tempo) {

    private val log = LoggerFactory.getLogger(Boot::class.java)

    fun start() {
        port(assignedPort())
        post("/parse", this::parse)
        delete("/worklog", this::delete)
        post("/worklog", this::createWorklog)
        post("/stop", this::shutdown)

        exception(Exception::class.java, { e, request, response ->
            log.error(request.url(), e)
        })
    }

    private fun parse(request: Request, response: Response): String {
        response.type("text/plain")
        val csv = request.body()
        val tasks = taskCsvFactory.fromCsv(csv)
        return tasks.joinToString("\n")
    }

    private fun createWorklog(request: Request, response: Response) {
        val csv = request.body()
        val auth = request.headers("Authorization")
        val (username, password) = HttpHeader.basicAuth(auth)
        val tasks = taskCsvFactory.fromCsv(csv)
        tempo.logTasks(username, password, tasks)
    }

    private fun shutdown(request: Request, response: Response) {
        asyncHttpClient.close()
        stop()
    }

    private fun delete(request: Request, response: Response): String {
        response.type("text/plain")
        val auth = request.headers("Authorization")
        val (username, password) = HttpHeader.basicAuth(auth)
        val ids = request.body().lines().map(Integer::parseInt)
        ids.forEach { tempo.delete(username, password, it) }
        return "Deleted ${ids.size} tasks"
    }

    fun assignedPort(): Int {
        val processBuilder = ProcessBuilder()
        if (processBuilder.environment()["PORT"] != null) {
            return Integer.parseInt(processBuilder.environment()["PORT"])
        }
        return 4567
    }
}
