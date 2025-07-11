package per.sinabro.adapter.`in`.ticket

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import per.sinabro.adapter.`in`.ticket.request.ReserveTicketRequest
import per.sinabro.port.`in`.ticket.usecase.CreateTicket
import per.sinabro.port.`in`.ticket.usecase.ReserveTicket

@RestController
class TicketController(
    private val reserveTicket: ReserveTicket,
    private val createTicket: CreateTicket
) {

    @PostMapping("/tickets")
    fun createTicket() {
        createTicket.create()
    }

    @PostMapping("/tickets/reserve")
    fun reserveTicket(@RequestBody request: ReserveTicketRequest): ResponseEntity<Void> {
        val (userId, ticketId) = request

        reserveTicket.reserve(userId, ticketId)

        return ResponseEntity.ok().build()
    }
}
