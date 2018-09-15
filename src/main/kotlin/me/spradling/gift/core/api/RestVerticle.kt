package me.spradling.gift.core.api

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler
import me.spradling.gift.core.api.routes.HealthHandler

class RestVerticle : AbstractVerticle() {

  override fun start(startFuture: Future<Void>) {
    val router = configureRouter()

    vertx.createHttpServer()
        .requestHandler(router::accept)
        .listen(8080) { result ->
          if (result.succeeded()) {
            print("Successfully started on port 8080")
            startFuture.complete()
          } else {
            print("Failed to start." + result.cause())
            startFuture.fail(result.cause())
          }
        }
  }

  private fun configureRouter(): Router {
    val router = Router.router(vertx)

    router.route("/*").handler(StaticHandler.create())
    router.get("/health").handler(HealthHandler())

    return router
  }
}