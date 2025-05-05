package todo.userservice.controller

import io.micrometer.observation.annotation.Observed
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import todo.userservice.service.AuthService

@Controller
@Observed(name = "UserController")
class UserController(
    private val authService: AuthService,
) {
    @GetMapping("/home")
    fun homePage(
        @AuthenticationPrincipal userDetails: UserDetails,
        model: Model
    ): String {
        val user = authService.findByUsername(userDetails.username)
            ?: return "redirect:/login"

        return "home"
    }
}