package per.sinabro.adapter.out.account

import org.springframework.stereotype.Repository
import per.sinabro.application.port.out.account.FindAccount
import per.sinabro.application.port.out.account.LoadAccount
import per.sinabro.domain.account.Account

@Repository
class AccountRepository(
    private val accountJpaRepository: AccountJpaRepository
) : LoadAccount, FindAccount {

    override fun loadAccount(account: Account): Long {
        return accountJpaRepository.save(account).id!!
    }

    override fun findAccount(id: Long): Account? {
        return accountJpaRepository.findById(id).orElse(null)
    }
}