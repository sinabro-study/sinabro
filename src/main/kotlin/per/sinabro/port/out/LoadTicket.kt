package per.sinabro.port.out

import per.sinabro.domain.Ticket

interface LoadTicket {
    fun loadTicket(ticket: Ticket): Ticket
}
