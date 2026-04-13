package pl.michalkowol.nip

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class JacksonXmlConfiguration {

    @Bean
    fun customXmlMapper(): CustomXmlMapper {
        return CustomXmlMapper(XmlMapper())
    }

    class CustomXmlMapper(val objectMapper: ObjectMapper) {
        fun readTree(xml: String): JsonNode = objectMapper.readTree(xml)
        fun readArray(xml: String): List<JsonNode> = objectMapper.readValue(xml, object : TypeReference<List<JsonNode>>() {})
    }
}
