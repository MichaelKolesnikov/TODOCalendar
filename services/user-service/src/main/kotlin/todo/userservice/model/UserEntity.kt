package todo.userservice.model

import jakarta.persistence.*

@Entity
@Table(name = "app_user")
data class UserEntity (
    @Column(nullable = false)
    val username: String,
    @Column(nullable = false)
    val password: String,
) : AbstractEntity()
