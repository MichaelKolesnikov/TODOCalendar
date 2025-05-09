package todo.userservice.service

import io.micrometer.observation.annotation.Observed
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import todo.userservice.dto.EventDTO
import todo.userservice.dto.TaskDTO

@Observed(name = "TODOService")
@FeignClient(name = "todo-service") // , fallbackFactory = TodoFallbackFactory::class)
interface TODOService {
    @PostMapping("/task/create")
    fun createTask(@RequestBody task: TaskDTO): ResponseEntity<Boolean>

    @GetMapping("/task/get/{userId}")
    fun getTasks(@PathVariable userId: Long): ResponseEntity<List<TaskDTO>>

    @PostMapping("/task/update")
    fun updateTask(
        @RequestBody taskDTO: TaskDTO,
    )

    @DeleteMapping("/task/{id}")
    fun deleteTask(@PathVariable("id") id: Long)

    @GetMapping("/task/get/{userId}/{id}")
    fun getTaskByIdAndUserId(@PathVariable("userId") userId: Long, @PathVariable("id") id: Long): TaskDTO

    @PostMapping("/event/create")
    fun createEvent(@RequestBody event: EventDTO): ResponseEntity<Boolean>

    @GetMapping("/event/get/{userId}")
    fun getEvents(@PathVariable userId: Long): ResponseEntity<List<EventDTO>>

    @PostMapping("/event/update")
    fun updateEvent(
        @RequestBody event: EventDTO,
    )

    @DeleteMapping("/event/{id}")
    fun deleteEventById(@PathVariable("id") id: Long)

    @GetMapping("/event/get/{userId}/{id}")
    fun getEventByIdAndUserId(@PathVariable("userId") userId: Long, @PathVariable("id") id: Long): EventDTO
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