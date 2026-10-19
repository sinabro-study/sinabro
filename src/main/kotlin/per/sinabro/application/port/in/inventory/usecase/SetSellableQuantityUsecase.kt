package per.sinabro.application.port.`in`.inventory.usecase

import per.sinabro.application.port.`in`.inventory.command.SetSellableQuantityCommand

interface SetSellableQuantityUsecase {
    fun setSellableQuantity(command: SetSellableQuantityCommand)
}
