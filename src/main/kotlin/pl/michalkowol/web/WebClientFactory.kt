package pl.michalkowol.web

import kotlin.reflect.KClass
import org.springframework.http.client.JdkClientHttpRequestFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Component
class WebClientFactory(private val builder: RestClient.Builder) {

    final inline fun <reified S : Any> createClient(baseUrl: String): S {
        return createClient(baseUrl, S::class)
    }

    fun <S : Any> createClient(baseUrl: String, serviceType: KClass<S>): S {
        val jdkRequestFactory = JdkClientHttpRequestFactory()
        val restClient = builder
            .baseUrl(baseUrl)
            .requestFactory(jdkRequestFactory)
            .build()
        val adapter = RestClientAdapter.create(restClient)
        val factory = HttpServiceProxyFactory.builderFor(adapter).build()
        return factory.createClient(serviceType.java)
    }
}
