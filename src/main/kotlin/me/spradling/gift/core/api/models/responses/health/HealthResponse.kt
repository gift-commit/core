package me.spradling.gift.core.api.models.responses.health

import com.fasterxml.jackson.annotation.JsonProperty

class HealthResponse {

  @JsonProperty("healthy")
  val healthy: Boolean = true

}