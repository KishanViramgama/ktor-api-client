package response.alluser

import kotlinx.serialization.Serializable

@Serializable
data class UserItem(val id: String,val name: String, val email: String, val password: String, var image: String)