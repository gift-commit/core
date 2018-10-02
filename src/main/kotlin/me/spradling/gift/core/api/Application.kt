package me.spradling.gift.core.api

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.annotation.JsonInclude
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import javax.inject.Inject

class Application @Inject constructor(val vertx: Vertx, val restVerticle: RestVerticle) {

  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      configureJsonMappers()
      DaggerAppComponent.create().application().launch()
    }

    private fun configureJsonMappers() {
      Json.mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      Json.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
      Json.prettyMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      Json.prettyMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }
  }

  fun launch() {
    vertx.deployVerticle(restVerticle)
  }
}