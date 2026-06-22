package per.sinabro.application.port.out.account

import per.sinabro.domain.account.Account

interface FindAccount {
    fun findAccount(id: Long): Account?
    fun findAll(): List<Account>
    fun findAllByBalanceGreaterThanEqual(balance: Long): List<Account>
}
