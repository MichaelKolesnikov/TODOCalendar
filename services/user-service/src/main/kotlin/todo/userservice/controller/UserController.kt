package todo.userservice.controller

import io.micrometer.observation.annotation.Observed
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import todo.userservice.dto.EventDTO
import todo.userservice.dto.TaskDTO
import todo.userservice.service.AuthService
import todo.userservice.service.TODOService
import java.time.LocalDate
import kotlin.math.max

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

        val tasks = todoService.getTasks(user.id).body
            ?.sortedBy { it.deadline } ?: emptyList()

        val events = todoService.getEvents(user.id).body
            ?.sortedBy { it.startTime } ?: emptyList()

        model.addAllAttributes(mapOf(
            "tasks" to tasks,
            "events" to events,
            "newTask" to TaskDTO(),
            "newEvent" to EventDTO(),
        ))
        return "home"
    }

    @GetMapping("/new-task")
    fun newTask(): String = "new-task"

    @GetMapping("/new-event")
    fun newEvent(): String = "new-event"


    @GetMapping("/task/edit/{id}")
    fun editTaskForm(
        @PathVariable("id") id: Long,
        model: Model,
        @AuthenticationPrincipal userDetails: UserDetails
    ): String {
        val user = authService.findByUsername(userDetails.username) ?: return "redirect:/login"
        val task = todoService.getTaskByIdAndUserId(user.id, id) ?: return "redirect:/home"
        model.addAttribute("task", task)
        return "edit-task"
    }

    @PostMapping("/task/update/{id}")
    fun updateTask(
        @PathVariable("id") id: Long,
        @ModelAttribute taskDTO: TaskDTO,
        bindingResult: BindingResult,
        @AuthenticationPrincipal userDetails: UserDetails
    ): String {
        if (bindingResult.hasErrors()) {
            return "edit-task"
        }
        val user = authService.findByUsername(userDetails.username) ?: return "redirect:/login"
        todoService.updateTask(id, user.id)
        return "redirect:/home"
    }

    @GetMapping("/event/edit/{id}")
    fun editEventForm(
        @PathVariable("id") id: Long,
        model: Model,
        @AuthenticationPrincipal userDetails: UserDetails
    ): String {
        val user = authService.findByUsername(userDetails.username) ?: return "redirect:/login"
        val event = todoService.getEventByIdAndUserId(user.id, id) ?: return "redirect:/home"
        model.addAttribute("event", event)
        return "edit-event"
    }

    @PostMapping("/event/update/{id}")
    fun updateEvent(
        @PathVariable("id") id: Long,
        @ModelAttribute eventDTO: EventDTO,
        bindingResult: BindingResult,
        @AuthenticationPrincipal userDetails: UserDetails
    ): String {
        if (bindingResult.hasErrors()) {
            return "edit-event"
        }
        val user = authService.findByUsername(userDetails.username) ?: return "redirect:/login"
        todoService.updateEvent(id, user.id)
        return "redirect:/home"
    }



    @GetMapping("/calendar")
    fun calendarPage(
        @AuthenticationPrincipal userDetails: UserDetails,
        model: Model
    ): String {
        val user = authService.findByUsername(userDetails.username)
            ?: return "redirect:/login"

        val tasks = todoService.getTasks(user.id).body ?: emptyList()
        val events = todoService.getEvents(user.id).body ?: emptyList()

        val datePriorityMap = mutableMapOf<String, Long>().withDefault { 0 }

        tasks.forEach { task ->
            val date = task.deadline.toLocalDate().toString()
            datePriorityMap[date] = max(datePriorityMap.getValue(date), task.priority)
        }

        events.forEach { event ->
            var date = event.startTime!!.toLocalDate()
            val endDate = event.endTime!!.toLocalDate()
            while (!date.isAfter(endDate)) {
                val dateStr = date.toString()
                datePriorityMap[dateStr] = max(datePriorityMap.getValue(dateStr), event.priority)
                date = date.plusDays(1)
            }
        }

        model.addAttribute("datePriorities", datePriorityMap)
        model.addAttribute("currentDate", LocalDate.now())
        return "calendar"
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

    @PostMapping("/event/create")
    fun createEvent(
        @ModelAttribute("newEvent") eventDTO: EventDTO,
        bindingResult: BindingResult,
        @AuthenticationPrincipal userDetails: UserDetails
    ): String {
        if (bindingResult.hasErrors()) {
            return "home" // Вернуться на страницу с ошибками
        }
        val user = authService.findByUsername(userDetails.username) ?: return "redirect:/login"
        eventDTO.userId = user.id
        todoService.createEvent(eventDTO)
        return "redirect:/home"
    }
}