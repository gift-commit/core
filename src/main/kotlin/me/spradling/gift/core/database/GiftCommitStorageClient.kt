package me.spradling.gift.core.database

import me.spradling.gift.core.database.models.Account
import me.spradling.gift.core.database.models.Item

interface GiftCommitStorageClient {

  fun createAccount(account: Account) : String
  fun getAccounts(count: Int): List<Account>
  fun getAccount(accountId: String): Account
  fun updateAccount(accountId: String, updatedAccount: Account)
  fun deleteAccount(accountId: String)

  fun createItem(item: Item) : String
  fun getItem(itemId: String): Item
  fun getAccountItems(accountId: String): List<Item>
  fun updateItem(itemId: String, updatedItem: Item)
  fun deleteItem(itemId: String)
}