package pl.michalkowol.nip

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter

@Configuration(proxyBeanMethods = false)
class JacksonXmlConfiguration {

    @Bean
    fun xmlMapper(converter: MappingJackson2XmlHttpMessageConverter): CustomXmlMapper {
        return CustomXmlMapper(converter.objectMapper)
    }

    class CustomXmlMapper(val objectMapper: ObjectMapper) {
        fun readTree(xml: String): JsonNode = objectMapper.readTree(xml)
        fun readArray(xml: String): List<JsonNode> = objectMapper.readValue(xml, object : TypeReference<List<JsonNode>>() {})
    }
}
