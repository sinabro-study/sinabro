package per.sinabro.application.port.out.ticket

import per.sinabro.domain.ticket.TicketReservation

interface LoadTicketReservation {
    fun load(ticketReservation: TicketReservation)
}
