package per.sinabro.port.`in`.service

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.Callable
import java.util.concurrent.Executors

@SpringBootTest
class TicketServiceTest(
    private val ticketService: TicketService
) : FunSpec({

    test("동시성 이슈") {
        val theadCount = 10
        val ticketId = ticketService.create()

        val executorService = Executors.newFixedThreadPool(theadCount)
        val tasks = List(theadCount * theadCount) {
            Callable {
                try {
                    ticketService.reserve(1, ticketId)
                    true
                } catch (e: Exception) {
                    false
                }
            }
        }
        val results = executorService.invokeAll(tasks).map { it.get() }

        val successCount = results.count { it }
        val failCount = results.count { !it }

        successCount shouldBe theadCount
        failCount shouldBe theadCount * theadCount - theadCount
    }

    test("동시성 이슈 해결") {
        val theadCount = 10
        val limitCount = 1
        val ticketId = ticketService.create()

        val executorService = Executors.newFixedThreadPool(theadCount)
        val tasks = List(theadCount * theadCount) {
            Callable {
                try {
                    ticketService.reservePriority(1, ticketId)
                    true
                } catch (e: Exception) {
                    false
                }
            }
        }
        val results = executorService.invokeAll(tasks).map { it.get() }

        val successCount = results.count { it }
        val failCount = results.count { !it }

        successCount shouldBe limitCount
        failCount shouldBe theadCount * theadCount - limitCount
    }
})
