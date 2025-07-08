package per.sinabro.port.out.ticket

import per.sinabro.domain.ticket.TicketReservation

interface LoadTicketReservation {
    fun load(ticketReservation: TicketReservation)
}
