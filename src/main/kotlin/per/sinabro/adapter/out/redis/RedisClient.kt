package per.sinabro.adapter.out.redis

import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import per.sinabro.port.out.lock.AcquireLock
import per.sinabro.port.out.lock.ReleaseLock
import java.util.concurrent.TimeUnit

private const val WAIT_SECONDS = 3L
private const val RELEASE_SECONDS = 5L

@Component
class RedisClient(
    private val redisson: RedissonClient
) : AcquireLock, ReleaseLock {

    override fun acquire(key: String): Boolean {
        val lock = redisson.getLock("lock:$key")

        return lock.tryLock(WAIT_SECONDS, RELEASE_SECONDS, TimeUnit.SECONDS)
    }

    override fun release(key: String) {
        redisson.getLock("lock:$key").takeIf { it.isHeldByCurrentThread }?.unlock()
    }
}
