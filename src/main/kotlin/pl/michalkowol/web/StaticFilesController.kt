package pl.michalkowol.web

import spark.Spark

class StaticFilesController : Controller {

    override fun start() {
        Spark.staticFiles.location("/public")
    }

}
