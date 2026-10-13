package per.sinabro.application.port.`in`.inventory.command

data class SetSellableQuantityCommand(
    val productId: Long,
    val quantity: Int,
)
