package todo.userservice.controller

import io.micrometer.observation.annotation.Observed
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import todo.userservice.dto.EventDTO
import todo.userservice.service.AuthService
import todo.userservice.service.TODOService

@Controller
@RequestMapping("/event")
@Observed(name = "EventController")
class EventController(
    private val authService: AuthService,
    private val todoService: TODOService
) {
    @GetMapping("/new")
    fun newEvent(): String = "/new-event"

    @GetMapping("/edit/{id}")
    fun editEventForm(
        @PathVariable("id") id: Long,
        model: Model,
        @AuthenticationPrincipal userDetails: UserDetails
    ): String {
        val user = authService.findByUsername(userDetails.username) ?: return "redirect:/login"
        val event = todoService.getEventByIdAndUserId(user.id, id)
        model.addAttribute("event", event)
        return "edit-event"
    }

    @PostMapping("/update")
    fun updateEvent(
        @ModelAttribute eventDTO: EventDTO,
        bindingResult: BindingResult,
        @AuthenticationPrincipal userDetails: UserDetails
    ): String {
        if (bindingResult.hasErrors()) {
            return "edit-event"
        }
        val user = authService.findByUsername(userDetails.username) ?: return "redirect:/login"
        eventDTO.userId = user.id
        todoService.updateEvent(eventDTO)
        return "redirect:/home"
    }

    @PostMapping("/create")
    fun createEvent(
        @ModelAttribute("newEvent") eventDTO: EventDTO,
        bindingResult: BindingResult,
        @AuthenticationPrincipal userDetails: UserDetails
    ): String {
        if (bindingResult.hasErrors()) {
            return "home"
        }
        val user = authService.findByUsername(userDetails.username) ?: return "redirect:/login"
        eventDTO.userId = user.id
        todoService.createEvent(eventDTO)
        return "redirect:/home"
    }

    @GetMapping("/delete/{id}")
    fun deleteEventById(@PathVariable("id") id: Long): String {
        todoService.deleteEventById(id)
        return "redirect:/home"
    }
}