package todo.userservice.dto

import java.time.LocalDateTime

data class EventDTO(
    val id: Long = 0,
    val title: String = "",
    val theme: String = "",
    val priority: Long = 3,
    var userId: Long = 0,
    val reminder: LocalDateTime? = null,
    val startTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null,
)
