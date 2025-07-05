package per.sinabro.port.`in`.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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

    @Transactional
    override fun reserve(userId: Long, ticketId: Long): Long {
        val ticket = findTicket.findByIdIsReserved(ticketId, false) ?: throw RuntimeException("Not found ticket.")

        ticket.reserve()
        loadTicketReservation.load(TicketReservation(userId, ticket))

        return 0
    }

    @Transactional
    override fun reservePriority(userId: Long, ticketId: Long): Long {
        val ticket = findTicket.findByIdIsReservedForPriority(ticketId, false) ?: throw RuntimeException("Not found ticket.")

        ticket.reserve()
        loadTicketReservation.load(TicketReservation(userId, ticket))

        return 0
    }

    @Transactional
    override fun create(): Long {
        return loadTicket.loadTicket(Ticket()).id ?: 0
    }
}
