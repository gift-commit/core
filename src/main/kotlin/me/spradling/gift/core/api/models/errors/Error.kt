package me.spradling.gift.core.api.models.errors

import com.fasterxml.jackson.annotation.JsonProperty

class Error (details: ErrorDetails) {

  @JsonProperty("code")
  val code = details.name

  @JsonProperty("message")
  val message = details.message
}