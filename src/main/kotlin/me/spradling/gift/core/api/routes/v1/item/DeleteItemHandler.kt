package me.spradling.gift.core.api.routes.v1.item

import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Future
import me.spradling.gift.core.api.models.ApiRequest
import me.spradling.gift.core.api.models.responses.ApiResponse
import me.spradling.gift.core.api.routes.GiftCommitHandler
import me.spradling.gift.core.database.GiftCommitStorageClient
import javax.inject.Inject

class DeleteItemHandler @Inject constructor(val storageClient: GiftCommitStorageClient) : GiftCommitHandler<Void>(Void::class.java) {

  override fun handleRequest(request: ApiRequest<Void>): Future<ApiResponse> {
    val itemId = request.context.pathParam("itemId")

    return storageClient.deleteItem(itemId).compose { _ ->
      Future.succeededFuture(ApiResponse(HttpResponseStatus.NO_CONTENT.code()))
    }.recover { error ->
      Future.succeededFuture(ApiResponse.from(error))
    }
  }
}