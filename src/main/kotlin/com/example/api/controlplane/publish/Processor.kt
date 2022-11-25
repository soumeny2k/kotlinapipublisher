package com.example.api.controlplane.publish

import com.example.api.controlplane.config.PublisherConfig
import com.example.api.controlplane.kafka.KafkaData
import com.example.api.controlplane.transferobject.ControlPlane
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Component

@Component
class Processor(
    private val config: PublisherConfig,
    private val azurePublisher: AzurePublisher,
    private val kongPublisher: KongPublisher
) {
    private val mapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
    private val thirdPartyPublishers = mutableListOf<Publisher>()

    init {
        config.publishers.map {
            if (it == "AZURE") thirdPartyPublishers.add(azurePublisher)
            if (it == "KONG") thirdPartyPublishers.add(kongPublisher)
        }
    }

    fun run(data: String) {
        val kafkaData = mapper.readValue(data, KafkaData::class.java)
        thirdPartyPublishers.forEach {
            when (kafkaData.event) {
                "API" -> it.publishApi(mapper.readValue(kafkaData.data, ControlPlane.Api::class.java))
                "ROUTE" -> it.publishRoute(mapper.readValue(kafkaData.data, ControlPlane.Route::class.java))
                else -> throw Exception("not supported")
            }
        }
    }
}
