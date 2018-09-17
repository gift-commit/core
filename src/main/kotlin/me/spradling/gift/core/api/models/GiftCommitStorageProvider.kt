package me.spradling.gift.core.api.models

enum class GiftCommitStorageProvider(val value: String) {
  RDS("RDS");

  override fun toString(): String {
    return value
  }
}