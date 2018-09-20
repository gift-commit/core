package me.spradling.gift.core.api.models.configuration

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import me.spradling.gift.core.api.database.GiftCommitStorageClient
import me.spradling.gift.core.api.database.aws.RDSGiftCommitStorageClient
import me.spradling.gift.core.api.models.GiftCommitStorageProvider

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