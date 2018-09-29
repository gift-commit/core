package me.spradling.gift.core.api.models

import io.vertx.ext.web.RoutingContext

class ApiRequest<T> {

  val context : RoutingContext
  val requestBody : T?

  constructor(context: RoutingContext) {
    this.context = context
    this.requestBody = null
  }

  constructor(context: RoutingContext, requestBody: T?) {
    this.context = context
    this.requestBody = requestBody
  }
}