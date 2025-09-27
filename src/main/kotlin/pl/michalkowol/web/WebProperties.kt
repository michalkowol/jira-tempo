package pl.michalkowol.web

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.URL
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.web")
class WebProperties(
    @field:NotBlank
    @field:URL
    val jiraUrl: String,

    @field:NotBlank
    @field:URL
    val regonUrl: String
)
