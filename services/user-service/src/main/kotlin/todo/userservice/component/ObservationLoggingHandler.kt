package todo.userservice.component

import io.micrometer.observation.Observation
import io.micrometer.observation.ObservationHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ObservationLoggingHandler : ObservationHandler<Observation.Context> {
    private val logger = LoggerFactory.getLogger(ObservationLoggingHandler::class.java)

    override fun onStart(context: Observation.Context) {
        logger.info("Observing {} with context {}", context.name, context)
    }

    override fun supportsContext(context: Observation.Context): Boolean {
        return true
    }
}