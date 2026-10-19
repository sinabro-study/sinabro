package per.sinabro.application.port.service

import org.springframework.stereotype.Service
import per.sinabro.application.port.`in`.member.command.RegisterMemberCommand
import per.sinabro.application.port.`in`.member.usecase.RegisterMemberUsecase
import per.sinabro.application.port.out.FindMember
import per.sinabro.application.port.out.LoadMember
import per.sinabro.domain.member.Member

@Service
class MemberApplicationService(
    private val findMember: FindMember,
    private val loadMember: LoadMember
) : RegisterMemberUsecase {

    override fun register(command: RegisterMemberCommand) {
        val (name) = command

        findMember.findMember(name) ?: loadMember.loadMember(Member(name))
    }
}
