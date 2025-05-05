package todo.todoservice.entity

import jakarta.persistence.*

@Entity
@Table(name = "Theme")
data class ThemeEntity(
    @Column(nullable = false, unique = true)
    val name: String,

    @Column(nullable = false)
    val userId: Long
) : AbstractEntity()
