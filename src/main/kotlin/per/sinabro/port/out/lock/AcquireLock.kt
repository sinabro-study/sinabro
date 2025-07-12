package per.sinabro.port.out.lock

interface AcquireLock {
    fun acquire(key: String): Boolean
}
