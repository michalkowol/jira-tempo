package pl.michalkowol.mcp

import org.springframework.ai.tool.ToolCallback
import org.springframework.ai.tool.ToolCallbackProvider
import org.springframework.ai.tool.function.FunctionToolCallback
import org.springframework.ai.tool.method.MethodToolCallbackProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.michalkowol.nip.NipService


@Configuration(proxyBeanMethods = false)
class ToolCallbackConfiguration {

    @Bean
    fun nipServiceMcpTool(nipService: NipService): ToolCallbackProvider {
        return MethodToolCallbackProvider.builder()
            .toolObjects(nipService)
            .build()
    }


    @JvmRecord
    data class TextInput(val input: String)

    @Bean
    fun toUpperCase(): ToolCallback {
        return FunctionToolCallback.builder("toUpperCase") { input: TextInput -> input.input.uppercase() }
            .inputType(TextInput::class.java)
            .description("Put the text to upper case")
            .build()
    }
}
