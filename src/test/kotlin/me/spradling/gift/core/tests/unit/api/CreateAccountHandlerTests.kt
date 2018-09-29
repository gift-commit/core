package me.spradling.gift.core.tests.unit.api

import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.ext.web.RoutingContext
import me.spradling.gift.core.api.extensions.unwrap
import me.spradling.gift.core.api.models.ApiRequest
import me.spradling.gift.core.api.models.errors.Error
import me.spradling.gift.core.api.models.errors.ErrorDetails
import me.spradling.gift.core.api.models.responses.CreatedResource
import me.spradling.gift.core.api.routes.v1.CreateAccountHandler
import me.spradling.gift.core.database.memory.InMemoryGiftCommitStorageClient
import me.spradling.gift.core.tests.unit.UnitTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito.mock

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("When I call `handle` on CreateAccountHandler,")
class CreateAccountHandlerTests : UnitTestBase() {

  private val mockContext = mock(RoutingContext::class.java)!!
  private val inMemoryStorageClient = InMemoryGiftCommitStorageClient()
  private val handler = CreateAccountHandler(inMemoryStorageClient)

  @Nested
  @DisplayName("given a valid request,")
  inner class GivenValidRequest {

    @Test
    @DisplayName("request should succeed with a 201 Created")
    fun succeedsWithCreated() {
      val response = handler.handleRequest(ApiRequest(mockContext, validAccounts["api"])).unwrap()
      assertThat(response.statusCode).isEqualTo(HttpResponseStatus.CREATED.code())
    }

    @Test
    @DisplayName("request should return a valid created resource response")
    fun succeedsWithCreatedResourceResponse() {
      val response = handler.handleRequest(ApiRequest(mockContext, validAccounts["api"])).unwrap()

      assertThat(response.body.get().javaClass).isEqualTo(CreatedResource::class.java)
      val responseBody = response.body.get() as CreatedResource
      assertThat(responseBody.id).isNotBlank()
    }
  }

  @Nested
  @DisplayName("given an invalid request,")
  inner class GivenInvalidRequest {

    @Test
    @DisplayName("request should fail with a 422 Unprocessible Entity")
    fun failsWithUnprocessibleEntity() {
      val response = handler.handleRequest(ApiRequest(mockContext, invalidAccounts["create"])).unwrap()
      assertThat(response.statusCode).isEqualTo(HttpResponseStatus.UNPROCESSABLE_ENTITY.code())
    }

    @Test
    @DisplayName("request should return an Invalid Request error")
    fun failsWithInvalidRequestError() {
      val response = handler.handleRequest(ApiRequest(mockContext, invalidAccounts["create"])).unwrap()

      verifyErrorResponse(response, ErrorDetails.INVALID_REQUEST)
    }
  }
}