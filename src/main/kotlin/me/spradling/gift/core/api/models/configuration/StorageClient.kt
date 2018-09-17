package me.spradling.gift.core.api.models.configuration

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import me.spradling.gift.core.api.database.GiftCommitStorageClient
import me.spradling.gift.core.api.database.aws.RDSGiftCommitStorageClient
import me.spradling.gift.core.api.models.GiftCommitStorageProvider

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "name")
@JsonSubTypes(
    JsonSubTypes.Type(value = RDSGiftCommitStorageClient::class, name = "RDS")
)
abstract class StorageClient constructor(
    @JsonProperty("name")
    val name: GiftCommitStorageProvider,
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