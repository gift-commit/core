package me.spradling.gift.core.database.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Item @JsonCreator constructor(
    @JsonProperty("itemId")
    val itemId: String,
    @JsonProperty("accountId")
    val accountId: String,
    @JsonProperty("event")
    val event: String,
    @JsonProperty("claimedBy")
    val claimedBy: String?,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("url")
    val url: String?,
    @JsonProperty("price")
    val price: Double,
    @JsonProperty("notes")
    val notes: String?
)