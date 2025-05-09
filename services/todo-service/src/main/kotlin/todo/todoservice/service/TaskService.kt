package todo.todoservice.service

import io.micrometer.observation.annotation.Observed
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import todo.todoservice.dto.TaskDTO
import todo.todoservice.entity.TaskEntity
import todo.todoservice.repository.TaskRepository

@Service
@Observed(name = "TaskService")
class TaskService(
    private val taskRepository: TaskRepository,
    private val themeService: ThemeService,
) {
    fun createTask(task: TaskEntity): Boolean {
        try {
            taskRepository.save(task)
        }
        catch (e: Exception) {
            return false
        }
        return true
    }

    fun deleteTask(id: Long) {
        taskRepository.deleteById(id)
    }

    fun getAllUserTasks(userId: Long): List<TaskEntity> {
        return taskRepository.findTaskEntitiesByUserId(userId)
    }

    fun getTaskByIdAndUserId(id: Long, userId: Long): TaskEntity? {
        return taskRepository.findTaskByIdAndUserId(id, userId)
    }

    fun updateTask(taskDTO: TaskDTO) {
        val theme = themeService.findOrCreateTheme(taskDTO.theme, taskDTO.userId)

        val task = taskRepository.findByIdOrNull(taskDTO.id) ?: throw EntityNotFoundException()
        task.title = taskDTO.title
        task.theme = theme
        task.priority = taskDTO.priority
        task.reminder = taskDTO.reminder
        task.deadline = taskDTO.deadline

        taskRepository.save(task)
    }
}