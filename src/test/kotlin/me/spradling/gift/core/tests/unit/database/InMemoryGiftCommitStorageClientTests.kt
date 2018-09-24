package me.spradling.gift.core.tests.unit.database

import me.spradling.gift.core.api.models.errors.ErrorDetails
import me.spradling.gift.core.api.models.exceptions.GiftCommitException
import me.spradling.gift.core.database.memory.InMemoryGiftCommitStorageClient
import me.spradling.gift.core.database.models.Account
import me.spradling.gift.core.database.models.Item
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

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
      storageClient.createAccount(account)
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
      val retrievedAccount = storageClient.getAccount(account.accountId)
      assertThat(retrievedAccount).isEqualTo(account)
    }

    @Test
    @DisplayName("retrieving an account that doesn't exist throws Not Found exception")
    fun throwsNotFoundException() {
      val exception = assertThrows(GiftCommitException::class.java) { storageClient.getAccount("DOES NOT EXIST") }

      assertThat(exception.error).isEqualTo(ErrorDetails.RESOURCE_NOT_FOUND)
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
      val exception = assertThrows(GiftCommitException::class.java) {
        storageClient.deleteAccount("DOES NOT EXIST")
      }

      assertThat(exception.error).isEqualTo(ErrorDetails.RESOURCE_NOT_FOUND)
    }
  }

  @Nested
  @DisplayName("to create an item,")
  inner class CreateItem {

    @Test
    @DisplayName("the item should be accessible")
    fun itemIsSuccessfullyCreated() {
      storageClient.createItem(item)
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
      val retrievedItem = storageClient.getItem(item.itemId)
      assertThat(retrievedItem).isEqualTo(item)
    }

    @Test
    @DisplayName("retrieving an item that doesn't exist throws Not Found exception")
    fun throwsNotFoundException() {
      val exception = assertThrows(GiftCommitException::class.java) { storageClient.getItem("DOES NOT EXIST") }

      assertThat(exception.error).isEqualTo(ErrorDetails.RESOURCE_NOT_FOUND)
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
      val exception = assertThrows(GiftCommitException::class.java) {
        storageClient.deleteItem("DOES NOT EXIST")
      }

      assertThat(exception.error).isEqualTo(ErrorDetails.RESOURCE_NOT_FOUND)
    }
  }
}