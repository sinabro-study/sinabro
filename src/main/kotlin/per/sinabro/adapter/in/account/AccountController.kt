package per.sinabro.adapter.`in`.account

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
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

    @PatchMapping("/account/withdraw/dirty")
    fun withdrawForDirtyRead() {
        withdrawAccountUsecase.withdrawForDirtyRead()
    }

    @PostMapping("/account")
    fun registerAccount() {
        registerAccountUsecase.register()
    }

    @GetMapping("/account")
    fun account() {
        checkAccountUsecase.checkForDirtyReadWithReadUncommited()
    }
}