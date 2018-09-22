package me.spradling.gift.core.database.aws

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName
import me.spradling.gift.core.api.models.configuration.StorageClient
import me.spradling.gift.core.database.models.Account
import me.spradling.gift.core.database.models.Item
import java.sql.DriverManager

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
    val url = MySqlJdbcConnection(this.host, this.instance, this.useSsl).build()
    DriverManager.getConnection(url, this.user, this.password)
  }

  private val accountTable = "account"
  private val itemTable = "item"

  init {
    Class.forName("com.mysql.jdbc.Driver").newInstance()
  }

  override fun createAccount(account: Account) {
    val createQuery = "INSERT INTO $accountTable VALUES (${account.accountId}," +
                                                        "${account.groupId}," +
                                                        "${account.firstName}," +
                                                        "${account.lastName}," +
                                                        "${account.email}," +
                                                        "${account.password})"

    connection.createStatement().executeQuery(createQuery)
  }

  override fun getAccount(accountId: String): Account {
    TODO("not implemented")
  }

  override fun updateAccount(accountId: String, updatedAccount: Account) {
    TODO("not implemented")
  }

  override fun deleteAccount(accountId: String) {
    TODO("not implemented")
  }

  override fun createItem(item: Item) {
    TODO("not implemented")
  }

  override fun getItem(itemId: String): Item {
    TODO("not implemented")
  }

  override fun getAccountItems(accountId: String): List<Item> {
    TODO("not implemented")
  }

  override fun updateItem(itemId: String, updatedItem: Item) {
    TODO("not implemented")
  }

  override fun deleteItem(itemId: String) {
    TODO("not implemented")
  }

}