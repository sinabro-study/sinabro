package per.sinabro.port.`in`.service

import org.springframework.stereotype.Service
import per.sinabro.domain.Ticket
import per.sinabro.domain.TicketReservation
import per.sinabro.port.`in`.usecase.CreateTicket
import per.sinabro.port.`in`.usecase.ReserveTicket
import per.sinabro.port.out.FindTicket
import per.sinabro.port.out.LoadTicket
import per.sinabro.port.out.LoadTicketReservation

@Service
class TicketService(
    private val loadTicketReservation: LoadTicketReservation,
    private val findTicket: FindTicket,
    private val loadTicket: LoadTicket
) : ReserveTicket, CreateTicket {

    override fun reserve(userId: Long, ticketId: Long): Long {
        val ticket = findTicket.findByIdIsReserved(ticketId, false) ?: throw RuntimeException("Not found ticket.")

        ticket.reserve()
        loadTicketReservation.load(TicketReservation(userId, ticket))

        return 0
    }

    override fun create() {
        loadTicket.loadTicket(Ticket())
    }
}
