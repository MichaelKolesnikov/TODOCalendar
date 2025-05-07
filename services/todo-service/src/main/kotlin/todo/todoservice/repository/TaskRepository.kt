package todo.todoservice.repository

import io.micrometer.observation.annotation.Observed
import org.springframework.data.jpa.repository.JpaRepository
import todo.todoservice.entity.TaskEntity

@Observed(name = "TaskRepository")
interface TaskRepository: JpaRepository<TaskEntity, Long> {
    fun getTaskById(id: Long): TaskEntity?

    fun findTaskEntitiesByUserId(userId: Long): List<TaskEntity>

    fun findTaskByIdAndUserId(id: Long, userId: Long): TaskEntity?
}