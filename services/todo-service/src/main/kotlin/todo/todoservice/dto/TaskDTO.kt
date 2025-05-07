package todo.todoservice.dto

import java.time.LocalDateTime

data class TaskDTO(
    val id: Long = 0,
    val title: String = "",
    val theme: String = "",
    val priority: Long = 3,
    var userId: Long = 0,
    val reminder: LocalDateTime? = null,
    val deadline: LocalDateTime = LocalDateTime.now().plusDays(1)
)
