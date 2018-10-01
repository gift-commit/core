package me.spradling.gift.core.tests.unit

import io.vertx.core.Future
import io.vertx.core.json.Json
import io.vertx.ext.web.RoutingContext
import me.spradling.gift.core.api.models.Account
import me.spradling.gift.core.api.models.Item
import me.spradling.gift.core.api.models.errors.Error
import me.spradling.gift.core.api.models.errors.ErrorDetails
import me.spradling.gift.core.api.models.responses.ApiResponse
import me.spradling.gift.core.conversion.GiftCommitApiConverter
import me.spradling.gift.core.conversion.GiftCommitStorageConverter
import me.spradling.gift.core.database.memory.InMemoryGiftCommitStorageClient
import me.spradling.gift.core.tests.unit.api.UpdateAccountHandlerTests
import org.assertj.core.api.Assertions
import org.mockito.Mockito
import java.io.File

open class UnitTestBase {

  companion object {
    @JvmStatic
    private val ValidFilePath = "/data/valid"
    @JvmStatic
    private val InvalidFilePath = "/data/invalid"
  }

  protected val mockContext = Mockito.mock(RoutingContext::class.java)!!
  protected val inMemoryStorageClient = InMemoryGiftCommitStorageClient()
  protected val storageConverter = GiftCommitStorageConverter(inMemoryStorageClient)
  protected val apiConverter = GiftCommitApiConverter()
  protected val validAccounts = hashMapOf<String, Account>()
  protected val invalidAccounts = hashMapOf<String, Account>()
  protected val validItems = hashMapOf<String, Item>()
  protected val invalidItems = hashMapOf<String, Item>()

  init {
    getResourceList("$ValidFilePath/account").forEach { fileName ->
      val resource = readResource("$ValidFilePath/account/$fileName")
      validAccounts[File(fileName).nameWithoutExtension] = Json.mapper.readValue(resource, Account::class.java)
    }

    getResourceList("$InvalidFilePath/account").forEach { fileName ->
      val resource = readResource("$InvalidFilePath/account/$fileName")
      invalidAccounts[File(fileName).nameWithoutExtension] = Json.mapper.readValue(resource, Account::class.java)
    }
    getResourceList("$ValidFilePath/item").forEach { fileName ->
      val resource = readResource("$ValidFilePath/item/$fileName")
      validItems[File(fileName).nameWithoutExtension] = Json.mapper.readValue(resource, Item::class.java)
    }
    getResourceList("$InvalidFilePath/item").forEach { fileName ->
      val resource = readResource("$InvalidFilePath/item/$fileName")
      validItems[File(fileName).nameWithoutExtension] = Json.mapper.readValue(resource, Item::class.java)
    }
  }

  protected fun getResourceList(path: String): List<String> {
    val stream = UpdateAccountHandlerTests::class.java.getResourceAsStream(path) ?: return emptyList()
    return stream.bufferedReader().use { it.readLines() }
  }

  protected fun readResource(path : String): String {
    return UpdateAccountHandlerTests::class.java.getResource(path).readText()
  }

  fun verifyErrorResponse(response : ApiResponse, details: ErrorDetails) {
    Assertions.assertThat(response.body.get().javaClass).isEqualTo(Error::class.java)
    val responseBody = response.body.get() as Error
    Assertions.assertThat(responseBody.code).isEqualTo(details.name)
    Assertions.assertThat(responseBody.message).isEqualTo(details.message)
  }

  fun verifyFailedFuture(response: Future<out Any>, exceptionClass: Class<out Exception>) {
    Assertions.assertThat(response.failed()).isEqualTo(true)
    Assertions.assertThat(response.cause()).isInstanceOf(exceptionClass)
  }
}