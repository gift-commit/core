package me.spradling.gift.core.database.models

data class Account constructor(
    val accountId: String,
    val groupId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)