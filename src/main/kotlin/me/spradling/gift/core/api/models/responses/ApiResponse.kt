package me.spradling.gift.core.api.models.responses

import me.spradling.gift.core.api.models.errors.Error
import me.spradling.gift.core.api.models.errors.ErrorDetails
import me.spradling.gift.core.api.models.exceptions.GiftCommitException
import java.util.Optional

open class ApiResponse {

  val statusCode: Int
  val body: Optional<Any>

  companion object {
    @JvmStatic
    fun from(exception: Throwable) : ApiResponse {
      if (exception is GiftCommitException) {
        return ApiResponse.from(exception.error)
      }

      return ApiResponse.from(ErrorDetails.INTERNAL_SERVER_ERROR)
    }

    @JvmStatic
    fun from(details: ErrorDetails) : ApiResponse {
      return ApiResponse(details.statusCode, Error(details))
    }
  }

  constructor(statusCode: Int) {
    this.statusCode = statusCode
    this.body = Optional.empty()
  }

  constructor(statusCode: Int, body: Any) {
    this.statusCode = statusCode
    this.body = Optional.of(body)
  }
}