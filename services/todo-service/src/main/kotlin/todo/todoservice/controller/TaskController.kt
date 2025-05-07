package todo.todoservice.controller

import io.micrometer.observation.annotation.Observed
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import todo.todoservice.dto.TaskDTO
import todo.todoservice.entity.TaskEntity
import todo.todoservice.service.TaskService
import todo.todoservice.service.ThemeService

@RestController
@RequestMapping("/task")
@Observed(name = "TaskController")
class TaskController(
    private val taskService: TaskService,
    private val themeService: ThemeService,
) {
    @PostMapping("/create")
    fun createTask(@RequestBody task: TaskDTO): ResponseEntity<Boolean> {
        val theme = themeService.findOrCreateTheme(task.theme, task.userId)

        val success = taskService.createTask(TaskEntity(
            title = task.title,
            theme = theme, // Используем существующую или новую тему
            priority = task.priority,
            userId = task.userId,
            reminder = task.reminder,
            deadline = task.deadline,
        ))

        return if (success) ResponseEntity.ok(true)
        else ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false)
    }
    @GetMapping("/get/{userId}")
    fun getTasks(@PathVariable userId: Long): ResponseEntity<List<TaskDTO>> {
        val dtoList = taskService.getAllUserTasks(userId).map {
            TaskDTO(
                id = it.id,
                title = it.title,
                theme = it.theme.name,
                priority = it.priority,
                userId = it.userId,
                reminder = it.reminder,
                deadline = it.deadline,
            )
        }
        return ResponseEntity(dtoList, HttpStatus.OK)
    }

    @PostMapping("/update/{userId}/{id}")
    fun updateTask(
        @PathVariable userId: Long,
        @PathVariable id: Long,
    ) {
        taskService.updateTask(id, userId)
    }

    @GetMapping("/get/{userId}/{id}")
    fun getTaskByIdAndUserId(@PathVariable("userId") userId: Long, @PathVariable("id") id: Long): TaskDTO {
        val it = taskService.getTaskByIdAndUserId(id, userId) ?: return TaskDTO()
        return TaskDTO(
            id = it.id,
            title = it.title,
            theme = it.theme.name,
            priority = it.priority,
            userId = it.userId,
            reminder = it.reminder,
            deadline = it.deadline,
        )
    }
}