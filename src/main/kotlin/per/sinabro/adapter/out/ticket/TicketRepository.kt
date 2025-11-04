package per.sinabro.adapter.out.ticket

import org.springframework.stereotype.Repository
import per.sinabro.domain.ticket.Ticket
import per.sinabro.application.port.out.ticket.FindTicket
import per.sinabro.application.port.out.ticket.LoadTicket

@Repository
class TicketRepository(
    private val ticketJpaRepository: TicketJpaRepository
) : FindTicket, LoadTicket {

    override fun findByIdIsReserved(id: Long, isReservation: Boolean): Ticket? =
        ticketJpaRepository.findByIdAndIsReserved(id, isReservation)

    override fun findByIdIsReservedForPriority(id: Long, isReservation: Boolean): Ticket? =
        ticketJpaRepository.findByIdAndIsReservedForUpdate(id, isReservation)

    override fun loadTicket(ticket: Ticket): Ticket {
        return ticketJpaRepository.save(ticket)
    }
}
