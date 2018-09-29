package me.spradling.gift.core.tests.unit.api

import io.netty.handler.codec.http.HttpResponseStatus
import me.spradling.gift.core.api.extensions.unwrap
import me.spradling.gift.core.api.models.ApiRequest
import me.spradling.gift.core.api.models.errors.ErrorDetails
import me.spradling.gift.core.api.routes.v1.DeleteAccountHandler
import me.spradling.gift.core.tests.unit.UnitTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito.`when`

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("When I call `handle` on DeleteAccountHandler,")
class DeleteAccountHandlerTests : UnitTestBase() {

  private val handler = DeleteAccountHandler(inMemoryStorageClient)

  @Nested
  @DisplayName("given a valid account id,")
  inner class ValidAccountId {

    private val dbAccount = converter.convert(validAccounts["api"]!!)

    @BeforeEach
    fun setup() {
      inMemoryStorageClient.createAccount(dbAccount)
      `when`(mockContext.pathParam("accountId")).thenReturn(dbAccount.accountId)
    }

    @Test
    @DisplayName("should succeed with 204 Http Status")
    fun succeedsWith204() {

      val response = handler.handleRequest(ApiRequest(mockContext)).unwrap()

      assertThat(response.statusCode).isEqualTo(HttpResponseStatus.NO_CONTENT.code())
    }

    @Test
    @DisplayName("should succeed with no response body")
    fun succeedsWithNoBody() {

      val response = handler.handleRequest(ApiRequest(mockContext)).unwrap()

      assertThat(response.body.isPresent).isEqualTo(false)
    }

  }

  @Nested
  @DisplayName("given an account id that does not exist,")
  inner class UserDoesNotExist {

    @BeforeEach
    fun setup() {
      `when`(mockContext.pathParam("accountId")).thenReturn("DOES NOT EXIST")
    }

    @Test
    @DisplayName("should fail with 404 Http Status")
    fun succeedsWith404() {

      val response = handler.handleRequest(ApiRequest(mockContext)).unwrap()

      assertThat(response.statusCode).isEqualTo(HttpResponseStatus.NOT_FOUND.code())
    }

    @Test
    @DisplayName("should succeed with no response body")
    fun failsWithNotFoundError() {

      val response = handler.handleRequest(ApiRequest(mockContext)).unwrap()

      verifyErrorResponse(response, ErrorDetails.RESOURCE_NOT_FOUND)
    }
  }
}