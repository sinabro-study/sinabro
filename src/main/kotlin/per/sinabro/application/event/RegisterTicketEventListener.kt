package per.sinabro.application.event

import org.springframework.context.event.*
import org.springframework.scheduling.annotation.*
import org.springframework.stereotype.*
import per.sinabro.domain.ticket.event.*

@Component
class RegisterTicketEventListener {

    @Async
    @EventListener
    fun onApplicationEvent(event: RegisterTicketEvent) {
        println(event.ticketId)
    }
}