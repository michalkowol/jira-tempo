package pl.michalkowol.web

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import pl.michalkowol.jira.JiraWorklogException
import pl.michalkowol.nip.NipServiceException

@RestControllerAdvice
class ErrorHandler : ResponseEntityExceptionHandler() {

    private val log = LoggerFactory.getLogger(ErrorHandler::class.java)

    @ExceptionHandler(NipServiceException::class)
    fun handleNipServiceException(ex: NipServiceException): ProblemDetail {
        log.error("NIP service error [message={}]", ex.message, ex)
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY, ex.message ?: "NIP service error")
    }

    @ExceptionHandler(JiraWorklogException::class)
    fun handleJiraWorklogException(ex: JiraWorklogException): ProblemDetail {
        log.error(
            "Jira worklog error [taskKey={}, status={}, response={}, request={}]",
            ex.taskKey,
            ex.status,
            ex.responseBody,
            ex.requestBody,
            ex
        )
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY, ex.message ?: "Jira worklog error")
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ProblemDetail {
        log.warn("Bad request [message={}]", ex.message, ex)
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.message ?: "Invalid argument")
    }

    @ExceptionHandler(Exception::class)
    fun handleUnexpected(ex: Exception): ProblemDetail {
        log.error("Unexpected error [message={}]", ex.message, ex)
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error")
    }
}
