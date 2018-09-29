package me.spradling.gift.core.conversion

import io.vertx.core.Future
import me.spradling.gift.core.database.GiftCommitStorageClient
import me.spradling.gift.core.database.models.Account
import me.spradling.gift.core.database.models.Item
import java.util.UUID
import javax.inject.Inject

class GiftCommitStorageConverter @Inject constructor(private val storageClient: GiftCommitStorageClient) {

  fun convert(account: me.spradling.gift.core.api.models.Account): Account {
    return Account(UUID.randomUUID().toString(),
                   account.groupId!!,
                   account.firstName!!,
                   account.lastName!!,
                   account.email!!,
                   account.password!!)
  }

  fun convert(item: me.spradling.gift.core.api.models.Item): Item {
    return Item(UUID.randomUUID().toString(),
                "123",
                item.event!!,
                item.claimedBy,
                item.name!!,
                item.url,
                item.price!!,
                item.notes)
  }

  fun merge(accountId: String, account: me.spradling.gift.core.api.models.Account): Future<Account> {
    return storageClient.getAccount(accountId).compose { existingAccount ->
      Future.succeededFuture(Account(accountId,
                                    account.groupId ?: existingAccount.groupId,
                                    account.firstName ?: existingAccount.firstName,
                                    account.lastName ?: existingAccount.lastName,
                                    account.email ?: existingAccount.email,
                                    account.password ?: existingAccount.password))
    }
  }

  fun merge(itemId: String, item: me.spradling.gift.core.api.models.Item): Future<Item> {
    return storageClient.getItem(itemId).compose { existingItem ->
      Future.succeededFuture(Item(itemId,
                                  "123",
                                  item.event ?: existingItem.event,
                                  item.claimedBy ?: existingItem.claimedBy,
                                  item.name ?: existingItem.name,
                                  item.url ?: existingItem.url,
                                  item.price ?: existingItem.price,
                                  item.notes ?: existingItem.notes))
    }
  }
}