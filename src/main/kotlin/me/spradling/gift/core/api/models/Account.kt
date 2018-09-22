package me.spradling.gift.core.api.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.mindrot.jbcrypt.BCrypt

class Account @JsonCreator constructor(
    @JsonProperty("group")
    val groupId: String?,
    @JsonProperty("firstName")
    val firstName: String?,
    @JsonProperty("lastName")
    val lastName: String?,
    @JsonProperty("email")
    val email: String?,
    @JsonProperty("password")
    password: String?
) {
  val password : String? = BCrypt.hashpw(password, BCrypt.gensalt())
}