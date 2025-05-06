package todo.userservice.service

import io.micrometer.observation.annotation.Observed
import org.springframework.cloud.openfeign.FallbackFactory
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import todo.userservice.dto.EventDTO
import todo.userservice.dto.TaskDTO

@Observed(name = "TODOService")
@FeignClient(name = "todo-service") // , fallbackFactory = TodoFallbackFactory::class)
interface TODOService {
    @PostMapping("/task/create")
    fun createTask(@RequestBody task: TaskDTO): ResponseEntity<Boolean>

    @GetMapping("/task/get/{userId}")
    fun getTasks(@PathVariable userId: Long): ResponseEntity<List<TaskDTO>>

    @PostMapping("/event/create")
    fun createEvent(@RequestBody event: EventDTO): ResponseEntity<Boolean>

    @GetMapping("/event/get/{userId}")
    fun getEvents(@PathVariable userId: Long): ResponseEntity<List<EventDTO>>
}

//@Component
//class TodoFallbackFactory : FallbackFactory<TODOService> {
//    override fun create(cause: Throwable): TODOService = object : TODOService {
//        override fun createTask(task: TaskDTO): ResponseEntity<Boolean> {
//            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(false)
//        }
//
//        override fun getTasks(userId: Long): ResponseEntity<List<TaskDTO>> {
//            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(emptyList())
//        }
//    }
//}