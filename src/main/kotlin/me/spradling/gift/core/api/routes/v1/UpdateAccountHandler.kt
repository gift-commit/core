package me.spradling.gift.core.api.routes.v1

import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Future
import me.spradling.gift.core.api.models.Account
import me.spradling.gift.core.api.models.ApiRequest
import me.spradling.gift.core.api.models.errors.ErrorDetails
import me.spradling.gift.core.api.models.exceptions.GiftCommitException
import me.spradling.gift.core.api.models.exceptions.ResourceNotFoundException
import me.spradling.gift.core.api.models.responses.ApiResponse
import me.spradling.gift.core.api.routes.GiftCommitHandler
import me.spradling.gift.core.conversion.GiftCommitStorageConverter
import me.spradling.gift.core.database.GiftCommitStorageClient
import javax.inject.Inject

class UpdateAccountHandler @Inject constructor(private val storageClient: GiftCommitStorageClient) : GiftCommitHandler<Account>(Account::class.java) {

  private val storageConverter  = GiftCommitStorageConverter(storageClient)

  override fun handleRequest(request: ApiRequest<Account>): Future<ApiResponse> {

    val account = request.requestBody ?: return Future.succeededFuture(ApiResponse.from(ErrorDetails.INVALID_REQUEST))
    val accountId = request.context.pathParam("accountId") ?: return Future.succeededFuture(ApiResponse.from(ErrorDetails.INVALID_REQUEST))

    return storageConverter.merge(accountId, account).compose { mergedAccount ->
      storageClient.updateAccount(accountId, mergedAccount).compose { _ ->
        Future.succeededFuture(ApiResponse(HttpResponseStatus.NO_CONTENT.code()))
      }
    }.recover{ error ->
      Future.succeededFuture(ApiResponse.from(error))
    }
  }
}