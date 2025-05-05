package todo.userservice.repository

import io.micrometer.observation.annotation.Observed
import org.springframework.data.jpa.repository.JpaRepository
import todo.userservice.model.UserEntity

@Observed(name = "UserRepository")
interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByUsername(username: String): UserEntity?
}