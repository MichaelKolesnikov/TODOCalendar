package todo.userservice.controller

import io.micrometer.observation.annotation.Observed
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import todo.userservice.dto.TaskDTO
import todo.userservice.service.AuthService
import todo.userservice.service.TODOService

@Controller
@RequestMapping("/task")
@Observed(name = "TaskController")
class TaskController(
    private val authService: AuthService,
    private val todoService: TODOService,
) {
    @GetMapping("/new")
    fun newTask(): String = "/new-task"

    @GetMapping("/edit/{id}")
    fun editTaskForm(
        @PathVariable("id") id: Long,
        model: Model,
        @AuthenticationPrincipal userDetails: UserDetails
    ): String {
        val user = authService.findByUsername(userDetails.username) ?: return "redirect:/login"
        val task = todoService.getTaskByIdAndUserId(user.id, id)
        model.addAttribute("task", task)
        return "edit-task"
    }

    @PostMapping("/update")
    fun updateTask(
        @ModelAttribute taskDTO: TaskDTO,
        bindingResult: BindingResult,
        @AuthenticationPrincipal userDetails: UserDetails
    ): String {
        if (bindingResult.hasErrors()) {
            return "edit-task"
        }
        val user = authService.findByUsername(userDetails.username) ?: return "redirect:/login"
        taskDTO.userId = user.id
        todoService.updateTask(taskDTO)
        return "redirect:/home"
    }

    @PostMapping("/create")
    fun createTask(
        @ModelAttribute("newTask") task: TaskDTO,
        bindingResult: BindingResult,
        @AuthenticationPrincipal userDetails: UserDetails
    ): String {
        if (bindingResult.hasErrors()) {
            return "home" // Вернуться на страницу с ошибками
        }
        val user = authService.findByUsername(userDetails.username) ?: return "redirect:/login"
        task.userId = user.id
        todoService.createTask(task)
        return "redirect:/home"
    }

    @GetMapping("/delete/{id}")
    fun deleteTask(@PathVariable("id") id: Long): String {
        todoService.deleteTask(id)
        return "redirect:/home"
    }
}