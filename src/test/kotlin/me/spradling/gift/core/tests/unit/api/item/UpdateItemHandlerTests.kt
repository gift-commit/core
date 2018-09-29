package me.spradling.gift.core.tests.unit.api.item

import io.netty.handler.codec.http.HttpResponseStatus
import me.spradling.gift.core.api.extensions.unwrap
import me.spradling.gift.core.api.models.ApiRequest
import me.spradling.gift.core.api.models.errors.ErrorDetails
import me.spradling.gift.core.api.routes.v1.item.UpdateItemHandler
import me.spradling.gift.core.tests.unit.UnitTestBase
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("When I call `handle` on UpdateItemHandler,")
class UpdateItemHandlerTests : UnitTestBase() {

  private val handler = UpdateItemHandler(inMemoryStorageClient)

  @Nested
  @DisplayName("given a valid request,")
  inner class GivenValidRequest {

    private val databaseItem = converter.convert(validItems["api"]!!)

    @BeforeEach
    fun setup() {
      Mockito.`when`(mockContext.pathParam("itemId")).thenReturn(databaseItem.itemId)
    }

    @Nested
    @DisplayName("for a valid user,")
    inner class ForAValidUser {

      @BeforeEach
      fun setup() {
        inMemoryStorageClient.createItem(databaseItem)
      }

      @Test
      @DisplayName("request should succeed with a 204 No Content")
      fun succeedsWith204() {
        val response = handler.handleRequest(ApiRequest(mockContext, validItems["api"])).unwrap()
        Assertions.assertThat(response.statusCode).isEqualTo(HttpResponseStatus.NO_CONTENT.code())
      }

      @Test
      @DisplayName("request should succeed with No Content")
      fun succeedsWithNoContent() {
        val response = handler.handleRequest(ApiRequest(mockContext, validItems["api"])).unwrap()
        org.junit.jupiter.api.Assertions.assertFalse(response.body.isPresent)
      }

      @Nested
      @DisplayName("with the request with extra fields")
      inner class RequestHasExtraFields {
        @Test
        @DisplayName("request with extra fields should still succeed with a 204 No Content")
        fun extraFieldStillSucceedsWith204() {
          val response = handler.handleRequest(ApiRequest(mockContext, validItems["extraFields"])).unwrap()
          Assertions.assertThat(response.statusCode).isEqualTo(HttpResponseStatus.NO_CONTENT.code())
        }

        @Test
        @DisplayName("request with extra fields should succeed with No Content")
        fun extraFieldStillSucceedsWithNoContent() {
          val response = handler.handleRequest(ApiRequest(mockContext, validItems["extraFields"])).unwrap()
          org.junit.jupiter.api.Assertions.assertFalse(response.body.isPresent)
        }
      }
    }

    @Nested
    @DisplayName("for a user that doesn't exist,")
    inner class UserDoesNotExist {

      @Test
      @DisplayName("request should fail with a 404 Http Status Code")
      fun failsWith404() {
        val response = handler.handleRequest(ApiRequest(mockContext, validItems["api"])).unwrap()

        Assertions.assertThat(response.statusCode).isEqualTo(HttpResponseStatus.NOT_FOUND.code())
      }

      @Test
      @DisplayName("request should fail with Not Found error")
      fun failsWithNotFoundError() {
        val response = handler.handleRequest(ApiRequest(mockContext, validItems["api"])).unwrap()

        verifyErrorResponse(response, ErrorDetails.RESOURCE_NOT_FOUND)
      }
    }
  }
}
