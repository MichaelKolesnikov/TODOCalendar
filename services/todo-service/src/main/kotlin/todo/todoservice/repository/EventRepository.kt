package todo.todoservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import todo.todoservice.entity.EventEntity

interface EventRepository: JpaRepository<EventEntity, Long> {
    fun findAllByUserId(userId: Long): List<EventEntity>
}