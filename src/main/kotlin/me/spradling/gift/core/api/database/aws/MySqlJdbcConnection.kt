package me.spradling.gift.core.api.database.aws

class MySqlJdbcConnection(private val host: String,
                          private val database: String,
                          private val useSsl: Boolean) {

  fun build() : String {
    return "jdbc:mysql://$host/$database?useSSL=$useSsl"
  }
}