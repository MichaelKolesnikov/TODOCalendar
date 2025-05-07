package todo.todoservice.service

import io.micrometer.observation.annotation.Observed
import jakarta.persistence.EntityNotFoundException
import org.springframework.scheduling.config.Task
import org.springframework.stereotype.Service
import todo.todoservice.entity.TaskEntity
import todo.todoservice.repository.TaskRepository

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

    fun getTaskByIdAndUserId(id: Long, userId: Long): TaskEntity? {
        return taskRepository.findTaskByIdAndUserId(id, userId)
    }

    fun updateTask(id: Long, userId: Long) {
        val task = taskRepository.findTaskByIdAndUserId(id, userId)
            ?: throw EntityNotFoundException("Task not found")

        taskRepository.save(task)
    }
}