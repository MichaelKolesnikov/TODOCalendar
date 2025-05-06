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
        return themeRepository.findByNameAndUserId(themeName, userId)
            ?: themeRepository.save(ThemeEntity(name = themeName, userId = userId))
    }
}