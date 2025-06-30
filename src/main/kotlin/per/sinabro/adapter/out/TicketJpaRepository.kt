package per.sinabro.adapter.out

import org.springframework.data.jpa.repository.JpaRepository
import per.sinabro.domain.Ticket

interface TicketJpaRepository : JpaRepository<Ticket, Long> {
    fun findByIdAndIsReserved(id: Long, isReserved: Boolean): Ticket?
}
