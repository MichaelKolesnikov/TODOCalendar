package todo.configservice.controller

import io.micrometer.observation.annotation.Observed
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Observed(name = "ExperimentalController")
@RequestMapping("/lol")
class ExperimentalController {
    @GetMapping("/kek")
    fun index(): ResponseEntity<String> {
        return ResponseEntity.ok("OK")
    }
}