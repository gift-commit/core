package me.spradling.gift.core.tests.unit.api

import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Future
import me.spradling.gift.core.api.extensions.unwrap
import me.spradling.gift.core.api.models.ApiRequest
import me.spradling.gift.core.api.models.errors.ErrorDetails
import me.spradling.gift.core.api.routes.v1.GetAccountsHandler
import me.spradling.gift.core.database.memory.InMemoryGiftCommitStorageClient
import me.spradling.gift.core.tests.unit.UnitTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("When I call `handleRequest` on GetAccountsHandler,")
internal class GetAccountsHandlerTests : UnitTestBase() {

  private val getHandler = GetAccountsHandler(inMemoryStorageClient)
  private val mockInMemoryStorageClient = Mockito.mock(InMemoryGiftCommitStorageClient::class.java)

  @Nested
  @DisplayName("given no results")
  inner class NoResults {

    @Test
    @DisplayName("request should fail with Not Found error")
    fun failsWithNotFoundError() {
      val response = getHandler.handleRequest(ApiRequest(mockContext)).unwrap()

      verifyErrorResponse(response, ErrorDetails.RESOURCE_NOT_FOUND)
    }
  }

  @Nested
  @DisplayName("given a valid request")
  inner class ValidRequest {

    @BeforeEach
    fun setup() {
      inMemoryStorageClient.createAccount(storageConverter.convert(validAccounts["api"]!!))
    }

    @Nested
    @DisplayName("given valid query parameters,")
    inner class ValidQueryParameter {

      private val accounts = listOf(
          me.spradling.gift.core.database.models.Account(
              "123",
              "321",
              "Test",
              "User",
              "test@gmail.com",
              "password"),
          me.spradling.gift.core.database.models.Account(
              "234",
              "432",
              "Test2",
              "User2",
              "test2@gmail.com",
              "password")
      )

      @BeforeEach
      fun setup() {
        `when`(mockContext.queryParam("limit")).thenReturn(listOf("10")) // Return that we want 10 accounts when calling the "limit" query param
        `when`(mockInMemoryStorageClient.getAccounts(10)).thenReturn(Future.succeededFuture(accounts)) // Return a list of multiple accounts
      }

      @Test
      @DisplayName("should succeed with 200 code")
      fun shouldReturnAccounts() {
        val response = getHandler.handleRequest(ApiRequest(mockContext)).unwrap() // Execute request

        assertThat(response.statusCode).isEqualTo(HttpResponseStatus.OK.code()) // Should have 200 code
        assertThat(response.body.isPresent).isEqualTo(true)
        assertThat(accounts == (response.body.get() as List<*>))
      }
    }

    @Nested
    @DisplayName("given no account limit")
    inner class NoLimitQuery {

      @Test
      @DisplayName("should pass with no parameters")
      fun shouldPassWithNoQueryParams() {
        val response = getHandler.handleRequest(ApiRequest(mockContext)).unwrap()

        assertThat(response.statusCode).isEqualTo(HttpResponseStatus.OK.code())
        assertThat(response.body.isPresent).isEqualTo(true)
        val retrievedAccounts = response.body.get() as List<*>
        assertThat(listOf(validAccounts["api"]).size == retrievedAccounts.size &&
            listOf(validAccounts["api"]).containsAll(retrievedAccounts) &&
            retrievedAccounts.containsAll(listOf(validAccounts["api"])))
      }

    }

    @Nested
    @DisplayName("given incomplete account limit")
    inner class WrongLimitQuery {

      @Test
      @DisplayName("should fail with wrong parameters")
      fun shouldFailWithWrongQueryParams() {

        `when`(mockContext.queryParam("limit")).thenReturn(listOf("a"))

        val response = getHandler.handleRequest(ApiRequest(mockContext)).unwrap()

        assertThat(response.statusCode).isEqualTo(HttpResponseStatus.UNPROCESSABLE_ENTITY.code())
      }
    }
  }
}