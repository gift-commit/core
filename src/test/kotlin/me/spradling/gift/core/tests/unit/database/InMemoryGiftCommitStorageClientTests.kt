package me.spradling.gift.core.tests.unit.database

import io.vertx.core.Future
import me.spradling.gift.core.api.models.exceptions.ResourceNotFoundException
import me.spradling.gift.core.database.memory.InMemoryGiftCommitStorageClient
import me.spradling.gift.core.database.models.Account
import me.spradling.gift.core.database.models.Item
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch

@DisplayName("When I use the InMemoryGiftCommitStorageClient,")
class InMemoryGiftCommitStorageClientTests {

  private val storageClient = InMemoryGiftCommitStorageClient()
  private val account = Account("123",
      "321",
      "Test",
      "User",
      "test@gmail.com",
      "password")
  private val item = Item("321",
      "123",
      "Christmas",
      true,
      "PlayStation 4",
      "www.amazon.com",
      199.99,
      "So awesome!")

  @Nested
  @DisplayName("to create an account,")
  inner class CreateAccount {

    @Test
    @DisplayName("the account should be accessible")
    fun accountIsSuccessfullyCreated() {
      storageClient.createAccount(account).wait()
      assertThat(storageClient.accounts[account.accountId]).isEqualTo(account)
    }
  }

  @Nested
  @DisplayName("to get an account,")
  inner class GetAccount {

    @BeforeEach
    fun setup() {
      storageClient.createAccount(account)
    }

    @Test
    @DisplayName("the retrieved account should be expected")
    fun accountIsExpected() {
      val retrievedAccount = storageClient.getAccount(account.accountId).wait().result()
      assertThat(retrievedAccount).isEqualTo(account)
    }

    @Test
    @DisplayName("retrieving an account that doesn't exist throws Not Found exception")
    fun throwsNotFoundException() {
      val response = storageClient.getAccount("DOES NOT EXIST").wait()

      verifyFutureFailed(response, ResourceNotFoundException::class.java)
    }
  }

  @Nested
  @DisplayName("to update an account,")
  inner class UpdateAccount {

    @BeforeEach
    fun setup() {
      storageClient.createAccount(account)
    }

    @Test
    @DisplayName("the expected account should be updated")
    fun expectedAccountIsUpdated() {
      val updatedAccount = account.copy(firstName = "Updated", lastName = "Name")
      storageClient.updateAccount(account.accountId, updatedAccount)


      val storedAccount = storageClient.accounts[account.accountId]!!
      assertThat(storedAccount.firstName).isEqualTo("Updated")
      assertThat(storedAccount.lastName).isEqualTo("Name")

    }
  }

  @Nested
  @DisplayName("to delete an account,")
  inner class DeleteAccount {

    @BeforeEach
    fun setup() {
      storageClient.createAccount(account)
    }

    @Test
    @DisplayName("the expected account should be deleted")
    fun expectedAccountIsUpdated() {
      storageClient.deleteAccount(account.accountId)

      assertThat(storageClient.accounts[account.accountId]).isNull()
    }

    @Test
    @DisplayName("deleting an nonexistent account throws expected GiftCommitException")
    fun deletingNonexistentAccountThrows() {
      val response = storageClient.deleteAccount("DOES NOT EXIST").wait()

      verifyFutureFailed(response, ResourceNotFoundException::class.java)
    }
  }

  @Nested
  @DisplayName("to create an item,")
  inner class CreateItem {

    @Test
    @DisplayName("the item should be accessible")
    fun itemIsSuccessfullyCreated() {
      storageClient.createItem(item).wait()
      assertThat(storageClient.items[item.itemId]).isEqualTo(item)
    }
  }

  @Nested
  @DisplayName("to get an item,")
  inner class GetItem {

    @BeforeEach
    fun setup() {
      storageClient.createItem(item)
    }

    @Test
    @DisplayName("the retrieved item should be expected")
    fun itemIsExpected() {
      val retrievedItem = storageClient.getItem(item.itemId).wait().result()
      assertThat(retrievedItem).isEqualTo(item)
    }

    @Test
    @DisplayName("retrieving an item that doesn't exist throws Not Found exception")
    fun throwsNotFoundException() {
      val response = storageClient.getItem("DOES NOT EXIST").wait()

      verifyFutureFailed(response, ResourceNotFoundException::class.java)
    }
  }

  @Nested
  @DisplayName("to update an item,")
  inner class UpdateItem {

    @BeforeEach
    fun setup() {
      storageClient.createItem(item)
    }

    @Test
    @DisplayName("the expected item should be updated")
    fun expectedItemIsUpdated() {
      val updatedItem = item.copy(price = 299.99, notes = "Kinda expensive!!")
      storageClient.updateItem(item.itemId, updatedItem)


      val storedItem = storageClient.items[item.itemId]!!
      assertThat(storedItem.price).isEqualTo(299.99)
      assertThat(storedItem.notes).isEqualTo("Kinda expensive!!")
    }
  }

  @Nested
  @DisplayName("to delete an item,")
  inner class DeleteItem {

    @BeforeEach
    fun setup() {
      storageClient.createItem(item)
    }

    @Test
    @DisplayName("the expected item should be deleted")
    fun expectedItemIsUpdated() {
      storageClient.deleteItem(item.itemId)

      assertThat(storageClient.items[item.itemId]).isNull()
    }

    @Test
    @DisplayName("deleting an nonexistent item throws expected GiftCommitException")
    fun deletingNonexistentItemThrows() {
      val response = storageClient.deleteItem("DOES NOT EXIST").wait()

      verifyFutureFailed(response, ResourceNotFoundException::class.java)
    }
  }

  fun verifyFutureFailed(response: Future<out Any>, exceptionClass: Class<out Exception>) {
    assertThat(response.failed()).isEqualTo(true)
    assertThat(response.cause()).isInstanceOf(exceptionClass)
  }

  fun <T> Future<T>.wait(): Future<T> {
    val countDownLatch = CountDownLatch(1)

    this.setHandler { _ ->
      countDownLatch.countDown()
    }

    countDownLatch.await()

    return this
  }
}