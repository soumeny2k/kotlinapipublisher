package com.example.api.controlplane.publish

import com.example.api.controlplane.transferobject.ControlPlane

interface Publisher {

    fun publishApi(api: ControlPlane.Api)

    fun publishRoute(route: ControlPlane.Route)
}

