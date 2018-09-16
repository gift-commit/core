package me.spradling.gift.core.api.models.configuration

import com.fasterxml.jackson.annotation.JsonProperty

data class ApiConfiguration constructor(
    @JsonProperty("host")
    val host: String,
    @JsonProperty("port")
    val port: Int,
    @JsonProperty("ssl")
    val ssl: Boolean
)