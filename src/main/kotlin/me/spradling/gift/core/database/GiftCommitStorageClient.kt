package me.spradling.gift.core.database

import io.vertx.core.Future
import me.spradling.gift.core.database.models.Account
import me.spradling.gift.core.database.models.Item

interface GiftCommitStorageClient {

  fun createAccount(account: Account) : Future<String>
  fun getAccount(accountId: String): Future<Account>
  fun updateAccount(accountId: String, updatedAccount: Account) : Future<Void>
  fun deleteAccount(accountId: String) : Future<Void>
  fun getAccounts(limit: Int?): Future<List<Account>>
  fun createItem(item: Item) : Future<String>
  fun getItem(itemId: String): Future<Item>
  fun getAccountItems(accountId: String): Future<List<Item>>
  fun updateItem(itemId: String, updatedItem: Item) : Future<Void>
  fun deleteItem(itemId: String) : Future<Void>
}