package me.spradling.gift.core.api

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.ext.web.Router
import me.spradling.gift.core.api.routes.HealthHandler
import javax.inject.Inject

class RestVerticle @Inject constructor(): AbstractVerticle() {

  override fun start(startFuture : Future<Void>) {
    val start = Future.future<Void>()

    val router = Router.router(vertx)

    router.get("/health").handler(HealthHandler())

    vertx.createHttpServer()
        .requestHandler(router::accept)
        .listen(8080) { result ->
          if (result.succeeded()) {
            print("Successfully started on port 8080")
            start.complete()
          } else {
            print("Failed to start." + result.cause())
            start.fail(result.cause())
          }
        }
  }
}