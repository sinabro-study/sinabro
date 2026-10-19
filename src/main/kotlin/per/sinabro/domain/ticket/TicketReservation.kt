package per.sinabro.domain.ticket

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class TicketReservation(
    val userId: Long,

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    val ticket: Ticket
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = null
}
