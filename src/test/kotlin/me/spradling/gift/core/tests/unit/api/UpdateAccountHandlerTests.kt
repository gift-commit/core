package me.spradling.gift.core.tests.unit.api

import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.json.Json
import io.vertx.ext.web.RoutingContext
import me.spradling.gift.core.api.models.Account
import me.spradling.gift.core.api.models.ApiRequest
import me.spradling.gift.core.api.models.errors.Error
import me.spradling.gift.core.api.models.errors.ErrorDetails
import me.spradling.gift.core.api.routes.v1.UpdateAccountHandler
import me.spradling.gift.core.conversion.GiftCommitStorageConverter
import me.spradling.gift.core.database.memory.InMemoryGiftCommitStorageClient
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("When I call `handle` on UpdateAccountHandler,")
class UpdateAccountHandlerTests {

  private val mockContext = Mockito.mock(RoutingContext::class.java)!!
  private val inMemoryStorageClient = InMemoryGiftCommitStorageClient()
  private val handler = UpdateAccountHandler(inMemoryStorageClient)
  private val converter = GiftCommitStorageConverter(inMemoryStorageClient)

  private val validAccounts = hashMapOf<String, Account>()
  private val invalidAccounts = hashMapOf<String, Account>()

  init {
    getResourceList("/data/valid/account").forEach { fileName ->
      val resource = readResource("/data/valid/account/$fileName")
      validAccounts[File(fileName).nameWithoutExtension] = Json.mapper.readValue(resource, Account::class.java)
    }

    getResourceList("/data/invalid/account").forEach { fileName ->
      val resource = readResource("/data/invalid/account/$fileName")
      invalidAccounts[File(fileName).nameWithoutExtension] = Json.mapper.readValue(resource, Account::class.java)
    }
  }

  @Nested
  @DisplayName("given a valid request,")
  inner class GivenValidRequest {

    private val databaseAccount = converter.convert(validAccounts["api"]!!)

    @BeforeEach
    fun setup() {
      `when`(mockContext.pathParam("accountId")).thenReturn(databaseAccount.accountId)
    }

    @Nested
    @DisplayName("for a valid user,")
    inner class ForAValidUser {

      @BeforeEach
      fun setup() {
        inMemoryStorageClient.createAccount(databaseAccount)
      }

      @Test
      @DisplayName("request should succeed with a 204 No Content")
      fun succeedsWith204() {
        val response = handler.handleRequest(ApiRequest(mockContext, validAccounts["api"]))
        Assertions.assertThat(response.statusCode).isEqualTo(HttpResponseStatus.NO_CONTENT.code())
      }

      @Test
      @DisplayName("request should succeed with No Content")
      fun succeedsWithNoContent() {
        val response = handler.handleRequest(ApiRequest(mockContext, validAccounts["api"]))
        assertFalse(response.body.isPresent)
      }

      @Nested
      @DisplayName("with the request with extra fields")
      inner class RequestHasExtraFields {
        @Test
        @DisplayName("request with extra fields should still succeed with a 204 No Content")
        fun extraFieldStillSucceedsWith204() {
          val response = handler.handleRequest(ApiRequest(mockContext, validAccounts["extraFields"]))
          Assertions.assertThat(response.statusCode).isEqualTo(HttpResponseStatus.NO_CONTENT.code())
        }

        @Test
        @DisplayName("request with extra fields should succeed with No Content")
        fun extraFieldStillSucceedsWithNoContent() {
          val response = handler.handleRequest(ApiRequest(mockContext, validAccounts["extraFields"]))
          assertFalse(response.body.isPresent)
        }
      }
    }

    @Nested
    @DisplayName("for a user that doesn't exist,")
    inner class UserDoesNotExist {

      @Test
      @DisplayName("request should fail with a 404 Http Status Code")
      fun failsWith404() {
        val response = handler.handleRequest(ApiRequest(mockContext, validAccounts["api"]))
        Assertions.assertThat(response.statusCode).isEqualTo(HttpResponseStatus.NOT_FOUND.code())
      }

      @Test
      @DisplayName("request should fail with Not Found error")
      fun failsWithNotFoundError() {
        val response = handler.handleRequest(ApiRequest(mockContext, validAccounts["api"]))

        Assertions.assertThat(response.body.get().javaClass).isEqualTo(Error::class.java)
        val responseBody = response.body.get() as Error
        Assertions.assertThat(responseBody.code).isEqualTo(ErrorDetails.RESOURCE_NOT_FOUND.name)
        Assertions.assertThat(responseBody.message).isEqualTo(ErrorDetails.RESOURCE_NOT_FOUND.message)
      }
    }
  }

  private fun getResourceList(path: String): List<String> {
    val stream = UpdateAccountHandlerTests::class.java.getResourceAsStream(path) ?: return emptyList()
    return stream.bufferedReader().use { it.readLines() }
  }

  private fun readResource(path : String): String {
    return UpdateAccountHandlerTests::class.java.getResource(path).readText()
  }

}
