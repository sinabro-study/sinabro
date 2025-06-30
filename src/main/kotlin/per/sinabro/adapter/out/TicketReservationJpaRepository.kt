package per.sinabro.adapter.out

import org.springframework.data.jpa.repository.JpaRepository
import per.sinabro.domain.TicketReservation

interface TicketReservationJpaRepository : JpaRepository<TicketReservation, Long> {
}
