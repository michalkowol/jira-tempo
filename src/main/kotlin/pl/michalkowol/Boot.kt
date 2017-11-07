package pl.michalkowol

import pl.michalkowol.configurations.Configuration
import pl.michalkowol.web.HttpServer

fun main(args: Array<String>) {
    Boot().start()
}

class Boot {

    fun start() {
        val injector = Configuration.injector
        val httpServer = injector.getInstance(HttpServer::class.java)
        httpServer.start()
    }

}
