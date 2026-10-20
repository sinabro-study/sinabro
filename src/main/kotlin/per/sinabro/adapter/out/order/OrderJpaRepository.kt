package per.sinabro.adapter.out.order

import org.springframework.data.jpa.repository.JpaRepository
import per.sinabro.domain.order.Order

interface OrderJpaRepository : JpaRepository<Order, Long>
