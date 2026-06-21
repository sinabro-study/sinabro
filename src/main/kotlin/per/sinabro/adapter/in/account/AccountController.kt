package per.sinabro.adapter.`in`.account

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import per.sinabro.application.port.`in`.account.usecase.CheckAccountUsecase
import per.sinabro.application.port.`in`.account.usecase.RegisterAccountUsecase
import per.sinabro.application.port.`in`.account.usecase.WithdrawAccountUsecase

@RestController
class AccountController(
    private val withdrawAccountUsecase: WithdrawAccountUsecase,
    private val registerAccountUsecase: RegisterAccountUsecase,
    private val checkAccountUsecase: CheckAccountUsecase
) {

    @PostMapping("/account")
    fun registerAccount(): Long {
        return registerAccountUsecase.register()
    }

    @PatchMapping("/account/{id}/withdraw")
    fun withdraw(@PathVariable id: Long) {
        withdrawAccountUsecase.withdraw(id)
    }

    @GetMapping("/account/{id}/uncommited")
    fun accountForUncommitted(@PathVariable id: Long): Long {
        return checkAccountUsecase.checkReadUncommited(id)
    }

    @GetMapping("/account/{id}/commited")
    fun accountForCommitted(@PathVariable id: Long): List<Long> {
        return checkAccountUsecase.checkReadCommited(id)
    }
}
