package todo.todoservice.service

import io.micrometer.observation.annotation.Observed
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import todo.todoservice.dto.UserDTO

@Observed(name = "UserServiceClient")
@FeignClient(name = "user-service")
interface UserServiceClient {
    @GetMapping("/api/v1/users/{userId}")
    fun getUserById(@PathVariable userId: Long): ResponseEntity<UserDTO>
}