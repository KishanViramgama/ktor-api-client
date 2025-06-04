package response.register

import kotlinx.serialization.Serializable
import response.alluser.UserItem

@Serializable
data class RegisterResponse(val status: Boolean, val message: String, val userItem: UserItem)