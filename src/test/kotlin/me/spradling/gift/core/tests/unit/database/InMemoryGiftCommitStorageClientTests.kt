package me.spradling.gift.core.tests.unit.database

import com.fasterxml.jackson.core.type.TypeReference
import io.vertx.core.json.Json
import me.spradling.gift.core.api.extensions.wait
import me.spradling.gift.core.api.models.exceptions.ResourceNotFoundException
import me.spradling.gift.core.database.memory.InMemoryGiftCommitStorageClient
import me.spradling.gift.core.database.models.Account
import me.spradling.gift.core.database.models.Item
import me.spradling.gift.core.tests.unit.UnitTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("When I use the InMemoryGiftCommitStorageClient,")
class InMemoryGiftCommitStorageClientTests : UnitTestBase() {

  private val storageClient = InMemoryGiftCommitStorageClient()
  private val account = Json.mapper.readValue(readResource("/data/valid/database/account.json"), Account::class.java)
  private val accounts: List<Account> = Json.mapper.readValue(readResource("/data/valid/database/accounts.json"), object: TypeReference<List<Account>>() {})
  private val item = Json.mapper.readValue(readResource("/data/valid/database/item.json"), Item::class.java)

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

      verifyFailedFuture(response, ResourceNotFoundException::class.java)
    }
  }

  @Nested
  @DisplayName("to get multiple accounts,")
  inner class GetAccounts {

    @BeforeEach
    fun setup() {
      accounts.forEach { account -> storageClient.createAccount(account) }
    }

    @Test
    @DisplayName("the retrieved accounts should be expected")
    fun accountIsExpected() {
      val retrievedAccounts = storageClient.getAccounts(null).wait().result()
      assertThat(retrievedAccounts).isEqualTo(accounts)
    }

    @Test
    @DisplayName("retrieving an invalid number of accounts throws Not Found exception")
    fun throwsNotFoundException() {
      val response = storageClient.getAccounts(-1).wait()

      verifyFailedFuture(response, ResourceNotFoundException::class.java)
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

      verifyFailedFuture(response, ResourceNotFoundException::class.java)
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

      verifyFailedFuture(response, ResourceNotFoundException::class.java)
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

      verifyFailedFuture(response, ResourceNotFoundException::class.java)
    }
  }
}