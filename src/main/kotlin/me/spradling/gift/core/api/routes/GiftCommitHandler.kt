package me.spradling.gift.core.api.routes

import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.json.Json
import io.vertx.ext.web.RoutingContext
import me.spradling.gift.core.api.models.ApiRequest
import me.spradling.gift.core.api.models.errors.ErrorDetails
import me.spradling.gift.core.api.models.responses.ApiResponse

abstract class GiftCommitHandler<T>(val requestBody: Class<T>): Handler<RoutingContext> {

  abstract fun handleRequest(request: ApiRequest<T>) : Future<ApiResponse>

  override fun handle(context: RoutingContext) {
    val requestBody = context.bodyAsJson?.mapTo(requestBody)

    val responseFuture = handleRequest(ApiRequest(context, requestBody))

    responseFuture.setHandler { result ->

      if (result.succeeded()) {
        val response = result.result()

        if (!response.body.isPresent) {
          context.response().setStatusCode(response.statusCode).end()
        } else {
          context.response().setStatusCode(response.statusCode).end(Json.mapper.writeValueAsString(response.body.get()))
        }
      }
      else {
        context.response()
               .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
               .end(Json.mapper.writeValueAsString(ErrorDetails.INTERNAL_SERVER_ERROR))
      }
    }
  }
}