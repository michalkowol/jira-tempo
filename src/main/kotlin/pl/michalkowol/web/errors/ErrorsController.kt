package pl.michalkowol.web.errors

import com.softwareberg.JsonMapper
import org.slf4j.LoggerFactory
import pl.michalkowol.web.Controller
import spark.Spark.exception

class ErrorsController(private val jsonMapper: JsonMapper) : Controller {

    private val log = LoggerFactory.getLogger(ErrorsController::class.java)

    override fun start() {
        exception(NotFoundException::class.java) { ex, request, response ->
            log.info(request.url(), ex)
            response.type("application/json")
            val notFound = NotFound(ex.message)
            response.status(notFound.status)
            response.body(jsonMapper.write(notFound))
        }

        exception(BadRequestException::class.java) { ex, request, response ->
            log.info(request.url(), ex)
            response.type("application/json")
            val badRequest = BadRequest(ex.message)
            response.status(badRequest.status)
            response.body(jsonMapper.write(badRequest))
        }

        exception(Exception::class.java) { ex, request, response ->
            log.error(request.url(), ex)
            response.type("application/json")
            val internalServerError = InternalServerError.create(ex)
            response.status(internalServerError.status)
            response.body(jsonMapper.write(internalServerError))
        }
    }

}
