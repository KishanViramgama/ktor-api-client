package response.login

import kotlinx.serialization.Serializable
import response.alluser.UserItem

@Serializable
data class LoginResponse(val status: Boolean, val message: String,val data: UserItem)
