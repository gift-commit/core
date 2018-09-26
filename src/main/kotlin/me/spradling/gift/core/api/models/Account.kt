package me.spradling.gift.core.api.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.mindrot.jbcrypt.BCrypt

@JsonIgnoreProperties(ignoreUnknown = true)
class Account {
  @JsonProperty("groupId")
  val groupId: String?

  @JsonProperty("firstName")
  val firstName: String?

  @JsonProperty("lastName")
  val lastName: String?

  @JsonProperty("email")
  val email: String?

  @JsonProperty("password")
  val password: String?

  @JsonCreator
  constructor(
      @JsonProperty("groupId")
      groupId: String?,
      @JsonProperty("firstName")
      firstName: String?,
      @JsonProperty("lastName")
      lastName: String?,
      @JsonProperty("email")
      email: String?,
      @JsonProperty("password")
      password: String?
  ) {
    this.groupId = groupId
    this.firstName = firstName
    this.lastName = lastName
    this.email = email
    this.password = BCrypt.hashpw(password, BCrypt.gensalt())
  }

  constructor(groupId: String?, firstName: String?, lastName: String?, email: String?) {
    this.groupId = groupId
    this.firstName = firstName
    this.lastName = lastName
    this.email = email
    this.password = null
  }

}
