package me.spradling.gift.core.tests.unit.conversion

import io.vertx.core.Future
import io.vertx.core.json.Json
import me.spradling.gift.core.api.models.Account
import me.spradling.gift.core.conversion.GiftCommitStorageConverter
import me.spradling.gift.core.database.memory.InMemoryGiftCommitStorageClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch

@DisplayName("When the GiftCommitStorageConverter is used")
class GiftCommitStorageConverterTests {

  private val storageClient = InMemoryGiftCommitStorageClient()
  private val converter = GiftCommitStorageConverter(storageClient)

  private val account = Json.mapper.readValue(this::class.java.classLoader.getResource("data/valid/account/api.json")
                                                                          .readText(),
                                      Account::class.java)

  @Nested
  @DisplayName("to convert")
  inner class Convert {

    @Nested
    @DisplayName("an Account")
    inner class Accounts {

      @Test
      @DisplayName("the conversion should result in the expected Account")
      fun expectedAccountConversion() {

        val convertedAccount = converter.convert(account)

        assertThat(convertedAccount.accountId).isNotNull()
        assertThat(convertedAccount.groupId).isEqualTo(account.groupId)
        assertThat(convertedAccount.firstName).isEqualTo(account.firstName)
        assertThat(convertedAccount.lastName).isEqualTo(account.lastName)
        assertThat(convertedAccount.email).isEqualTo(account.email)
        assertThat(convertedAccount.password).isNotNull()
      }
    }
  }

  @Nested
  @DisplayName("to merge")
  inner class Merge {

    val storageAccount = converter.convert(account)
    val updateAccount = Json.mapper.readValue(this::class.java.classLoader.getResource("data/valid/account/update.json")
                                                                          .readText(),
                                              Account::class.java)
    @BeforeEach
    fun setup() {
      storageClient.createAccount(storageAccount)
    }

    @Nested
    @DisplayName("an Account")
    inner class Accounts {

      @Test
      @DisplayName("the merge should result in the expected Account")
      fun expectedAccountMerge() {

        val mergedAccount = converter.merge(storageAccount.accountId, updateAccount).wait()

        assertThat(mergedAccount.accountId).isNotNull()
        assertThat(mergedAccount.groupId).isEqualTo(updateAccount.groupId ?: storageAccount.groupId)
        assertThat(mergedAccount.firstName).isEqualTo(updateAccount.firstName ?: storageAccount.firstName)
        assertThat(mergedAccount.lastName).isEqualTo(updateAccount.lastName ?: storageAccount.lastName)
        assertThat(mergedAccount.email).isEqualTo(updateAccount.email ?: storageAccount.email)
        assertThat(mergedAccount.password).isNotNull()
      }
    }
  }

  fun <T> Future<T>.wait() : T {
    val countDownLatch = CountDownLatch(1)

    this.setHandler { _ ->
      countDownLatch.countDown()
    }

    countDownLatch.await()

    return this.result()
  }
}