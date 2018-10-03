package me.spradling.gift.core.database.memory

import io.vertx.core.Future
import me.spradling.gift.core.database.GiftCommitStorageClient
import me.spradling.gift.core.api.models.exceptions.ResourceNotFoundException
import me.spradling.gift.core.database.models.Account
import me.spradling.gift.core.database.models.Item
import java.util.stream.Collectors

class InMemoryGiftCommitStorageClient : GiftCommitStorageClient {

  val accounts: HashMap<String, Account> = hashMapOf()
  val items: HashMap<String, Item> = hashMapOf()

  override fun createAccount(account: Account) : Future<String> {
    val accountId = account.accountId

    accounts[accountId] = account
    return Future.succeededFuture(accountId)
  }

  override fun getAccount(accountId: String): Future<Account> {
    val account = accounts[accountId] ?: return Future.failedFuture(ResourceNotFoundException())

    return Future.succeededFuture(account)
  }

  override fun getAccounts(limit: Int?): Future<List<Account>> {
    val numElements = limit ?: accounts.values.size
    val accounts = accounts.values.toList().subList(0, minOf(numElements, accounts.values.size))

    if (accounts.isEmpty()) {
      return Future.failedFuture(ResourceNotFoundException())
    }

    return Future.succeededFuture(accounts)
  }

  override fun updateAccount(accountId: String, updatedAccount: Account) : Future<Void> {
    accounts[accountId] = updatedAccount
    return Future.succeededFuture()
  }

  override fun deleteAccount(accountId: String) : Future<Void> {
    accounts.remove(accountId) ?: return Future.failedFuture(ResourceNotFoundException())
    return Future.succeededFuture()
  }

  override fun createItem(item: Item) : Future<String> {
    val itemId = item.itemId

    items[itemId] = item
    return Future.succeededFuture(itemId)
  }

  override fun getItem(itemId: String): Future<Item> {
    val item = items[itemId] ?: return Future.failedFuture(ResourceNotFoundException())
    return Future.succeededFuture(item)
  }

  override fun getAccountItems(accountId: String): Future<List<Item>> {
    val items = items.values.stream().filter{ item  -> item.accountId == accountId}.collect(Collectors.toList())

    if (items.isEmpty()) {
      return Future.failedFuture(ResourceNotFoundException())
    }

    return Future.succeededFuture(items)
  }

  override fun updateItem(itemId: String, updatedItem: Item) : Future<Void> {
    items[itemId] = updatedItem
    return Future.succeededFuture()
  }

  override fun deleteItem(itemId: String) : Future<Void> {
    items.remove(itemId) ?: return Future.failedFuture(ResourceNotFoundException())
    return Future.succeededFuture()
  }
}