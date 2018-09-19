package me.spradling.gift.core.api.models.configuration

import com.fasterxml.jackson.annotation.JsonProperty
import me.spradling.gift.core.api.models.GiftCommitStorageProvider

data class StorageConfiguration constructor(
    @JsonProperty("selected")
    val selected: GiftCommitStorageProvider,
    @JsonProperty("clients")
    val storageClients: List<StorageClient>
) {

  fun getSelectedClient() : StorageClient {
    return storageClients.stream()
                         .filter { storageClient -> storageClient.name == selected }
                         .findFirst()
                         .orElseThrow{ IllegalArgumentException("Unable to find configuration for selected storage provider") }
  }
}
