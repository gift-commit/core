package me.spradling.gift.core.tests.unit.api

import io.netty.handler.codec.http.HttpResponseStatus
import me.spradling.gift.core.api.extensions.unwrap
import me.spradling.gift.core.api.models.ApiRequest
import me.spradling.gift.core.api.routes.v1.GetAccountsHandler
import me.spradling.gift.core.tests.unit.UnitTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.mockito.Mockito.`when`

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("When I call `handleRequest` on GetAccountsHandler,")
internal class GetAccountsHandlerTests : UnitTestBase() {

  private val handler = GetAccountsHandler(inMemoryStorageClient)

  @Nested
  @DisplayName("given valid query parameters,")
  inner class ValidQueryParameter {

    private val apiAccount = apiConverter.convert(validAccounts["account"]!!)

    @BeforeEach
    fun setup() {
//      `when`(mockContext.queryParam("limit")).thenReturn(databaseAccount)
    }

    @Test
    @DisplayName("should succeed with 200 code")
    fun shouldReturnAccounts() {

      val response = handler.handleRequest(ApiRequest(mockContext)).unwrap()

      assertThat(response.statusCode).isEqualTo(HttpResponseStatus.OK.code())
      assertThat(response.body.isPresent).isEqualTo(true)
    }

  }

  @Nested
  @DisplayName("given no or incomplete account limit")
  inner class WrongLimitQuery {

    @Test
    @DisplayName("should fail with wrong parameters")
    fun shouldFailWithWrongQueryParams() {

      `when`(mockContext.queryParam("limit")).thenReturn(emptyList())

      val response = handler.handleRequest(ApiRequest(mockContext)).unwrap()

      assertThat(response.statusCode).isEqualTo(HttpResponseStatus.UNPROCESSABLE_ENTITY.code())
    }

    @Test
    @DisplayName("should fail with no parameters")
    fun shouldFailWithNoQueryParams() {

      `when`(mockContext.queryParam("")).thenReturn(emptyList())

      val response = handler.handleRequest(ApiRequest(mockContext)).unwrap()

      assertThat(response.statusCode).isEqualTo(HttpResponseStatus.UNPROCESSABLE_ENTITY.code())
    }

  }
}