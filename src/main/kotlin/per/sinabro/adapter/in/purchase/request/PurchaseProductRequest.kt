package per.sinabro.adapter.`in`.purchase.request

data class PurchaseProductRequest(
    val memberId: Long,
    val productId: Long,
    val quantity: Int,
)
