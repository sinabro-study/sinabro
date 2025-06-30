package per.sinabro.adapter.out

import org.springframework.stereotype.Repository
import per.sinabro.domain.TicketReservation
import per.sinabro.port.out.LoadTicketReservation

@Repository
class TicketReservationRepository(
    private val ticketReservationJpaRepository: TicketReservationJpaRepository
) : LoadTicketReservation {

    override fun load(ticketReservation: TicketReservation) {
        ticketReservationJpaRepository.save(ticketReservation)
    }
}
