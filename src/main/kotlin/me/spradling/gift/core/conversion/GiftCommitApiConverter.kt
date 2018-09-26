package me.spradling.gift.core.conversion

import me.spradling.gift.core.api.models.Account
import me.spradling.gift.core.database.GiftCommitStorageClient
import java.util.stream.Collectors
import javax.inject.Inject

class GiftCommitApiConverter {

  fun convert(accounts: List<me.spradling.gift.core.database.models.Account>): List<Account> {

    return accounts.stream().map { account: me.spradling.gift.core.database.models.Account ->
      Account(account.groupId,
              account.firstName,
              account.lastName,
              account.email,
              account.password)
    }.collect(Collectors.toList())
  }
}