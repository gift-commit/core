package me.spradling.gift.core.database.aws

class MySqlConnection(private val host: String,
                      private val database: String,
                      private val useSsl: Boolean) {

  fun build(): String {
    return "jdbc:mysql://$host/$database?useSSL=$useSsl"
  }
}