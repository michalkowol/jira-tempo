package pl.michalkowol.web

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.web")
class WebProperties(val jiraUrl: String)
