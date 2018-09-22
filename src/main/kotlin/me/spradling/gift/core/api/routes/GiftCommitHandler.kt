package me.spradling.gift.core.api.routes

import io.vertx.core.Handler
import io.vertx.core.json.Json
import io.vertx.ext.web.RoutingContext
import me.spradling.gift.core.api.models.ApiRequest
import me.spradling.gift.core.api.models.responses.ApiResponse

abstract class GiftCommitHandler<T>(val requestBody: Class<T>): Handler<RoutingContext> {

  abstract fun handleRequest(request: ApiRequest<T>) : ApiResponse

  override fun handle(context: RoutingContext) {
    val requestBody = context.bodyAsJson?.mapTo(requestBody)

    val response = handleRequest(ApiRequest(context, requestBody))

    if (!response.body.isPresent) {
      context.response().setStatusCode(response.statusCode).end()
    }
    else {
      context.response().setStatusCode(response.statusCode).end(Json.mapper.writeValueAsString(response.body.get()))
    }
  }
}