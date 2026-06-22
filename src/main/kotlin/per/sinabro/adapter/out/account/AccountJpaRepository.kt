package per.sinabro.adapter.out.account

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import per.sinabro.domain.account.Account

interface AccountJpaRepository : JpaRepository<Account, Long> {
    fun findAllByBalanceGreaterThanEqual(balance: Long): List<Account>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a")
    fun findAllForUpdate(): List<Account>
}