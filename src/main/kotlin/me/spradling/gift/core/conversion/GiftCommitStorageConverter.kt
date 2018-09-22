package me.spradling.gift.core.conversion

import me.spradling.gift.core.database.GiftCommitStorageClient
import me.spradling.gift.core.database.models.Account
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

  fun merge(accountId: String, account: me.spradling.gift.core.api.models.Account): Account {
    val existingAccount = storageClient.getAccount(accountId)
    return Account(accountId,
                   account.groupId ?: existingAccount.groupId,
                   account.firstName ?: existingAccount.firstName,
                   account.lastName ?: existingAccount.lastName,
                   account.email ?: existingAccount.email,
                   account.password ?: existingAccount.password)
  }
}