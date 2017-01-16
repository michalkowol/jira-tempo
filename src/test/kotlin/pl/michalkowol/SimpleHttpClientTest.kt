package pl.michalkowol

import org.asynchttpclient.DefaultAsyncHttpClient
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import pl.michalkowol.HttpMethod.GET

class SimpleHttpClientTest {
    @Test
    @DisplayName("it should exec GET")
    fun get() {
        // given
        val asyncHttpClient = DefaultAsyncHttpClient()
        val httpClient = SimpleHttpClient(asyncHttpClient)

        // when
        val response = httpClient.execute(HttpRequest(GET, "http://www.google.pl/"))

        // then
        asyncHttpClient.close()
        assertNotNull(response)
        assertTrue(response.body != "", "Response body should not be empty")
        assertEquals(200, response.statusCode)
    }
}