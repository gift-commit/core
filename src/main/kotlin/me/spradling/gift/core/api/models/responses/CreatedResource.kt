package me.spradling.gift.core.api.models.responses

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import io.netty.handler.codec.http.HttpResponseStatus

data class CreatedResourceResponse(val id: String) : ApiResponse(HttpResponseStatus.CREATED.code(), CreatedResource(id))

data class CreatedResource @JsonCreator constructor(@JsonProperty("id") val id : String)