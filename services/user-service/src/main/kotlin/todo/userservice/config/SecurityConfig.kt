package todo.userservice.config

import io.micrometer.observation.annotation.Observed
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.RedirectStrategy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler

@Configuration
@EnableWebSecurity
@Observed(name = "SecurityConfig")
class SecurityConfig {

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/register", "/login").permitAll()
                    .anyRequest().authenticated()
            }
            .formLogin { form ->
                form
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/home", true)
                    .permitAll()
            }
            .logout { logout ->
                logout
                    .logoutSuccessUrl("/login")
                    .addLogoutHandler(securityContextLogoutHandler())
            }
            .headers { headers ->
                headers.frameOptions { frameOptions ->
                    frameOptions.sameOrigin()
                }
            }

        return http.build()
    }

    @Bean
    fun simpleUrlAuthenticationSuccessHandler(): SimpleUrlAuthenticationSuccessHandler {
        return SimpleUrlAuthenticationSuccessHandler().apply {
            setRedirectStrategy(customRedirectStrategy())
        }
    }

    @Bean
    fun securityContextLogoutHandler(): SecurityContextLogoutHandler {
        return SecurityContextLogoutHandler().apply {
            isInvalidateHttpSession = true
        }
    }

    @Bean
    fun customRedirectStrategy(): RedirectStrategy {
        return RedirectStrategy { request, response, url ->
            val forwardedHost = request.getHeader("X-Forwarded-Host")
            val forwardedProto = request.getHeader("X-Forwarded-Proto") ?: "http"

            val redirectUrl = if (forwardedHost != null) {
                "$forwardedProto://$forwardedHost$url"
            } else {
                url
            }

            response.setHeader("Location", redirectUrl)
            response.status = HttpServletResponse.SC_FOUND
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}