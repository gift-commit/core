package me.spradling.gift.core.api.models.configuration

import com.fasterxml.jackson.annotation.JsonProperty

data class GiftCommitConfiguration constructor(
  @JsonProperty("api")
  val apiConfiguration : ApiConfiguration
)