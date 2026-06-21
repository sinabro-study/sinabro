package per.sinabro.application.port.service

import jakarta.persistence.EntityManager
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import per.sinabro.application.port.`in`.account.usecase.CheckAccountUsecase
import per.sinabro.application.port.`in`.account.usecase.RegisterAccountUsecase
import per.sinabro.application.port.`in`.account.usecase.WithdrawAccountUsecase
import per.sinabro.application.port.out.account.FindAccount
import per.sinabro.application.port.out.account.LoadAccount
import per.sinabro.domain.account.Account

@Service
class AccountApplicationService(
    private val loadAccount: LoadAccount,
    private val findAccount: FindAccount,
    private val entityManager: EntityManager,
) : WithdrawAccountUsecase, RegisterAccountUsecase, CheckAccountUsecase {

    @Transactional
    override fun withdraw(id: Long) {
        val account = findAccount.findAccount(id) ?: throw RuntimeException("Account not found")

        println("[Before Update] Account Balance: ${account.balance}")
        account.withdraw()
        entityManager.flush()
        println("[After Update] Account Balance: ${account.balance}")

        Thread.sleep(1_000)
        println("[Commit Update] Account Balance: ${account.balance}")
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    override fun checkReadUncommited(id: Long): Long {
        val account = findAccount.findAccount(id) ?: throw RuntimeException("Account not found")
        println("[Read Uncommitted] Account Balance: ${account.balance}")
        return account.balance
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    override fun checkReadCommited(id: Long): List<Long> {
        val account1 = findAccount.findAccount(id) ?: throw RuntimeException("Account not found")
        entityManager.clear()
        println("[Read Committed] Account1 Balance: ${account1.balance}")

        Thread.sleep(5_000)

        val account2 = findAccount.findAccount(id) ?: throw RuntimeException("Account not found")
        entityManager.clear()
        println("[Read Committed] Account2 Balance: ${account2.balance}")

        return listOf(account1.balance, account2.balance)
    }

    @Transactional
    override fun register(): Long {
        return loadAccount.loadAccount(Account())
    }
}
