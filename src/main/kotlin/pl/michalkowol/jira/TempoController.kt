package pl.michalkowol.jira

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class TempoController(
    private val taskCsvFactory: TaskCsvFactory,
    private val tempo: Tempo
) {

    @PostMapping("/worklogFromForm")
    fun worklogFromForm(@RequestParam cookie: String, @RequestParam tasks: String): ResponseEntity<Void> {
        val tasksList = taskCsvFactory.fromCsv(tasks)
        tempo.logTasks(tasksList, cookie)
        return ResponseEntity.noContent().build()
    }
}
