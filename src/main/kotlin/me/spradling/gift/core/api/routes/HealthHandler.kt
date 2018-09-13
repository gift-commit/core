package me.spradling.gift.core.api.routes

import io.vertx.core.Handler
import io.vertx.core.json.Json
import io.vertx.ext.web.RoutingContext
import me.spradling.gift.core.api.models.responses.health.HealthResponse

class HealthHandler: Handler<RoutingContext> {

  override fun handle(context: RoutingContext) {
    context.response().setStatusCode(200).end(Json.mapper.writeValueAsString(HealthResponse()))
  }
}