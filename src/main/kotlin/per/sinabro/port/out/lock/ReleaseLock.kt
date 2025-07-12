package per.sinabro.port.out.lock

interface ReleaseLock {
    fun release(key: String)
}
