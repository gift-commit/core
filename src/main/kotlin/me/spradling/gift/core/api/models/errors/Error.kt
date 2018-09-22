package me.spradling.gift.core.api.models.errors

import com.fasterxml.jackson.annotation.JsonProperty

class Error (details: ErrorDetails) {

  @JsonProperty("code")
  private val code = details.name

  @JsonProperty("message")
  private val message = details.message
}