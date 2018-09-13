package me.spradling.gift.core.api.models.responses.health

import com.fasterxml.jackson.annotation.JsonProperty
import me.spradling.gift.core.api.models.responses.ApiResponse

class HealthResponse : ApiResponse() {

  @JsonProperty("healthy")
  val healthy: Boolean = true

}