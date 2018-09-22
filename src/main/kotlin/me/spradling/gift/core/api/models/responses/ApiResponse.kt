package me.spradling.gift.core.api.models.responses

import me.spradling.gift.core.api.models.errors.Error
import me.spradling.gift.core.api.models.errors.ErrorDetails
import java.util.Optional

class ApiResponse {

  val statusCode: Int
  val body: Optional<Any>

  constructor(statusCode: Int) {
    this.statusCode = statusCode
    this.body = Optional.empty()
  }

  constructor(statusCode: Int, body: Any) {
    this.statusCode = statusCode
    this.body = Optional.of(body)
  }

  constructor(details: ErrorDetails) {
    this.statusCode = details.statusCode
    this.body = Optional.of(Error(details))
  }
}