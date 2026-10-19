package per.sinabro.adapter.out.member

import org.springframework.data.jpa.repository.JpaRepository
import per.sinabro.domain.member.Member

interface MemberJpaRepository : JpaRepository<Member, Long> {
    fun findMemberByName(name: String): Member?
}
