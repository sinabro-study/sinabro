package per.sinabro.domain.inventory

class OutOfStockException(
    inventoryQuantity: Int,
    purchaseQuantity: Int
) : RuntimeException(
    "재고가 부족합니다. inventoryQuantity=$inventoryQuantity purchaseQuantity=$purchaseQuantity"
)
