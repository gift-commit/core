package me.spradling.gift.core.api.models.exceptions

import me.spradling.gift.core.api.models.errors.ErrorDetails

class GiftCommitException : Exception {

  private val error : ErrorDetails

  constructor(error: ErrorDetails): super() {
    this.error = error
  }

  constructor(error: ErrorDetails, cause: Exception) : super(cause){
    this.error = error
  }
}