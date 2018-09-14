package me.spradling.gift.core.api

import io.vertx.core.Vertx

class ApiEntrypoint {

  companion object {
    @JvmStatic
    fun main(args : Array<String>) {
      val vertx: Vertx = Vertx.vertx()
      vertx.deployVerticle(DaggerAppComponent.create().inject())
    }
  }
}