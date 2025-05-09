package todo.todoservice.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "Task")
data class TaskEntity(
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
    var deadline: LocalDateTime,
): AbstractEntity()