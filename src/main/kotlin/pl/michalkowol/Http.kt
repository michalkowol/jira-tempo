package pl.michalkowol

import io.netty.handler.codec.http.DefaultHttpHeaders
import org.asynchttpclient.AsyncHttpClient
import org.asynchttpclient.Request
import org.asynchttpclient.RequestBuilder
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.CompletableFuture

enum class HttpMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE
}
data class UsernameWithPassword(val username: String, val password: String)
data class HttpHeader(val name: String, val value: String) {
    companion object {
        fun basicAuth(username: String, password: String): HttpHeader {
            val encoded = Base64.getEncoder().encodeToString("$username:$password".toByteArray(StandardCharsets.UTF_8))
            return HttpHeader("Authorization", "Basic $encoded")
        }
        fun basicAuth(authHeaderValue: String): UsernameWithPassword {
            val withoutBasic = authHeaderValue.removePrefix("Basic ")
            val decode = Base64.getDecoder().decode(withoutBasic).toString(StandardCharsets.UTF_8)
            val usernameWithPassword = decode.split(':', limit = 2)
            return UsernameWithPassword(usernameWithPassword[0], usernameWithPassword[1])
        }
    }
}
data class HttpRequest(val method: HttpMethod, val url: String, val headers: List<HttpHeader> = emptyList(), val body: String? = null)
data class HttpResponse(val statusCode: Int, val body: String? = null)

interface HttpClient {
    fun executeAsync(request: HttpRequest): CompletableFuture<HttpResponse>
    fun execute(request: HttpRequest): HttpResponse
}

class SimpleHttpClient(private val asyncHttpClient: AsyncHttpClient) : HttpClient {

    override fun execute(request: HttpRequest): HttpResponse = executeAsync(request).get()

    override fun executeAsync(request: HttpRequest): CompletableFuture<HttpResponse> {
        return asyncHttpClient
                .prepareRequest(toAsyncRequest(request))
                .execute()
                .toCompletableFuture()
                .thenApply({ resp -> mapToResponse(resp) })
    }

    private fun toAsyncRequest(request: HttpRequest): Request {
        val headers: DefaultHttpHeaders = request.headers.fold(DefaultHttpHeaders(), { httpHeaders, header ->
            httpHeaders.add(header.name, header.value)
            httpHeaders
        })
        val requestBuilder = RequestBuilder()
                .setUrl(request.url)
                .setMethod(request.method.name)
                .setHeaders(headers)
        if (request.body != null) {
            requestBuilder.setBody(request.body)
        }
        return requestBuilder.build()
    }

    private fun mapToResponse(response: org.asynchttpclient.Response): HttpResponse {
        val body = response.responseBody
        val statusCode = response.statusCode
        return HttpResponse(statusCode, body)
    }
}
