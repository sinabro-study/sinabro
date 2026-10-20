package per.sinabro.adapter.`in`.member

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import per.sinabro.application.port.`in`.member.command.RegisterMemberCommand
import per.sinabro.application.port.`in`.member.usecase.RegisterMemberUsecase

@RestController
class MemberController(
    private val registerMemberUsecase: RegisterMemberUsecase
) {

    @PostMapping("/member")
    fun register(@RequestBody command: RegisterMemberCommand) {
        registerMemberUsecase.register(command)
    }
}
