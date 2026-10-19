package per.sinabro.adapter.out.ticket

import org.springframework.stereotype.Repository
import per.sinabro.application.port.out.ticket.LoadTicketReservation
import per.sinabro.domain.ticket.TicketReservation

@Repository
class TicketReservationRepository(
    private val ticketReservationJpaRepository: TicketReservationJpaRepository
) : LoadTicketReservation {

    override fun load(ticketReservation: TicketReservation) {
        ticketReservationJpaRepository.save(ticketReservation)
    }
}
