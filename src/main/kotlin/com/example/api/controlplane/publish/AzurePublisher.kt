package com.example.api.controlplane.publish

import com.example.api.controlplane.transferobject.ControlPlane
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class AzurePublisher : Publisher {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val mapper = ObjectMapper().registerModule(KotlinModule.Builder().build())

    override fun publishApi(api: ControlPlane.Api) {
        logger.info("publishing api to azure")
        val azureApi = AzureApi(
            name = api.name,
            version = 1,
            type = api.type,
            protocol = api.protocol
        )
        logger.info(mapper.writeValueAsString(azureApi))
    }

    override fun publishRoute(route: ControlPlane.Route) {
        logger.info("publishing route to azure")
        val azureBackend = AzureBackend(
            name = route.name,
            retries = route.retries
        )
        logger.info(mapper.writeValueAsString(azureBackend))
    }
}

data class AzureApi @JvmOverloads constructor(
    val name: String,
    val version: Int,
    val type: String,
    val protocol: String,
    val spec: String? = null,
    val lifeCycleStatus: String? = null
)

data class AzureBackend @JvmOverloads constructor(
    val name: String,
    var retries: Int? = null,
    var rateLimit: Int? = null,
    var connectionTimeout: Long? = null,
    var cacheEnabled: Boolean? = null
)
