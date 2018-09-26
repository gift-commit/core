package me.spradling.gift.core.api.routes.v1

import io.netty.handler.codec.http.HttpResponseStatus
import me.spradling.gift.core.api.models.Account
import me.spradling.gift.core.api.models.ApiRequest
import me.spradling.gift.core.api.models.errors.ErrorDetails
import me.spradling.gift.core.api.models.exceptions.ResourceNotFoundException
import me.spradling.gift.core.api.models.responses.ApiResponse
import me.spradling.gift.core.api.routes.GiftCommitHandler
import me.spradling.gift.core.conversion.GiftCommitStorageConverter
import me.spradling.gift.core.database.GiftCommitStorageClient
import javax.inject.Inject

class UpdateAccountHandler @Inject constructor(private val storageClient: GiftCommitStorageClient) : GiftCommitHandler<Account>(Account::class.java) {

  private val storageConverter  = GiftCommitStorageConverter(storageClient)

  override fun handleRequest(request: ApiRequest<Account>): ApiResponse {

    val account = request.requestBody ?: return ApiResponse(ErrorDetails.INVALID_REQUEST)
    val accountId = request.context.pathParam("accountId") ?: return ApiResponse(ErrorDetails.INVALID_REQUEST)

    try {
      storageClient.updateAccount(accountId, storageConverter.merge(accountId, account))
    } catch (ex : ResourceNotFoundException) {
      return ApiResponse(ErrorDetails.RESOURCE_NOT_FOUND)
    }

    return ApiResponse(HttpResponseStatus.NO_CONTENT.code())
  }
}