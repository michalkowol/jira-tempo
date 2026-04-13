package pl.michalkowol.nip

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tools.jackson.core.type.TypeReference
import tools.jackson.databind.JsonNode
import tools.jackson.dataformat.xml.XmlMapper

@Configuration(proxyBeanMethods = false)
class JacksonXmlConfiguration {

    @Bean
    fun customXmlMapper(xmlMapper: XmlMapper): CustomXmlMapper {
        return CustomXmlMapper(xmlMapper)
    }

    class CustomXmlMapper(private val xmlMapper: XmlMapper) {
        fun readTree(xml: String): JsonNode = xmlMapper.readTree(xml)
        fun readArray(xml: String): List<JsonNode> = xmlMapper.readValue(xml, object : TypeReference<List<JsonNode>>() {})
    }
}
