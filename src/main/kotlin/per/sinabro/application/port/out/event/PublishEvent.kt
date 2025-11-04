package per.sinabro.application.port.out.event

interface PublishEvent {
    fun publish(topic: String, data: Any)
}
