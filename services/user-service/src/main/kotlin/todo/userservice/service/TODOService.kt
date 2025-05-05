package todo.userservice.service

import io.micrometer.observation.annotation.Observed
import org.springframework.cloud.openfeign.FeignClient

@Observed(name = "TODOService")
@FeignClient(name = "todo-service")
interface TODOService {

}