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
}