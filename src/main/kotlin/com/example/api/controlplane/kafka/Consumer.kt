package com.example.api.controlplane.kafka

import com.example.api.controlplane.config.KafkaConfig
import com.example.api.controlplane.publish.Processor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions
import reactor.kafka.receiver.ReceiverRecord

@Component
class Consumer(
    private val config: KafkaConfig,
    private val processor: Processor,
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private final var receiver: Flux<ReceiverRecord<String, String>>

    init {
        val consumerProps = mapOf(
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.GROUP_ID_CONFIG to "test",
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to config.host
        )
        val receiverOptions: ReceiverOptions<String, String> = ReceiverOptions
            .create<String, String>(consumerProps)
            .subscription(listOf(config.topic))

        receiver = KafkaReceiver.create(receiverOptions).receive()
        receiver.subscribe {
            logger.info("received: $it")
            try {
                processor.run(it.value())
                it.receiverOffset().commit()
            } catch (e: Exception) {
                logger.error("error", e)
            }
        }
    }
}

data class KafkaData(
    val event: String,
    val data: String
)
