package per.sinabro.adapter.out.ticket

import org.springframework.data.jpa.repository.JpaRepository
import per.sinabro.domain.ticket.TicketReservation

interface TicketReservationJpaRepository : JpaRepository<TicketReservation, Long> {
}
