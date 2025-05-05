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
@Observed(name = "UserController")
class UserController(
    private val authService: AuthService,
    private val todoService: TODOService,
    ) {

    @GetMapping("/home")
    fun homePage(
        @AuthenticationPrincipal userDetails: UserDetails,
        model: Model
    ): String {
        val user = authService.findByUsername(userDetails.username)
            ?: return "redirect:/login"

        // Получаем задачи через Feign клиент
        val tasks = todoService.getTasks(user.id).body ?: emptyList()

        model.addAttribute("tasks", tasks)
        model.addAttribute("newTask", TaskDTO()) // Пустой DTO для формы
        return "home"
    }

    @PostMapping("/task/create")
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
}