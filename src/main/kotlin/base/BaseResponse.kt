package base

import kotlinx.serialization.Serializable

@Serializable
open class BaseResponse(
    val status: Boolean,
    val message: String
)