package me.spradling.gift.core.api.models.responses

import com.fasterxml.jackson.annotation.JsonCreator
import io.netty.handler.codec.http.HttpResponseStatus

data class RetrievedResourceResponse<T> @JsonCreator constructor(val resource: T): ApiResponse(HttpResponseStatus.OK.code(), resource!!)