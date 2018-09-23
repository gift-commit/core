package me.spradling.gift.core.api.models.errors

enum class ErrorDetails constructor(val statusCode: Int, val message: String) {
  RESOURCE_NOT_FOUND(404, "The requested resource was not found"),
  RESOURCE_CONFLICT(409, "The request resource already exists"),
  INVALID_REQUEST(422, "The request was invalid")
}