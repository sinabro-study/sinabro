package per.sinabro.adapter.out.kafka

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import per.sinabro.application.port.out.event.PublishEvent

@Component
class KafkaClient(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
) : PublishEvent {

    override fun publish(topic: String, data: Any) {
        kafkaTemplate.send(topic, data)
    }
}