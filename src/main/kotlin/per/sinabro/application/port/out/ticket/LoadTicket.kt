package per.sinabro.application.port.out.ticket

import per.sinabro.domain.ticket.Ticket

interface LoadTicket {
    fun loadTicket(ticket: Ticket): Ticket
}
