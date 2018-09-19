package me.spradling.gift.core.api.models.configuration

import com.fasterxml.jackson.annotation.JsonProperty
import me.spradling.gift.core.api.database.GiftCommitStorageClient
import me.spradling.gift.core.api.database.aws.RDSGiftCommitStorageClient
import me.spradling.gift.core.api.models.GiftCommitStorageProvider

data class StorageConfiguration constructor(
    @JsonProperty("selected")
    val selected: GiftCommitStorageProvider,
    @JsonProperty("rds")
    val rdsConfiguration: RDSGiftCommitStorageClient
) {

  fun getSelectedClient() : GiftCommitStorageClient {
    return selected.getStorageClient(this)
  }
}
