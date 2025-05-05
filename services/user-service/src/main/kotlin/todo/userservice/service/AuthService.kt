package todo.userservice.service

import io.micrometer.observation.annotation.Observed
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import todo.userservice.model.UserEntity
import todo.userservice.repository.UserRepository

@Service
@Observed(name = "RegisterService")
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional
    fun registerUser(username: String, password: String): UserEntity {
        val existingUser = userRepository.findByUsername(username)
        if (existingUser != null) {
            throw IllegalArgumentException("Пользователь с именем $username уже существует")
        }
        val encodedPassword = passwordEncoder.encode(password)
        val user = UserEntity(username = username, password = encodedPassword)
        val savedUser = userRepository.save(user)

        return savedUser
    }

    fun findByUsername(username: String): UserEntity? {
        return userRepository.findByUsername(username)
    }
}