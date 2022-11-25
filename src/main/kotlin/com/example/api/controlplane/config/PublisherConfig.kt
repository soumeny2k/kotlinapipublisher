package com.example.api.controlplane.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "app")
data class PublisherConfig(
    var publishers: List<String> = emptyList()
)
