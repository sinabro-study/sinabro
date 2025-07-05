package per.sinabro.adapter.out

import org.springframework.stereotype.Repository
import per.sinabro.domain.Ticket
import per.sinabro.port.out.FindTicket
import per.sinabro.port.out.LoadTicket

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
