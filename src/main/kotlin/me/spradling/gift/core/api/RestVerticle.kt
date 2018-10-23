package me.spradling.gift.core.api

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.ext.web.handler.impl.BodyHandlerImpl
import me.spradling.gift.core.api.models.configuration.GiftCommitConfiguration
import me.spradling.gift.core.api.routes.HealthHandler
import me.spradling.gift.core.api.routes.v1.CreateAccountHandler
import me.spradling.gift.core.api.routes.v1.DeleteAccountHandler
import me.spradling.gift.core.api.routes.v1.UpdateAccountHandler
import me.spradling.gift.core.api.routes.v1.item.CreateItemHandler
import me.spradling.gift.core.api.routes.v1.item.DeleteItemHandler
import me.spradling.gift.core.api.routes.v1.item.UpdateItemHandler
import me.spradling.gift.core.database.GiftCommitStorageClient
import me.spradling.gift.core.api.routes.v1.ListAccountsHandler
import javax.inject.Inject

class RestVerticle @Inject constructor(val configuration: GiftCommitConfiguration,
                                       val storageClient: GiftCommitStorageClient) : AbstractVerticle() {

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

    router.route(HttpMethod.POST, "/*").handler(BodyHandlerImpl())
    router.route(HttpMethod.PATCH, "/*").handler(BodyHandlerImpl())

    router.route("/swagger/*").handler(StaticHandler.create("swagger"))
    router.get("/health").handler(HealthHandler())
    router.get("/v1/accounts").handler(ListAccountsHandler(storageClient))

    router.post("/v1/accounts").handler(CreateAccountHandler(storageClient))
    router.patch("/v1/accounts/:accountId").handler(UpdateAccountHandler(storageClient))
    router.delete("/v1/accounts/:accountId").handler(DeleteAccountHandler(storageClient))

    router.post("/v1/items").handler(CreateItemHandler(storageClient))
    router.patch("/v1/items/:itemId").handler(UpdateItemHandler(storageClient))
    router.delete("/v1/items/:itemId").handler(DeleteItemHandler(storageClient))

    return router
  }
}