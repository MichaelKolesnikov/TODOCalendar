package todo.todoservice.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "Task")
data class TaskEntity(
    @Column(nullable = false)
    val title: String,

    @ManyToOne
    @JoinColumn(name = "theme_id")
    val theme: ThemeEntity,

    @Enumerated(EnumType.STRING)
    val priority: Priority,

    @Column(nullable = false)
    val userId: Long,

    @Column
    val reminder: LocalDateTime? = null,

    @Column(nullable = false)
    val deadline: LocalDateTime,
): AbstractEntity()