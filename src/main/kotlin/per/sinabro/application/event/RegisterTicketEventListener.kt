package per.sinabro.application.event

import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import per.sinabro.application.port.out.event.PublishEvent
import per.sinabro.domain.ticket.event.RegisterTicketEvent

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
