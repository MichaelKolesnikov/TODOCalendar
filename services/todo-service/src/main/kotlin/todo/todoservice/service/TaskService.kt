package todo.todoservice.service

import io.micrometer.observation.annotation.Observed
import org.springframework.stereotype.Service
import todo.todoservice.entity.TaskEntity
import todo.todoservice.entity.ThemeEntity
import todo.todoservice.repository.TaskRepository
import todo.todoservice.repository.ThemeRepository

@Service
@Observed(name = "TaskService")
class TaskService(
    private val taskRepository: TaskRepository,
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

    fun getAllUserTasks(userId: Long): List<TaskEntity> {
        return taskRepository.findTaskEntitiesByUserId(userId)
    }
}