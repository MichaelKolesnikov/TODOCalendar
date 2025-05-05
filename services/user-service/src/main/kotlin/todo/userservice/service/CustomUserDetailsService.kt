package todo.userservice.service

import io.micrometer.observation.annotation.Observed
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import todo.userservice.repository.UserRepository

@Service
@Observed(name = "CustomUserDetailsService")
class CustomUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("Пользователь не найден: $username")
        return org.springframework.security.core.userdetails.User(
            user.username,
            user.password,
            emptyList()
        )
    }
}