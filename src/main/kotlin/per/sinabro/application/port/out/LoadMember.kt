package per.sinabro.application.port.out

import per.sinabro.domain.member.Member

interface LoadMember {
    fun loadMember(member: Member): Member
}
