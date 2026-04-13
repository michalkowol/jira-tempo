package pl.michalkowol

import assertk.assertThat
import assertk.assertions.isNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext

class EdenApplicationIntTest : IntegrationTest() {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Test
    fun `should boot application`() {
        assertThat(applicationContext).isNotNull()
    }
}
