package per.sinabro.application.port.out.lock

interface AcquireLock {
    fun acquire(key: String): Boolean
}
