package pl.michalkowol.nip

import org.springframework.ai.tool.annotation.Tool
import org.springframework.ai.tool.annotation.ToolParam
import org.springframework.stereotype.Component
import pl.michalkowol.nip.JacksonXmlConfiguration.CustomXmlMapper

@Component
class NipService(
    private val regonWebClient: RegonWebClient,
    private val regonProperties: RegonProperties,
    private val xmlMapper: CustomXmlMapper
) {

    @Tool(description = "Check company information by NIP (Polish Tax Identification Number). Supports bulk requests.")
    fun checkNipsBulkMcpAdapter(@ToolParam(description = "List of NIPs (Polish Tax Identification Number)") nips: List<String>): List<CompanyInfo> {
        return checkNipsBulk(nips.map { Nip(it) })
    }

    fun checkNipsBulk(nips: List<Nip>): List<CompanyInfo> {
        val sessionId = login()
        return searchAndParseCompanies(sessionId, nips)
    }

    private fun searchAndParseCompanies(sessionId: SessionId, nips: List<Nip>): List<CompanyInfo> {
        val companiesResponse = searchCompanies(sessionId, nips)
        return parseCompaniesFromResponse(companiesResponse)
    }

    private fun parseCompaniesFromResponse(soapResponse: String): List<CompanyInfo> {
        return extractCompanyDataFromSoap(soapResponse)
            ?.let { parseRegonXml(it) }
            ?: emptyList()
    }

    private fun login(): SessionId {
        val loginBody = """
            <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope"
            xmlns:ns="http://CIS/BIR/PUBL/2014/07">
            <soap:Header xmlns:wsa="http://www.w3.org/2005/08/addressing">
            <wsa:To>https://wyszukiwarkaregontest.stat.gov.pl/wsBIR/UslugaBIRzewnPubl.svc</wsa:To>
            <wsa:Action>http://CIS/BIR/PUBL/2014/07/IUslugaBIRzewnPubl/Zaloguj</wsa:Action>
            </soap:Header>
            <soap:Body>
            <ns:Zaloguj>
            <ns:pKluczUzytkownika>${regonProperties.apiKey}</ns:pKluczUzytkownika>
            </ns:Zaloguj>
            </soap:Body>
            </soap:Envelope>
        """.trimIndent()

        val response = regonWebClient.login(loginBody)
        return SessionId(extractSessionId(response.body ?: throw EmptyResponseBodyException()))
    }

    private fun extractSessionId(response: String): String {
        return try {
            val xmlContent = extractXmlFromMultipart(response)
            val root = xmlMapper.readTree(xmlContent)
            root.path("Body").path("ZalogujResponse").path("ZalogujResult").asText()
                .takeIf { it.isNotBlank() }
                ?: throw NoLoginResultException()
        } catch (e: Exception) {
            throw SessionExtractionException("Unable to extract session ID from response: ${e.message}", e)
        }
    }

    private fun searchCompanies(sessionId: SessionId, nips: List<Nip>): String {
        val nipsString = nips.joinToString(" ") { it.value }

        val searchBody = """
            <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope"
            xmlns:ns="http://CIS/BIR/PUBL/2014/07"
            xmlns:dat="http://CIS/BIR/PUBL/2014/07/DataContract">
            <soap:Header xmlns:wsa="http://www.w3.org/2005/08/addressing">
            <wsa:To>https://wyszukiwarkaregontest.stat.gov.pl/wsBIR/UslugaBIRzewnPubl.svc</wsa:To>
            <wsa:Action>http://CIS/BIR/PUBL/2014/07/IUslugaBIRzewnPubl/DaneSzukajPodmioty</wsa:Action>
            </soap:Header>
            <soap:Body>
            <ns:DaneSzukajPodmioty>
            <ns:pParametryWyszukiwania>
            <dat:Nipy>$nipsString</dat:Nipy>
            </ns:pParametryWyszukiwania>
            </ns:DaneSzukajPodmioty>
            </soap:Body>
            </soap:Envelope>
        """.trimIndent()

        val response = regonWebClient.searchCompanies(sessionId, searchBody)
        return response.body ?: throw EmptyResponseBodyException()
    }

    private fun extractCompanyDataFromSoap(soapResponse: String): String? {
        return try {
            val xmlContent = extractXmlFromMultipart(soapResponse)
            val root = xmlMapper.readTree(xmlContent)
            root.path("Body").path("DaneSzukajPodmiotyResponse").path("DaneSzukajPodmiotyResult").asText()
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&#xD;", "")
                .replace("&#x", "")
                .takeIf { it.isNotBlank() }
        } catch (_: Exception) {
            null
        }
    }

    private fun extractXmlFromMultipart(multipartResponse: String): String {
        return extractXmlBetweenMarkers(multipartResponse, "<s:Envelope", "</s:Envelope>")
    }

    private fun parseRegonXml(xmlData: String): List<CompanyInfo> {
        return try {
            val cleanXmlData = extractRegonXmlContent(xmlData)
            val root = xmlMapper.readArray(cleanXmlData)
            root.map { companyNode ->
                CompanyInfo(
                    regon = companyNode.path("Regon").asText(""),
                    nip = companyNode.path("Nip").asText(""),
                    nipStatus = companyNode.path("StatusNip").asText("").takeIf { it.isNotBlank() },
                    name = companyNode.path("Nazwa").asText(""),
                    voivodeship = companyNode.path("Wojewodztwo").asText(""),
                    county = companyNode.path("Powiat").asText(""),
                    commune = companyNode.path("Gmina").asText(""),
                    city = companyNode.path("Miejscowosc").asText(""),
                    postalCode = companyNode.path("KodPocztowy").asText(""),
                    street = companyNode.path("Ulica").asText(""),
                    buildingNumber = companyNode.path("NrNieruchomosci").asText(""),
                    apartmentNumber = companyNode.path("NrLokalu").asText("").takeIf { it.isNotBlank() },
                    type = companyNode.path("Typ").asText(""),
                    silosId = companyNode.path("SilosID").asText(""),
                    businessEndDate = companyNode.path("DataZakonczeniaDzialalnosci").asText("").takeIf { it.isNotBlank() },
                    postalCity = companyNode.path("MiejscowoscPoczty").asText("")
                )
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun extractRegonXmlContent(xmlData: String): String {
        return extractXmlBetweenMarkers(xmlData, "<DaneSzukajPodmiotyResponse>", "</DaneSzukajPodmiotyResponse>")
    }

    private fun extractXmlBetweenMarkers(content: String, startMarker: String, endMarker: String): String {
        val startIndex = content.indexOf(startMarker)
        val endIndex = content.indexOf(endMarker) + endMarker.length

        return if (startIndex != -1 && endIndex > startIndex) {
            content.substring(startIndex, endIndex).trim()
        } else {
            content.trim()
        }
    }
}
