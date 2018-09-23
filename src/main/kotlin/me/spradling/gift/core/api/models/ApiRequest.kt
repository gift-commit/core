package me.spradling.gift.core.api.models

import io.vertx.ext.web.RoutingContext

data class ApiRequest<T> constructor(val context: RoutingContext, val requestBody: T?)