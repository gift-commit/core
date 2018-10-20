package me.spradling.gift.core.database.aws

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName
import io.vertx.core.Future
import me.spradling.gift.core.api.models.configuration.StorageClient
import me.spradling.gift.core.api.models.exceptions.ResourceNotFoundException
import me.spradling.gift.core.database.models.Account
import me.spradling.gift.core.database.models.Item
import java.sql.DriverManager
import java.sql.SQLException

@JsonTypeName("RDS")
class RDSGiftCommitStorageClient @JsonCreator constructor(
    @JsonProperty("host")
    host: String,
    @JsonProperty("instance")
    instance: String,
    @JsonProperty("useSsl")
    useSsl: Boolean,
    @JsonProperty("user")
    user: String,
    @JsonProperty("password")
    password: String) :
    StorageClient(host, instance, useSsl, user, password) {

  private val connection by lazy {
    val url = MySqlConnection(this.host, this.instance, this.useSsl).build()
    DriverManager.getConnection(url, this.user, this.password)
  }

  private val accountTable = "account"
  private val itemTable = "item"

  init {
    Class.forName("com.mysql.jdbc.Driver").newInstance()
  }

  override fun createAccount(account: Account) : Future<String> {
    val createQuery = "INSERT INTO $accountTable VALUES (${account.accountId}," +
                                                        "${account.groupId}," +
                                                        "${account.firstName}," +
                                                        "${account.lastName}," +
                                                        "${account.email}," +
                                                        "${account.password})"

    connection.createStatement().executeQuery(createQuery)

    return Future.succeededFuture(account.accountId)
  }

  override fun getAccount(accountId: String): Future<Account> {
    val getQuery = "SELECT * FROM $accountTable WHERE accountId=$accountId"
    val accounts = mutableListOf<Account>()
    var resultSet = connection.createStatement().executeQuery(getQuery)

    while (resultSet.next()) {
      accounts.add(Account(resultSet.getString("accountId"),
          resultSet.getString("groupId"),
          resultSet.getString("firstName"),
          resultSet.getString("lastName"),
          resultSet.getString("email"),
          resultSet.getString("password")))
    }

    if (accounts.isEmpty()) {
      throw ResourceNotFoundException()
    }

    return Future.succeededFuture(accounts[0])
  }

  override fun listAccounts(limit: Int?): Future<List<Account>> {
    val includesLimit = if (limit != null) "LIMIT $limit" else ""
    val getQuery = "SELECT * from $accountTable $includesLimit"
    val accounts = arrayListOf<Account>()
    val resultSet = connection.createStatement().executeQuery(getQuery)

    while (resultSet.next()) {
      accounts.add(Account(resultSet.getString("accountId"),
                            resultSet.getString("groupId"),
                            resultSet.getString("firstName"),
                            resultSet.getString("lastName"),
                            resultSet.getString("email"),
                            resultSet.getString("password")))
    }

    if (accounts.isEmpty()) {
      throw ResourceNotFoundException()
    }

    return Future.succeededFuture(accounts)
  }

  override fun updateAccount(accountId: String, updatedAccount: Account) : Future<Void> {
    val updateQuery = "UPDATE $accountTable SET groupId = ${updatedAccount.firstName}," +
                                               "firstName = ${updatedAccount.firstName}" +
                                               "lastName = ${updatedAccount.lastName}" +
                                               "email = ${updatedAccount.email}" +
                                               "password = ${updatedAccount.password}" +
                                           "WHERE accountId = $accountId"

    connection.createStatement().executeQuery(updateQuery)

    return Future.succeededFuture()
  }

  override fun deleteAccount(accountId: String): Future<Void> {
    val deleteQuery = "DELETE FROM $accountTable WHERE accountId=$accountId"
    connection.createStatement().executeQuery(deleteQuery)

    return Future.succeededFuture()
  }

  override fun createItem(item: Item): Future<String> {
    val createQuery = "INSERT INTO $itemTable VALUES (${item.itemId}," +
                                                     "${item.accountId}," +
                                                     "${item.event}," +
                                                     "${item.claimedBy}," +
                                                     "${item.name}," +
                                                     "${item.url}," +
                                                     "${item.price}," +
                                                     "${item.notes})"

    connection.createStatement().executeQuery(createQuery)

    return Future.succeededFuture(item.itemId)
  }

  override fun getItem(itemId: String): Future<Item> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getAccountItems(accountId: String): Future<List<Item>> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun updateItem(itemId: String, updatedItem: Item): Future<Void> {
    val updateQuery = "UPDATE $itemTable SET itemId = ${updatedItem.itemId}," +
                                               "accountId = ${updatedItem.accountId}" +
                                               "event = ${updatedItem.event}" +
                                               "claimedBy = ${updatedItem.claimedBy}" +
                                               "name = ${updatedItem.name}" +
                                               "url = ${updatedItem.url}" +
                                               "price = ${updatedItem.price}" +
                                               "notes = ${updatedItem.notes}" +
                                           "WHERE itemId = $itemId"

    connection.createStatement().executeQuery(updateQuery)

    return Future.succeededFuture()
  }

  override fun deleteItem(itemId: String): Future<Void> {
    val deleteQuery = "DELETE FROM $itemTable WHERE itemId=$itemId"
    connection.createStatement().executeQuery(deleteQuery)

    return Future.succeededFuture()
  }
}