package todo.todoservice.service

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import todo.todoservice.dto.UserDTO

@FeignClient(name = "user-service")
interface UserServiceClient {
    @GetMapping("/api/v1/users/{userId}")
    fun getUserById(@PathVariable userId: Long): ResponseEntity<UserDTO>
}