package pl.michalkowol

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class EdenApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<EdenApplication>(*args)
}

