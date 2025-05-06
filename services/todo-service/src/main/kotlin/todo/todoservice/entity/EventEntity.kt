package todo.todoservice.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "Event")
data class EventEntity(
    @Column(nullable = false)
    val title: String,

    @ManyToOne
    @JoinColumn(name = "theme_id")
    val theme: ThemeEntity,

    @Column(nullable = false)
    val priority: Long,

    @Column(nullable = false)
    val userId: Long,

    @Column
    val reminder: LocalDateTime? = null,

    @Column(nullable = false)
    val startTime: LocalDateTime? = null,

    @Column(nullable = false)
    val endTime: LocalDateTime? = null,
) : AbstractEntity()