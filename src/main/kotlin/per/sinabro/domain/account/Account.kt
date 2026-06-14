package per.sinabro.domain.account

import jakarta.annotation.Generated
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

private const val INIT_BALANCE = 1_000L

@Entity
class Account(
    @Column(name = "balance", nullable = false)
    var balance: Long = INIT_BALANCE,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun withdraw() {
        if (this.balance < 0) {
            throw RuntimeException("Not enough balance")
        }
        this.balance -= 500
    }
}