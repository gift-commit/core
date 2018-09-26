package me.spradling.gift.core.api.routes.v1

import me.spradling.gift.core.api.models.ApiRequest
import me.spradling.gift.core.api.models.responses.ApiResponse
import me.spradling.gift.core.api.models.responses.RetrievedResourceResponse
import me.spradling.gift.core.api.routes.GiftCommitHandler
import me.spradling.gift.core.conversion.GiftCommitApiConverter
import me.spradling.gift.core.database.GiftCommitStorageClient

class GetAccountsHandler(private val storageClient: GiftCommitStorageClient) : GiftCommitHandler<Void>(Void::class.java) {

  private val storageConverter = GiftCommitApiConverter()

  override fun handleRequest(request: ApiRequest<Void>): ApiResponse {

//        if (request.context) {
//            return ApiResponse(ErrorDetails.INVALID_REQUEST)
//        }

    val count = request.context.queryParam("limit").first().toInt()
    val accounts = storageConverter.convert(storageClient.getAccounts(count))

    return RetrievedResourceResponse(accounts)
  }
}