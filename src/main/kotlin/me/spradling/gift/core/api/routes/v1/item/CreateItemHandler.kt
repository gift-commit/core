package me.spradling.gift.core.api.routes.v1.item

import io.vertx.core.Future
import me.spradling.gift.core.api.models.ApiRequest
import me.spradling.gift.core.api.models.Item
import me.spradling.gift.core.api.models.errors.ErrorDetails
import me.spradling.gift.core.api.models.responses.ApiResponse
import me.spradling.gift.core.api.models.responses.CreatedResourceResponse
import me.spradling.gift.core.api.routes.GiftCommitHandler
import me.spradling.gift.core.conversion.GiftCommitStorageConverter
import me.spradling.gift.core.database.GiftCommitStorageClient
import javax.inject.Inject

class CreateItemHandler @Inject constructor(private val storageClient: GiftCommitStorageClient) : GiftCommitHandler<Item>(Item::class.java) {

  private val storageConverter = GiftCommitStorageConverter(storageClient)

  override fun handleRequest(request: ApiRequest<Item>): Future<ApiResponse> {
    if (request.requestBody.isInvalid()) {
      return Future.succeededFuture(ApiResponse.from(ErrorDetails.INVALID_REQUEST))
    }

    val item = request.requestBody!!

    return storageClient.createItem(storageConverter.convert(item)).compose { id ->
      Future.succeededFuture<ApiResponse>(CreatedResourceResponse(id))
    }
  }

  fun Item?.isInvalid() : Boolean {
    return try {
      checkNotNull(this?.name)
      checkNotNull(this?.price)
      checkNotNull(this?.event)
      false
    } catch(ex : IllegalStateException) {
      true
    }
  }
}

