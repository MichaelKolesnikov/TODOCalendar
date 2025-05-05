package todo.todoservice.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@MappedSuperclass
abstract class AbstractEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
) {
    @Column(name = "created_at")
    @CreationTimestamp
    val createdAt: LocalDateTime = LocalDateTime.now()
}