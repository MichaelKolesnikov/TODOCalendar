package todo.todoservice.service

import io.micrometer.observation.annotation.Observed
import org.springframework.stereotype.Service
import todo.todoservice.entity.ThemeEntity
import todo.todoservice.repository.ThemeRepository

@Observed(name = "ThemeService")
@Service
class ThemeService(
    private val themeRepository: ThemeRepository
) {
    fun findOrCreateTheme(themeName: String, userId: Long): ThemeEntity {
        val existingTheme = themeRepository.findByNameAndUserId(themeName, userId)
        if (existingTheme != null) {
            return existingTheme
        }

        val newTheme = ThemeEntity(
            name = themeName,
            userId = userId
        )
        return themeRepository.save(newTheme)
    }
}