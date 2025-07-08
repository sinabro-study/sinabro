package per.sinabro.adapter.out.ticket

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import per.sinabro.domain.ticket.Ticket

interface TicketJpaRepository : JpaRepository<Ticket, Long> {
    fun findByIdAndIsReserved(id: Long, isReserved: Boolean): Ticket?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ticket FROM Ticket ticket WHERE ticket.id = :id and isReserved = :isReserved")
    fun findByIdAndIsReservedForUpdate(id: Long, isReserved: Boolean): Ticket?
}
