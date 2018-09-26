package me.spradling.gift.core.api

import com.fasterxml.jackson.databind.DeserializationFeature
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import javax.inject.Inject

class Application @Inject constructor(val vertx: Vertx, val restVerticle: RestVerticle) {

  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      Json.mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      Json.prettyMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      DaggerAppComponent.create().application().launch()
    }
  }

  fun launch() {
    vertx.deployVerticle(restVerticle)
  }
}