package per.sinabro.domain.ticket

import jakarta.persistence.*

@Entity
class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(length = 1, nullable = false)
    private var isReserved: Boolean = false

    fun reserve() {
        this.isReserved = true
    }
}
