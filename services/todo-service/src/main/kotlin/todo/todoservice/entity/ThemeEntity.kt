package todo.todoservice.entity

import jakarta.persistence.*

@Entity
@Table(
    name = "Theme",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["name", "user_id"])
    ]
)
data class ThemeEntity(
    @Column(nullable = false)
    val name: String,

    @Column(name = "user_id", nullable = false)
    val userId: Long
) : AbstractEntity()
