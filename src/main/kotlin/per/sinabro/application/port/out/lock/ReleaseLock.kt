package per.sinabro.application.port.out.lock

interface ReleaseLock {
    fun release(key: String)
}
