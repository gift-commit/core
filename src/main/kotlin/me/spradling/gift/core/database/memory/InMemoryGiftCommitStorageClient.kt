package me.spradling.gift.core.database.memory

import me.spradling.gift.core.api.models.errors.ErrorDetails
import me.spradling.gift.core.database.GiftCommitStorageClient
import me.spradling.gift.core.api.models.exceptions.GiftCommitException
import me.spradling.gift.core.database.models.Account
import me.spradling.gift.core.database.models.Item
import java.util.stream.Collectors

class InMemoryGiftCommitStorageClient : GiftCommitStorageClient {

  private val accounts: HashMap<String, Account> = hashMapOf()
  private val items: HashMap<String, Item> = hashMapOf()

  override fun createAccount(account: Account) {
    if (items[account.accountId] != null) {
      throw GiftCommitException(ErrorDetails.RESOURCE_CONFLICT)
    }

    accounts[account.accountId] = account
  }

  override fun getAccount(accountId: String): Account {
    return accounts[accountId] ?: throw GiftCommitException(ErrorDetails.RESOURCE_NOT_FOUND)
  }

  override fun updateAccount(accountId: String, updatedAccount: Account) {
    accounts[accountId] = updatedAccount
  }

  override fun deleteAccount(accountId: String) {
    accounts.remove(accountId) ?: throw GiftCommitException(ErrorDetails.RESOURCE_NOT_FOUND)
  }

  override fun createItem(item: Item) {
    if (items[item.itemId] != null) {
      throw GiftCommitException(ErrorDetails.RESOURCE_CONFLICT)
    }

    items[item.itemId] = item
  }

  override fun getItem(itemId: String): Item {
    return items[itemId] ?: throw GiftCommitException(ErrorDetails.RESOURCE_NOT_FOUND)
  }

  override fun getAccountItems(accountId: String): List<Item> {
    return items.values.stream().filter{ item  -> item.accountId == accountId}.collect(Collectors.toList())
  }

  override fun updateItem(itemId: String, updatedItem: Item) {
    items[itemId] = updatedItem
  }

  override fun deleteItem(itemId: String) {
    accounts.remove(itemId) ?: throw GiftCommitException(ErrorDetails.RESOURCE_NOT_FOUND)
  }
}