package me.spradling.gift.core.api.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Item @JsonCreator constructor(
    @JsonProperty("event")
    val event: String?,
    @JsonProperty("claimed")
    val claimed: Boolean?,
    @JsonProperty("name")
    val name: String?,
    @JsonProperty("url")
    val url: String?,
    @JsonProperty("price")
    val price: Double?,
    @JsonProperty("notes")
    val notes: String?
)

