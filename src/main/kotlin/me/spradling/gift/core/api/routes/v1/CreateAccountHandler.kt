package me.spradling.gift.core.api.routes.v1

import io.vertx.core.Future
import me.spradling.gift.core.api.models.Account
import me.spradling.gift.core.api.models.ApiRequest
import me.spradling.gift.core.api.models.errors.ErrorDetails
import me.spradling.gift.core.api.models.responses.ApiResponse
import me.spradling.gift.core.api.models.responses.CreatedResourceResponse
import me.spradling.gift.core.api.routes.GiftCommitHandler
import me.spradling.gift.core.conversion.GiftCommitStorageConverter
import me.spradling.gift.core.database.GiftCommitStorageClient

class CreateAccountHandler(private val storageClient : GiftCommitStorageClient) : GiftCommitHandler<Account>(Account::class.java) {

  private val storageConverter  = GiftCommitStorageConverter(storageClient)

  override fun handleRequest(request: ApiRequest<Account>): Future<ApiResponse> {

    if (request.requestBody.isInvalid()) {
      return Future.succeededFuture(ApiResponse.from(ErrorDetails.INVALID_REQUEST))
    }

    val account = request.requestBody!!

    return storageClient.createAccount(storageConverter.convert(account)).compose { id ->
      Future.succeededFuture<ApiResponse>(CreatedResourceResponse(id))
    }
  }

  private fun Account?.isInvalid() : Boolean {
    return try {
      checkNotNull(this?.groupId)
      checkNotNull(this?.firstName)
      checkNotNull(this?.lastName)
      checkNotNull(this?.email)
      checkNotNull(this?.password)
      false
    } catch(ex : IllegalStateException) {
      true
    }
  }
}