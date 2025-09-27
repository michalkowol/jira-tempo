package pl.michalkowol.proxy

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClient

@RestController
@RequestMapping(consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
class ProxyController(private val restClient: RestClient) {

    @CrossOrigin
    @PostMapping("/forward/**")
    fun forwardRequest(
        @RequestBody body: String?,
        @RequestHeader headers: HttpHeaders,
        request: HttpServletRequest
    ): ResponseEntity<String> {
        val originalHost = headers.getFirst("X-Original-Host") ?: throw IllegalArgumentException("X-Original-Host header is required")
        val path = request.requestURI.removePrefix("/forward")
        val targetUrl = "https://$originalHost$path"
        val forwardedHeaders = HttpHeaders(headers)
        forwardedHeaders.remove("X-Original-Host")
        forwardedHeaders.replace("Host", listOf(originalHost))
        return try {
            restClient.post()
                .uri(targetUrl)
                .headers { it.addAll(forwardedHeaders) }
                .body(body ?: "")
                .retrieve()
                .toEntity(String::class.java)
        } catch (e: Exception) {
            ResponseEntity.status(500).body("Error forwarding request: ${e.message}")
        }
    }
}
