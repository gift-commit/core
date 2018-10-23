package me.spradling.gift.core.api.models.responses

import io.netty.handler.codec.http.HttpResponseStatus

data class RetrievedResourceResponse<T> (val resource: T): ApiResponse(HttpResponseStatus.OK.code(), resource!!)