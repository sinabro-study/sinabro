package per.sinabro.adapter.out

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import per.sinabro.domain.Ticket

interface TicketJpaRepository : JpaRepository<Ticket, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ticket FROM Ticket ticket WHERE ticket.id = :id and isReserved = :isReserved")
    fun findByIdAndIsReserved(id: Long, isReserved: Boolean): Ticket?
}
