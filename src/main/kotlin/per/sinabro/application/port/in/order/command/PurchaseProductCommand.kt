package per.sinabro.application.port.`in`.order.command

data class PurchaseProductCommand(
    val productId: Long,
    val quantity: Int,
)
