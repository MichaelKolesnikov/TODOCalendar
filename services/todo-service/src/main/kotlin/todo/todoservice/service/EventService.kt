package todo.todoservice.service

import io.micrometer.observation.annotation.Observed
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import todo.todoservice.dto.EventDTO
import todo.todoservice.entity.EventEntity
import todo.todoservice.repository.EventRepository

@Observed(name = "EventService")
@Service
class EventService(
    private val eventRepository: EventRepository,
    private val themeService: ThemeService
) {
    fun createEvent(eventEntity: EventEntity) : Boolean {
        try {
            eventRepository.save(eventEntity)
        }
        catch (e: Exception) {
            return false
        }
        return true
    }

    fun getAllUserEvents(userId: Long): List<EventEntity> {
        return eventRepository.findAllByUserId(userId)
    }

    fun updateEvent(eventDTO: EventDTO) {
        val theme = themeService.findOrCreateTheme(eventDTO.theme, eventDTO.userId)

        val event = eventRepository.findByIdOrNull(eventDTO.id) ?: throw EntityNotFoundException()
        event.title = eventDTO.title
        event.theme = theme
        event.priority = eventDTO.priority
        event.reminder = eventDTO.reminder
        event.startTime = eventDTO.startTime
        event.endTime = eventDTO.endTime

        eventRepository.save(event)
    }

    fun getEventByIdAndUserId(id: Long, userId: Long): EventEntity? {
        return eventRepository.findEventEntityByIdAndUserId(id, userId)
    }

    fun deleteEvent(id: Long) {
        eventRepository.deleteById(id)
    }
}