package per.sinabro.application.port.`in`.member.usecase

import per.sinabro.application.port.`in`.member.command.RegisterMemberCommand

interface RegisterMemberUsecase {
    fun register(command: RegisterMemberCommand)
}
