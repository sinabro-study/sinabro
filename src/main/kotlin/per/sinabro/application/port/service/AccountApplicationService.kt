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
    override fun withdraw() {
        val account = findAccount.findAccount(1) ?: throw RuntimeException("Account not found")

        println("[Before Update] Account Balance: ${account.balance}")
        account.withdraw()
        entityManager.flush()
        println("[After Update] Account Balance: ${account.balance}")

        Thread.sleep(1_000)
        println("[Commit Update] Account Balance: ${account.balance}")
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    override fun checkReadUncommited() {
        val account = findAccount.findAccount(1) ?: throw RuntimeException("Account not found")
        println("[Check Update] Account Balance: ${account.balance}")
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    override fun checkReadCommited() {
        val account1 = findAccount.findAccount(1) ?: throw RuntimeException("Account not found")
        entityManager.clear()
        println("[Check] Account1 Balance: ${account1.balance}")

        Thread.sleep(5_000)

        val account2 = findAccount.findAccount(1) ?: throw RuntimeException("Account not found")
        entityManager.clear()
        println("[Check] Account2 Balance: ${account2.balance}")
    }

    @Transactional
    override fun register() {
        loadAccount.loadAccount(Account())
    }
}