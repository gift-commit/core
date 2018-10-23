package me.spradling.gift.core.api.models.configuration

import com.fasterxml.jackson.annotation.JsonProperty
import me.spradling.gift.core.database.GiftCommitStorageClient

abstract class StorageClient constructor(
    @JsonProperty("host")
    val host: String,
    @JsonProperty("instance")
    val instance: String,
    @JsonProperty("useSsl")
    val useSsl: Boolean,
    @JsonProperty("user")
    val user: String,
    @JsonProperty("password")
    val password: String) : GiftCommitStorageClient