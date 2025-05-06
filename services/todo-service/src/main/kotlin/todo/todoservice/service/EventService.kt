package todo.todoservice.service

import io.micrometer.observation.annotation.Observed
import org.springframework.stereotype.Service
import todo.todoservice.entity.EventEntity
import todo.todoservice.repository.EventRepository

@Observed(name = "EventService")
@Service
class EventService(
    private val eventRepository: EventRepository
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
}