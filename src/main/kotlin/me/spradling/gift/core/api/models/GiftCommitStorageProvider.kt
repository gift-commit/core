package me.spradling.gift.core.api.models

import me.spradling.gift.core.database.GiftCommitStorageClient
import me.spradling.gift.core.api.models.configuration.StorageConfiguration
import me.spradling.gift.core.database.memory.InMemoryGiftCommitStorageClient

enum class GiftCommitStorageProvider(val value: String) {
  RDS("RDS"){
    override fun getStorageClient(configuration: StorageConfiguration) : GiftCommitStorageClient {
      return configuration.rdsConfiguration
    }
  },
  IN_MEMORY("IN_MEMORY"){
    override fun getStorageClient(configuration: StorageConfiguration): GiftCommitStorageClient {
      return InMemoryGiftCommitStorageClient()
    }
  };

  abstract fun getStorageClient(configuration: StorageConfiguration) : GiftCommitStorageClient

  override fun toString(): String {
    return value
  }
}