package per.sinabro.application.event

import org.springframework.context.event.*
import org.springframework.scheduling.annotation.*
import org.springframework.stereotype.*
import per.sinabro.application.port.out.event.PublishEvent
import per.sinabro.domain.ticket.event.*

@Component
class RegisterTicketEventListener(
    private val publishEvent: PublishEvent,
) {

    @Async
    @EventListener
    fun onApplicationEvent(event: RegisterTicketEvent) {
        publishEvent.publish("TicketCreated", event)
    }
}
