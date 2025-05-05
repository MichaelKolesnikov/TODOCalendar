package todo.todoservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import todo.todoservice.entity.ThemeEntity

interface ThemeRepository: JpaRepository<ThemeEntity, Long> {
    fun findByNameAndUserId(themeName: String, userId: Long): ThemeEntity?
}