package per.sinabro.application.port.out.account

import per.sinabro.domain.account.Account

interface LoadAccount {
    fun loadAccount(account: Account): Long
}