package per.sinabro.application.port.service

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import per.sinabro.domain.ticket.Ticket
import per.sinabro.domain.ticket.TicketReservation
import per.sinabro.application.port.`in`.ticket.usecase.CreateTicket
import per.sinabro.application.port.`in`.ticket.usecase.ReserveTicket
import per.sinabro.application.port.out.lock.AcquireLock
import per.sinabro.application.port.out.lock.ReleaseLock
import per.sinabro.application.port.out.ticket.FindTicket
import per.sinabro.application.port.out.ticket.LoadTicket
import per.sinabro.application.port.out.ticket.LoadTicketReservation
import per.sinabro.domain.ticket.event.RegisterTicketEvent

@Service
class TicketService(
    private val loadTicketReservation: LoadTicketReservation,
    private val findTicket: FindTicket,
    private val loadTicket: LoadTicket,
    private val acquireLock: AcquireLock,
    private val releaseLock: ReleaseLock,
    private val applicationEventPublisher: ApplicationEventPublisher
) : ReserveTicket, CreateTicket {

    @Transactional
    override fun reserve(userId: Long, ticketId: Long): Long {
        val ticket = findTicket.findByIdIsReserved(ticketId, false) ?: throw RuntimeException("Not found ticket.")

        ticket.reserve()
        loadTicketReservation.load(TicketReservation(userId, ticket))

        return 0
    }

    @Transactional
    override fun reservePriority(userId: Long, ticketId: Long): Long {
        val ticket = findTicket.findByIdIsReservedForPriority(ticketId, false) ?: throw RuntimeException("Not found ticket.")

        ticket.reserve()
        loadTicketReservation.load(TicketReservation(userId, ticket))

        return 0
    }

    @Transactional
    override fun reservePriorityV2(userId: Long, ticketId: Long): Long {
        val acquire = acquireLock.acquire("ticket:reserve")

        if (acquire.not()) {
            throw RuntimeException("Not found acquire.")
        }

        try {
            val ticket = findTicket.findByIdIsReservedForPriority(ticketId, false) ?: throw RuntimeException("Not found ticket.")
            ticket.reserve()
            loadTicketReservation.load(TicketReservation(userId, ticket))
        } catch (e: Exception) {
            throw RuntimeException("fail reserve ticket.")
        } finally {
            releaseLock.release("ticket:reserve")
        }

        return 0
    }

    @Transactional
    override fun create(): Long {
        val ticketId = loadTicket.loadTicket(Ticket()).id ?: 0

        applicationEventPublisher.publishEvent(
            RegisterTicketEvent(ticketId = ticketId)
        )

        return ticketId
    }
}
