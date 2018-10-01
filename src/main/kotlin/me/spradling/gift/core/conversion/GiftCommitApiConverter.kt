package me.spradling.gift.core.conversion

import io.vertx.core.Future
import me.spradling.gift.core.api.models.Account
import java.util.stream.Collectors

class GiftCommitApiConverter {

  fun convert(accounts: Future<List<me.spradling.gift.core.database.models.Account>>): List<Account> {

    return if (accounts.result() != null) {
      accounts.result().stream().map { account: me.spradling.gift.core.database.models.Account ->
        Account(account.groupId,
            account.firstName,
            account.lastName,
            account.email)
      }.collect(Collectors.toList())
    } else {
      emptyList()
    }
  }
}