package me.spradling.gift.core.database.models

data class Item constructor(
    val itemId: String,
    val accountId: String,
    val event: String,
    val claimed: Boolean,
    val name: String,
    val url: String?,
    val price: Double,
    val notes: String?
)