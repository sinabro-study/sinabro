package per.sinabro.application.port.out.order

import per.sinabro.domain.order.Order

interface LoadOrder {
    fun load(order: Order): Order
}
