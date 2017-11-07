package pl.michalkowol.configurations

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.softwareberg.HttpClient
import com.softwareberg.JsonMapper
import com.softwareberg.SimpleHttpClient
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import pl.michalkowol.jira.TaskCsvFactory
import pl.michalkowol.jira.Tempo
import pl.michalkowol.jira.TempoClient
import pl.michalkowol.jira.TempoController
import pl.michalkowol.web.HttpServer
import pl.michalkowol.web.StaticFilesController
import pl.michalkowol.web.errors.ErrorsController
import javax.inject.Singleton

class CommonModule : AbstractModule() {

    override fun configure() {}

    @Singleton
    @Provides
    private fun provideConfig(): Config {
        return ConfigFactory.load()
    }

    @Singleton
    @Provides
    private fun provideServerConfiguration(config: Config): ServerConfiguration {
        val port = config.getInt("server.port")
        return ServerConfiguration(port)
    }

    @Singleton
    @Provides
    private fun provideJsonMapper(): JsonMapper {
        return JsonMapper.create()
    }

    @Singleton
    @Provides
    private fun provideErrorsController(jsonMapper: JsonMapper): ErrorsController {
        return ErrorsController(jsonMapper)
    }

    @Singleton
    @Provides
    private fun provideStaticFilesController(): StaticFilesController {
        return StaticFilesController()
    }

    @Singleton
    @Provides
    private fun provideTaskCsvFactory(): TaskCsvFactory {
        return TaskCsvFactory()
    }

    @Singleton
    @Provides
    private fun provideHttpClient(): HttpClient {
        return SimpleHttpClient.create()
    }

    @Singleton
    @Provides
    private fun provideTempoClient(httpClient: HttpClient): TempoClient {
        return TempoClient(httpClient)
    }

    @Singleton
    @Provides
    private fun provideTempo(tempoClient: TempoClient): Tempo {
        return Tempo(tempoClient)
    }

    @Singleton
    @Provides
    private fun provideTempoController(
        taskCsvFactory: TaskCsvFactory,
        tempo: Tempo
    ): TempoController {
        return TempoController(taskCsvFactory, tempo)
    }

    @Singleton
    @Provides
    private fun provideHttpServer(
        serverConfiguration: ServerConfiguration,
        errorsController: ErrorsController,
        staticFilesController: StaticFilesController,
        tempoController: TempoController
    ): HttpServer {
        return HttpServer(
            serverConfiguration,
            errorsController,
            staticFilesController,
            tempoController
        )
    }

}
