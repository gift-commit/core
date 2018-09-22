package me.spradling.gift.core.api.routes.v1

import io.netty.handler.codec.http.HttpResponseStatus
import me.spradling.gift.core.api.models.Account
import me.spradling.gift.core.api.models.ApiRequest
import me.spradling.gift.core.api.models.errors.ErrorDetails
import me.spradling.gift.core.api.models.responses.ApiResponse
import me.spradling.gift.core.api.routes.GiftCommitHandler
import me.spradling.gift.core.conversion.GiftCommitStorageConverter
import me.spradling.gift.core.database.GiftCommitStorageClient

class CreateAccountHandler(private val storageClient : GiftCommitStorageClient) : GiftCommitHandler<Account>(Account::class.java) {

  private val storageConverter  = GiftCommitStorageConverter(storageClient)

  override fun handleRequest(request: ApiRequest<Account>): ApiResponse {

    if (request.requestBody.isInvalid()) {
      return ApiResponse(ErrorDetails.INVALID_REQUEST)
    }

    val account = request.requestBody!!
    storageClient.createAccount(storageConverter.convert(account))

    return ApiResponse(HttpResponseStatus.CREATED.code())
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