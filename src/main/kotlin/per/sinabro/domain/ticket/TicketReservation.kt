package per.sinabro.domain.ticket

import jakarta.persistence.*

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
