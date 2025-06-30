package per.sinabro.adapter.out

import org.springframework.stereotype.Repository
import per.sinabro.domain.Ticket
import per.sinabro.port.out.FindTicket

@Repository
class TicketRepository(
    private val ticketJpaRepository: TicketJpaRepository
) : FindTicket {

    override fun findByIdIsReserved(id: Long, isReservation: Boolean): Ticket? = ticketJpaRepository.findByIdAndIsReserved(id, isReservation)
}
