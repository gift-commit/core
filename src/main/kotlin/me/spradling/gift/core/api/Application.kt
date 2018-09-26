package me.spradling.gift.core.api

import com.fasterxml.jackson.annotation.JsonInclude
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import javax.inject.Inject

class Application @Inject constructor(val vertx: Vertx, val restVerticle: RestVerticle) {

  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      Json.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
      Json.prettyMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
      DaggerAppComponent.create().application().launch()
    }
  }

  fun launch() {
    vertx.deployVerticle(restVerticle)
  }
}