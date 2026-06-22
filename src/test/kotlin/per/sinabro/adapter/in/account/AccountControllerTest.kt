package per.sinabro.adapter.`in`.account

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.boot.test.web.client.getForObject
import org.springframework.boot.test.web.client.postForObject
import org.springframework.http.HttpMethod
import per.sinabro.adapter.`in`.account.request.RegisterRequest
import java.util.concurrent.Executors

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTest(
    private val restTemplate: TestRestTemplate
) : FunSpec({

    /**
     * Dirty Read 시나리오 (READ_UNCOMMITTED)
     *
     * 1. registerAccount  → account 생성 (balance=1000)
     * 2. withdraw         → flush 후 1초 대기 (미커밋 상태에서 balance=500 이 DB에 반영)
     * 3. accountForUncommitted → READ_UNCOMMITTED 로 미커밋 데이터(balance=500) 조회
     *
     * withdraw가 flush 완료 후 커밋 전 구간에서 checkReadUncommited 를 호출해
     * Dirty Read 가 발생하는지 검증한다.
     */
    test("Dirty Read: withdraw flush 후 READ_UNCOMMITTED 로 미커밋 데이터 조회") {
        val request = RegisterRequest(balance = 1000)
        val accountId = restTemplate.postForObject<Long>("/account", request)!!

        val executor = Executors.newSingleThreadExecutor()
        val withdrawFuture = executor.submit {
            restTemplate.exchange<Unit>("/account/$accountId/withdraw", HttpMethod.PATCH, null)
        }

        // withdraw 가 flush 를 완료할 때까지 대기 (withdraw 내부 sleep = 1s)
        Thread.sleep(200)

        val balance = restTemplate.getForObject<Long>("/account/$accountId/uncommited")!!

        withdrawFuture.get()
        executor.shutdown()

        balance shouldBe 500L
    }

    /**
     * Non-Repeatable Read 시나리오 (READ_COMMITTED)
     *
     * 1. registerAccount       → account 생성 (balance=1000)
     * 2. accountForCommitted   → 첫 번째 읽기(balance=1000) 후 5초 대기
     * 3. withdraw              → 2번 대기 중 커밋 완료 (balance=500)
     * 4. accountForCommitted   → 두 번째 읽기 → READ_COMMITTED 로 커밋된 값(balance=500) 반환
     *
     * 같은 트랜잭션 내에서 두 번 읽었는데 결과가 다른 Non-Repeatable Read 를 검증한다.
     */
    test("Non-Repeatable Read: READ_COMMITTED 에서 두 번의 읽기 결과가 다름") {
        val request = RegisterRequest(balance = 1000)
        val accountId = restTemplate.postForObject<Long>("/account", request)!!

        val executor = Executors.newSingleThreadExecutor()
        @Suppress("UNCHECKED_CAST")
        val checkFuture = executor.submit<List<Int>> {
            restTemplate.getForObject("/account/$accountId/commited", List::class.java) as List<Int>
        }

        // accountForCommitted 가 첫 번째 읽기를 완료할 때까지 대기
        Thread.sleep(500)

        // 5초 대기 구간에서 withdraw 커밋
        restTemplate.exchange<Unit>("/account/$accountId/withdraw", HttpMethod.PATCH)

        val balances = checkFuture.get()!!
        executor.shutdown()

        balances[0] shouldBe 1000
        balances[1] shouldBe 500
    }

    /**
     * Phantom Read 시나리오 (READ_COMMITTED)
     *
     * 1. registerAccount x2  → account 2건 생성
     * 2. accounts            → 첫 번째 읽기(count=N) 후 5초 대기
     * 3. registerAccount x1  → 2번 대기 중 새 account 커밋
     * 4. accounts            → 두 번째 읽기 → READ_COMMITTED 로 N+1 반환
     *
     * 같은 트랜잭션 내에서 두 번 읽었는데 행 수가 다른 Phantom Read 를 검증한다.
     */
    test("Phantom Read: READ_COMMITTED 에서 두 번의 읽기 결과가 다름") {
        val request = RegisterRequest(balance = 1000)
        restTemplate.postForObject<Long>("/account", request)!!
        restTemplate.postForObject<Long>("/account", request)!!

        val executor = Executors.newSingleThreadExecutor()
        @Suppress("UNCHECKED_CAST")
        val checkFuture = executor.submit<List<Int>> {
            restTemplate.getForObject("/accounts", List::class.java) as List<Int>
        }

        // accounts 가 첫 번째 읽기를 완료할 때까지 대기
        Thread.sleep(500)

        // 5초 대기 구간에서 새 account 추가 (phantom row)
        restTemplate.postForObject<Long>("/account", request)!!

        val counts = checkFuture.get()!!
        executor.shutdown()

        counts[1] shouldBe counts[0] + 1
    }
})
