package pl.michalkowol.web.errors

import java.io.PrintWriter
import java.io.StringWriter
import java.net.HttpURLConnection.HTTP_BAD_REQUEST
import java.net.HttpURLConnection.HTTP_INTERNAL_ERROR
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import java.util.UUID

interface ServerError {
    val status: Int
    val code: String
    val id: UUID
    val message: String?
}

class NotFound(
    override val message: String,
    override val status: Int = HTTP_NOT_FOUND,
    override val code: String = "NF",
    override val id: UUID = UUID.randomUUID()
) : ServerError

class BadRequest(
    override val message: String,
    override val status: Int = HTTP_BAD_REQUEST,
    override val code: String = "BR",
    override val id: UUID = UUID.randomUUID()
) : ServerError

class InternalServerError(
    override val message: String?,
    val stackTrace: String?,
    override val status: Int = HTTP_INTERNAL_ERROR,
    override val code: String = "IE",
    override val id: UUID = UUID.randomUUID()
) : ServerError {

    companion object {

        fun create(ex: Exception): InternalServerError = InternalServerError(ex.message, extractStackTrace(ex))

        private fun extractStackTrace(throwable: Throwable): String {
            val errorMsgWriter = StringWriter()
            throwable.printStackTrace(PrintWriter(errorMsgWriter))
            return errorMsgWriter.toString()
        }
    }
}
