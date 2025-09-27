package pl.michalkowol.nip

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.service.annotation.PostExchange

interface RegonWebClient {

    @PostExchange("/wsBIR/UslugaBIRzewnPubl.svc", contentType = "application/soap+xml")
    fun login(
        @RequestBody body: String
    ): ResponseEntity<String>

    @PostExchange("/wsBIR/UslugaBIRzewnPubl.svc", contentType = "application/soap+xml")
    fun searchCompanies(
        @RequestHeader("sid") sessionId: SessionId,
        @RequestBody body: String
    ): ResponseEntity<String>
}
