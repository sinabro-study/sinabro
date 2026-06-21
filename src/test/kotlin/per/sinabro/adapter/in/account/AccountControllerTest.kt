package per.sinabro.adapter.`in`.account

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod
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
        val accountId = restTemplate.postForObject("/account", null, Long::class.java)!!

        val executor = Executors.newSingleThreadExecutor()
        val withdrawFuture = executor.submit {
            restTemplate.exchange("/account/$accountId/withdraw", HttpMethod.PATCH, null, Unit::class.java)
        }

        // withdraw 가 flush 를 완료할 때까지 대기 (withdraw 내부 sleep = 1s)
        Thread.sleep(200)

        val balance = restTemplate.getForObject("/account/$accountId/uncommited", Long::class.java)!!

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
        val accountId = restTemplate.postForObject("/account", null, Long::class.java)!!

        val executor = Executors.newSingleThreadExecutor()
        @Suppress("UNCHECKED_CAST")
        val checkFuture = executor.submit<List<Int>> {
            restTemplate.getForObject("/account/$accountId/commited", List::class.java) as List<Int>
        }

        // accountForCommitted 가 첫 번째 읽기를 완료할 때까지 대기
        Thread.sleep(500)

        // 5초 대기 구간에서 withdraw 커밋
        restTemplate.exchange("/account/$accountId/withdraw", HttpMethod.PATCH, null, Unit::class.java)

        val balances = checkFuture.get()!!
        executor.shutdown()

        balances[0] shouldBe 1000
        balances[1] shouldBe 500
    }
})
