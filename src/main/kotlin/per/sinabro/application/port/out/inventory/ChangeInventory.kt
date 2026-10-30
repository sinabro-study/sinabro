package per.sinabro.application.port.out.inventory

interface ChangeInventory {
    fun change(productId: Long, quantity: Int): Int
}
