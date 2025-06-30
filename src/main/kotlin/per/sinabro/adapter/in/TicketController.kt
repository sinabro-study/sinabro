package per.sinabro.adapter.`in`

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import per.sinabro.adapter.`in`.request.ReserveTicketRequest
import per.sinabro.port.`in`.usecase.ReserveTicket

@RestController
class TicketController(
    private val reserveTicket: ReserveTicket
) {

    @PostMapping("/tickets/reserve")
    fun reserveTicket(@RequestBody request: ReserveTicketRequest): ResponseEntity<Void> {
        val (userId, ticketId) = request

        reserveTicket.reserve(userId, ticketId)

        return ResponseEntity.ok().build()
    }
}
