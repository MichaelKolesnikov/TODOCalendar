package todo.todoservice.repository

import io.micrometer.observation.annotation.Observed
import org.springframework.data.jpa.repository.JpaRepository
import todo.todoservice.entity.ThemeEntity

@Observed(name = "ThemeRepository")
interface ThemeRepository: JpaRepository<ThemeEntity, Long> {
    fun findByNameAndUserId(name: String, userId: Long): ThemeEntity?
}