package me.spradling.gift.core.conversion

import me.spradling.gift.core.api.models.Account
import java.util.stream.Collectors

class GiftCommitApiConverter {

  fun convert(accounts: List<me.spradling.gift.core.database.models.Account>): List<Account> {

    return accounts.stream().map { account ->
      Account(account.groupId,
              account.firstName,
              account.lastName,
              account.email)
    }.collect(Collectors.toList())
  }
}