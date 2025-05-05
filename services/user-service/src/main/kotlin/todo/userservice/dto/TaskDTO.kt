package todo.userservice.dto

import java.time.LocalDateTime

data class TaskDTO(
    val title: String,
//    val theme: ThemeEntityDTO,
//    val priority: PriorityDTO,
    val userId: Long,
    val reminder: LocalDateTime? = null,
    val deadline: LocalDateTime,
)
