package todo.todoservice.service

import io.micrometer.observation.annotation.Observed
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import todo.todoservice.entity.EventEntity
import todo.todoservice.entity.TaskEntity
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

    fun updateEvent(id: Long, userId: Long) {
        val event = eventRepository.findEventEntityByIdAndUserId(id, userId)
            ?: throw EntityNotFoundException("Task not found")

        eventRepository.save(event)
    }

    fun getEventByIdAndUserId(id: Long, userId: Long): EventEntity? {
        return eventRepository.findEventEntityByIdAndUserId(id, userId)
    }
}