package per.sinabro.application.port.out

import per.sinabro.domain.member.Member

interface FindMember {
    fun findMember(name: String): Member?
}
