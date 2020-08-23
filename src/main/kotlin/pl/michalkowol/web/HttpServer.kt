package pl.michalkowol.web

import pl.michalkowol.configurations.ServerConfiguration
import pl.michalkowol.jira.TempoController
import pl.michalkowol.web.errors.ErrorsController
import spark.Spark.port

class HttpServer(
    private val serverConfiguration: ServerConfiguration,
    private val errorsController: ErrorsController,
    private val staticFilesController: StaticFilesController,
    private val tempoController: TempoController
) {

    fun start() {
        port(serverConfiguration.port)
        errorsController.start()
        staticFilesController.start()
        tempoController.start()
    }
}
