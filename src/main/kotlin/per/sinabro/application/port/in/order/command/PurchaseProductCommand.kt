package per.sinabro.application.port.`in`.order.command

data class PurchaseProductCommand(
    val memberId: Long,
    val productId: Long,
    val quantity: Int,
)
