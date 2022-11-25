package com.example.api.controlplane.transferobject

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

sealed class ControlPlane {

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Api @JvmOverloads constructor(
        val name: String,
        var protocol: String,
        var type: String,
        var retries: Int? = null,
        var rateLimit: Int? = null,
        var connectionTimeout: Long? = null,
        var balance: String? = null,
        var backends: List<Backend> = emptyList(),
        var routes: List<Route> = emptyList()
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Route @JvmOverloads constructor(
        val name: String,
        var path: String? = null,
        var pathPattern: Int? = null,
        var method: String? = null,
        var retries: Int? = null,
        var rateLimit: Int? = null,
        var connectionTimeout: Long? = null,
        var cacheEnabled: Boolean? = null,
        var headers: List<RouteHeader> = emptyList(),
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Backend(
        val url: String,
        val weight: Int
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class RouteHeader(
        val name: String,
        val value: String
    )
}
