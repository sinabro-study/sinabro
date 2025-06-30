package per.sinabro.port.out

import per.sinabro.domain.TicketReservation

interface LoadTicketReservation {
    fun load(ticketReservation: TicketReservation)
}
