package me.spradling.gift.core.api

import io.vertx.core.Vertx
import javax.inject.Inject

class Application @Inject constructor(val vertx: Vertx) {

  companion object {
    @JvmStatic
    fun main(args : Array<String>) {
      DaggerAppComponent.create().application().launch()
    }
  }

  fun launch () {
    vertx.deployVerticle(RestVerticle())
  }
}