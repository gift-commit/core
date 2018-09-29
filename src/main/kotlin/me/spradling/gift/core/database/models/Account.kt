package me.spradling.gift.core.database.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Account @JsonCreator constructor(
    @JsonProperty("accountId")
    val accountId: String,
    @JsonProperty("groupId")
    val groupId: String,
    @JsonProperty("firstName")
    val firstName: String,
    @JsonProperty("lastName")
    val lastName: String,
    @JsonProperty("email")
    val email: String,
    @JsonProperty("password")
    val password: String
)