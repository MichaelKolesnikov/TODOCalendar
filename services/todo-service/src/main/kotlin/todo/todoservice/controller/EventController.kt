package todo.todoservice.controller

import io.micrometer.observation.annotation.Observed
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import todo.todoservice.dto.EventDTO
import todo.todoservice.dto.TaskDTO
import todo.todoservice.entity.EventEntity
import todo.todoservice.service.EventService
import todo.todoservice.service.ThemeService

@Observed(name = "EventController")
@RestController
@RequestMapping("/event")
class EventController(
    private val themeService: ThemeService,
    private val eventService: EventService,
) {
    @GetMapping("/hello")
    fun hello(): ResponseEntity<String> {
        return ResponseEntity("Hello, World", HttpStatus.OK)
    }

    @PostMapping("/create")
    fun createEvent(@RequestBody eventDTO: EventDTO): ResponseEntity<Boolean> {
        val theme = themeService.findOrCreateTheme(eventDTO.theme, eventDTO.userId)

        val success = eventService.createEvent(
            EventEntity(
                title = eventDTO.title,
                theme = theme,
                priority = eventDTO.priority,
                userId = eventDTO.userId,
                reminder = eventDTO.reminder,
                startTime = eventDTO.startTime,
                endTime = eventDTO.endTime
            )
        )

        return if (success) ResponseEntity.ok(true)
        else ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false)
    }
    @GetMapping("/get/{userId}")
    fun getEvents(@PathVariable userId: Long): ResponseEntity<List<EventDTO>> {
        val dtoList = eventService.getAllUserEvents(userId).map {
            EventDTO(
                id = it.id,
                title = it.title,
                theme = it.theme.name,
                priority = it.priority,
                userId = it.userId,
                reminder = it.reminder,
                startTime = it.startTime,
                endTime = it.endTime
            )
        }
        return ResponseEntity(dtoList, HttpStatus.OK)
    }

    @PostMapping("/update/{userId}/{id}")
    fun updateEvent(
        @PathVariable userId: Long,
        @PathVariable id: Long,
    ) {
        eventService.updateEvent(id, userId)
    }

    @GetMapping("/get/{userId}/{id}")
    fun getEventByIdAndUserId(@PathVariable("userId") userId: Long, @PathVariable("id") id: Long): EventDTO {
        val it = eventService.getEventByIdAndUserId(id, userId) ?: return EventDTO()
        return EventDTO(
            id = it.id,
            title = it.title,
            theme = it.theme.name,
            priority = it.priority,
            userId = it.userId,
            reminder = it.reminder,
            startTime = it.startTime,
            endTime = it.endTime
        )
    }
}