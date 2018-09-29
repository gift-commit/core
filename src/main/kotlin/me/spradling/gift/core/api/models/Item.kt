package me.spradling.gift.core.api.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Item @JsonCreator constructor(
    @JsonProperty("event")
    val event: String?,
    @JsonProperty("claimed")
    val claimedBy: String?,
    @JsonProperty("name")
    val name: String?,
    @JsonProperty("url")
    val url: String?,
    @JsonProperty("price")
    val price: Double?,
    @JsonProperty("notes")
    val notes: String?
)

