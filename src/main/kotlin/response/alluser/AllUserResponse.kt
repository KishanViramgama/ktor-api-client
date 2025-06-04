package response.alluser

import kotlinx.serialization.Serializable
import response.alluser.UserItem

@Serializable
data class AllUserResponse(val status: Boolean, val message: String, val data: List<UserItem>)