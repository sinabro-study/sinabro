package per.sinabro.application.port.out.inventory

import per.sinabro.domain.inventory.Inventory

interface LoadInventory {
    fun load(inventory: Inventory): Inventory
}