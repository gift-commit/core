package me.spradling.gift.core.api.routes.v1.item

import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Future
import me.spradling.gift.core.api.models.ApiRequest
import me.spradling.gift.core.api.models.Item
import me.spradling.gift.core.api.models.errors.ErrorDetails
import me.spradling.gift.core.api.models.responses.ApiResponse
import me.spradling.gift.core.api.routes.GiftCommitHandler
import me.spradling.gift.core.conversion.GiftCommitStorageConverter
import me.spradling.gift.core.database.GiftCommitStorageClient
import javax.inject.Inject

class UpdateItemHandler @Inject constructor(private val storageClient: GiftCommitStorageClient): GiftCommitHandler<Item>(Item::class.java) {

  private val storageConverter = GiftCommitStorageConverter(storageClient)

  override fun handleRequest(request: ApiRequest<Item>): Future<ApiResponse> {

    val item = request.requestBody ?: return Future.succeededFuture(ApiResponse.from(ErrorDetails.INVALID_REQUEST))
    val itemId = request.context.pathParam("itemId")

    return storageConverter.merge(itemId, item).compose { mergedItem ->
      storageClient.updateItem(itemId, mergedItem).compose { _ ->
        Future.succeededFuture(ApiResponse(HttpResponseStatus.NO_CONTENT.code()))
      }
    }.recover{ error ->
      Future.succeededFuture(ApiResponse.from(error))
    }
  }
}