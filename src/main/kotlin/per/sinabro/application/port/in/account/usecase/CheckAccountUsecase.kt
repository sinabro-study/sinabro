package per.sinabro.application.port.`in`.account.usecase

interface CheckAccountUsecase {
    fun checkReadUncommited(id: Long): Long
    fun checkReadCommited(id: Long): List<Long>
    fun checkAllBalanced(): List<Int>
}