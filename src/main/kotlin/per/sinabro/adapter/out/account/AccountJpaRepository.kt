package per.sinabro.adapter.out.account

import org.springframework.data.jpa.repository.JpaRepository
import per.sinabro.domain.account.Account

interface AccountJpaRepository : JpaRepository<Account, Long>