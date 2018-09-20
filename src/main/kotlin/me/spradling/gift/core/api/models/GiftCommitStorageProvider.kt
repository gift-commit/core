package me.spradling.gift.core.api.models

import me.spradling.gift.core.api.database.GiftCommitStorageClient
import me.spradling.gift.core.api.models.configuration.StorageConfiguration

enum class GiftCommitStorageProvider(val value: String) {
  RDS("RDS"){
    override fun getStorageClient(configuration: StorageConfiguration) : GiftCommitStorageClient {
      return configuration.rdsConfiguration
    }
  };

  abstract fun getStorageClient(configuration: StorageConfiguration) : GiftCommitStorageClient

  override fun toString(): String {
    return value
  }
}