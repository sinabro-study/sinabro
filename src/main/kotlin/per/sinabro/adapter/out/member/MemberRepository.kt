package per.sinabro.adapter.out.member

import org.springframework.stereotype.Repository
import per.sinabro.application.port.out.FindMember
import per.sinabro.application.port.out.LoadMember
import per.sinabro.domain.member.Member

@Repository
class MemberRepository(
    private val memberJpaRepository: MemberJpaRepository
) : FindMember, LoadMember {

    override fun findMember(name: String): Member? {
        return memberJpaRepository.findMemberByName(name)
    }

    override fun loadMember(member: Member): Member {
        return memberJpaRepository.save(member)
    }
}
