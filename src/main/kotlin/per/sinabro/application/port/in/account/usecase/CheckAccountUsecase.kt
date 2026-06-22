package per.sinabro.application.port.`in`.account.usecase

import per.sinabro.domain.account.Account

interface CheckAccountUsecase {
    fun checkReadUncommited(id: Long): Long
    fun checkReadCommited(id: Long): List<Long>
    fun checkAllBalanced(): List<Int>
}