package per.sinabro.domain.ticket

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    var id: Long? = null

    @Column(length = 1, nullable = false)
    private var isReserved: Boolean = false

    fun reserve() {
        this.isReserved = true
    }
}
