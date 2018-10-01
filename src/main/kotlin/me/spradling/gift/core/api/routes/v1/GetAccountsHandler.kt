package me.spradling.gift.core.api.routes.v1

import io.vertx.core.Future
import me.spradling.gift.core.api.models.ApiRequest
import me.spradling.gift.core.api.models.errors.ErrorDetails
import me.spradling.gift.core.api.models.responses.ApiResponse
import me.spradling.gift.core.api.models.responses.RetrievedResourceResponse
import me.spradling.gift.core.api.routes.GiftCommitHandler
import me.spradling.gift.core.conversion.GiftCommitApiConverter
import me.spradling.gift.core.database.GiftCommitStorageClient

class GetAccountsHandler(private val storageClient: GiftCommitStorageClient) : GiftCommitHandler<Void>(Void::class.java) {

  private val storageConverter = GiftCommitApiConverter()

  override fun handleRequest(request: ApiRequest<Void>): Future<ApiResponse> {

    if (request.context.queryParams().contains("limit")) {
      val limit = request.context.queryParam("limit").first()
      if (limit.isNotBlank()) {
        val accounts = storageConverter.convert(storageClient.getAccounts(limit.toInt()))
        if (accounts.isNotEmpty()) {
          return Future.succeededFuture<ApiResponse>(RetrievedResourceResponse(accounts))
        }
      }
    }
    return Future.succeededFuture(ApiResponse.from(ErrorDetails.INVALID_REQUEST))

  }

}