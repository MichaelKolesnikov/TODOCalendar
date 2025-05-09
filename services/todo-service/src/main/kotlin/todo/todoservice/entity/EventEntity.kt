package todo.todoservice.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "Event")
data class EventEntity(
    @Column(nullable = false)
    var title: String,

    @ManyToOne
    @JoinColumn(name = "theme_id")
    var theme: ThemeEntity,

    @Column(nullable = false)
    var priority: Long,

    @Column(nullable = false)
    val userId: Long,

    @Column
    var reminder: LocalDateTime? = null,

    @Column(nullable = false)
    var startTime: LocalDateTime? = null,

    @Column(nullable = false)
    var endTime: LocalDateTime? = null,
) : AbstractEntity()