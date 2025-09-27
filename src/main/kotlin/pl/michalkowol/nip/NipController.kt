package pl.michalkowol.nip

import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
@RequestMapping(consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
class NipController(private val nipService: NipService) {

    @PostMapping("/api/v1/nip/bulk-check")
    fun checkNipsBulk(@RequestBody request: NipBulkRequestDto): NipBulkResponseDto {
        val companies = nipService.checkNipsBulk(request.data)
        return NipBulkResponseDto(companies)
    }
}
