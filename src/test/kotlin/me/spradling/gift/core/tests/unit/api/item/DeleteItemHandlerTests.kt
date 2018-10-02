package me.spradling.gift.core.tests.unit.api.item

import io.netty.handler.codec.http.HttpResponseStatus
import me.spradling.gift.core.api.extensions.unwrap
import me.spradling.gift.core.api.models.ApiRequest
import me.spradling.gift.core.api.models.errors.ErrorDetails
import me.spradling.gift.core.api.routes.v1.item.DeleteItemHandler
import me.spradling.gift.core.tests.unit.UnitTestBase
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito.`when`

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("When I call `handle` on DeleteItemHandler,")
class DeleteItemHandlerTests : UnitTestBase() {

  private val handler = DeleteItemHandler(inMemoryStorageClient)

  @Nested
  @DisplayName("given a valid item id,")
  inner class ValidItemId {

    private val dbItem = converter.convert(validItems["api"]!!)

    @BeforeEach
    fun setup() {
      inMemoryStorageClient.createItem(dbItem)
      `when`(mockContext.pathParam("itemId")).thenReturn(dbItem.itemId)
    }

    @Test
    @DisplayName("should succeed with 204 Http Status")
    fun succeedsWith204() {

      val response = handler.handleRequest(ApiRequest(mockContext)).unwrap()

      Assertions.assertThat(response.statusCode).isEqualTo(HttpResponseStatus.NO_CONTENT.code())
    }

    @Test
    @DisplayName("should succeed with no response body")
    fun succeedsWithNoBody() {

      val response = handler.handleRequest(ApiRequest(mockContext)).unwrap()

      Assertions.assertThat(response.body.isPresent).isEqualTo(false)
    }

  }

  @Nested
  @DisplayName("given an item id that does not exist,")
  inner class UserDoesNotExist {

    @BeforeEach
    fun setup() {
      `when`(mockContext.pathParam("itemId")).thenReturn("DOES NOT EXIST")
    }

    @Test
    @DisplayName("should fail with 404 Http Status")
    fun succeedsWith404() {

      val response = handler.handleRequest(ApiRequest(mockContext)).unwrap()

      Assertions.assertThat(response.statusCode).isEqualTo(HttpResponseStatus.NOT_FOUND.code())
    }

    @Test
    @DisplayName("should succeed with no response body")
    fun failsWithNotFoundError() {

      val response = handler.handleRequest(ApiRequest(mockContext)).unwrap()

      verifyErrorResponse(response, ErrorDetails.RESOURCE_NOT_FOUND)
    }
  }
}