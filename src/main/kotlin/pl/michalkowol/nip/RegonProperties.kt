package pl.michalkowol.nip

import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.regon")
data class RegonProperties(
    @field:NotBlank val apiKey: String
)
