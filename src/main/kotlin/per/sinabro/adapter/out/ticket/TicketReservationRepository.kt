package per.sinabro.adapter.out.ticket

import org.springframework.stereotype.Repository
import per.sinabro.domain.ticket.TicketReservation
import per.sinabro.port.out.ticket.LoadTicketReservation

@Repository
class TicketReservationRepository(
    private val ticketReservationJpaRepository: TicketReservationJpaRepository
) : LoadTicketReservation {

    override fun load(ticketReservation: TicketReservation) {
        ticketReservationJpaRepository.save(ticketReservation)
    }
}
