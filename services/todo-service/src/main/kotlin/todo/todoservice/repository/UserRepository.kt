package todo.todoservice.repository

import io.micrometer.observation.annotation.Observed
import org.springframework.data.jpa.repository.JpaRepository
import todo.todoservice.entity.UserEntity

@Observed(name = "UserRepository")
interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findUserEntityById(id: Long): UserEntity?
    fun findByUsername(username: String): UserEntity?
}