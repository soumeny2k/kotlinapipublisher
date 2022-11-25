package com.example.api.controlplane.publish

import com.example.api.controlplane.transferobject.ControlPlane
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class KongPublisher : Publisher {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val mapper = ObjectMapper().registerModule(KotlinModule.Builder().build())

    override fun publishApi(api: ControlPlane.Api) {
        logger.info("publishing api to kong")
        val kongService = KongService(
            name = api.name,
            version = 1,
            type = api.type,
            protocol = api.protocol
        )
        logger.info(mapper.writeValueAsString(kongService))
    }

    override fun publishRoute(route: ControlPlane.Route) {
        logger.info("publishing route to kong")
        val kongRoute = KongRoute(
            name = route.name,
            retries = route.retries
        )
        logger.info(mapper.writeValueAsString(kongRoute))
    }
}

data class KongService @JvmOverloads constructor(
    val name: String,
    val version: Int,
    val type: String,
    val protocol: String,
    val spec: String? = null,
    val lifeCycleStatus: String? = null
)

data class KongRoute @JvmOverloads constructor(
    val name: String,
    var retries: Int? = null,
    var rateLimit: Int? = null,
    var connectionTimeout: Long? = null,
    var cacheEnabled: Boolean? = null
)
