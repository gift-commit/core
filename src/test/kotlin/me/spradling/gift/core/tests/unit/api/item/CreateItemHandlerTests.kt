package me.spradling.gift.core.tests.unit.api.item

import io.netty.handler.codec.http.HttpResponseStatus
import me.spradling.gift.core.api.extensions.unwrap
import me.spradling.gift.core.api.models.ApiRequest
import me.spradling.gift.core.api.models.errors.ErrorDetails
import me.spradling.gift.core.api.models.responses.CreatedResource
import me.spradling.gift.core.api.routes.v1.item.CreateItemHandler
import me.spradling.gift.core.tests.unit.UnitTestBase
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("When I call `handle` on CreateItemHandler,")
class CreateItemHandlerTests : UnitTestBase() {

  private val handler = CreateItemHandler(inMemoryStorageClient)

  @Nested
  @DisplayName("given a valid request,")
  inner class GivenValidRequest {

    @Test
    @DisplayName("request should succeed with a 201 Created")
    fun succeedsWithCreated() {
      val response = handler.handleRequest(ApiRequest(mockContext, validItems["api"])).unwrap()
      Assertions.assertThat(response.statusCode).isEqualTo(HttpResponseStatus.CREATED.code())
    }

    @Test
    @DisplayName("request should return a valid created resource response")
    fun succeedsWithCreatedResourceResponse() {
      val response = handler.handleRequest(ApiRequest(mockContext, validItems["api"])).unwrap()

      Assertions.assertThat(response.body.get().javaClass).isEqualTo(CreatedResource::class.java)
      val responseBody = response.body.get() as CreatedResource
      Assertions.assertThat(responseBody.id).isNotBlank()
    }
  }

  @Nested
  @DisplayName("given an invalid request,")
  inner class GivenInvalidRequest {

    @Test
    @DisplayName("request should fail with a 422 Unprocessible Entity")
    fun failsWithUnprocessibleEntity() {
      val response = handler.handleRequest(ApiRequest(mockContext, invalidItems["create"])).unwrap()
      Assertions.assertThat(response.statusCode).isEqualTo(HttpResponseStatus.UNPROCESSABLE_ENTITY.code())
    }

    @Test
    @DisplayName("request should return an Invalid Request error")
    fun failsWithInvalidRequestError() {
      val response = handler.handleRequest(ApiRequest(mockContext, invalidItems["create"])).unwrap()

      verifyErrorResponse(response, ErrorDetails.INVALID_REQUEST)
    }
  }
}