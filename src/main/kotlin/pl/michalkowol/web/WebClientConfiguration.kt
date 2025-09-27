package pl.michalkowol.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.michalkowol.jira.JiraWebClient
import pl.michalkowol.nip.RegonWebClient

@Configuration(proxyBeanMethods = false)
class WebClientConfiguration(private val factory: WebClientFactory) {

    @Bean
    fun jiraWebClient(properties: WebProperties): JiraWebClient {
        return factory.createClient<JiraWebClient>(baseUrl = properties.jiraUrl)
    }

    @Bean
    fun regonWebClient(properties: WebProperties): RegonWebClient {
        return factory.createClient<RegonWebClient>(baseUrl = properties.regonUrl)
    }
}
