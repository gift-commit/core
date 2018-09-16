package me.spradling.gift.core.api

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler
import me.spradling.gift.core.api.models.configuration.GiftCommitConfiguration
import me.spradling.gift.core.api.routes.HealthHandler
import javax.inject.Inject

class RestVerticle @Inject constructor(val configuration: GiftCommitConfiguration) : AbstractVerticle() {

  override fun start(startFuture: Future<Void>) {
    val router = configureRouter()
    var apiConfiguration = configuration.apiConfiguration

    vertx.createHttpServer()
        .requestHandler(router::accept)
        .listen(apiConfiguration.port) { result ->
          if (result.succeeded()) {
            print(String.format("Successfully started on port %s", apiConfiguration.port))
            startFuture.complete()
          } else {
            print("Failed to start." + result.cause())
            startFuture.fail(result.cause())
          }
        }
  }

  private fun configureRouter(): Router {
    val router = Router.router(vertx)

    router.route("/swagger/*").handler(StaticHandler.create("swagger"))
    router.get("/health").handler(HealthHandler())

    return router
  }
}