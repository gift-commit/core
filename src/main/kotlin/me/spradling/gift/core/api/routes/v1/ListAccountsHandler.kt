package me.spradling.gift.core.api.routes.v1

import io.vertx.core.Future
import me.spradling.gift.core.api.models.ApiRequest
import me.spradling.gift.core.api.models.errors.ErrorDetails
import me.spradling.gift.core.api.models.responses.ApiResponse
import me.spradling.gift.core.api.models.responses.RetrievedResourceResponse
import me.spradling.gift.core.api.routes.GiftCommitHandler
import me.spradling.gift.core.conversion.GiftCommitApiConverter
import me.spradling.gift.core.database.GiftCommitStorageClient

class ListAccountsHandler(private val storageClient: GiftCommitStorageClient) : GiftCommitHandler<Void>(Void::class.java) {

  private val storageConverter = GiftCommitApiConverter()

  override fun handleRequest(request: ApiRequest<Void>): Future<ApiResponse> {

    val limit = request.context.queryParam("limit").firstOrNull()

    return try {
      storageClient.listAccounts(limit?.toInt()).compose { accounts ->
        Future.succeededFuture<ApiResponse>(RetrievedResourceResponse(storageConverter.convert(accounts)))
      }.recover { error -> Future.succeededFuture(ApiResponse.from(error)) }
    } catch (e: NumberFormatException) {
      Future.succeededFuture(ApiResponse.from(ErrorDetails.INVALID_REQUEST))
    }
  }

}