package per.sinabro.adapter.out.order

import org.springframework.stereotype.Repository
import per.sinabro.application.port.out.order.LoadOrder
import per.sinabro.domain.order.Order

@Repository
class OrderRepository(
    private val orderJpaRepository: OrderJpaRepository
) : LoadOrder {

    override fun load(order: Order): Order {
        return orderJpaRepository.save(order)
    }
}
