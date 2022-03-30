package com.ammar.vendorapp.authentication.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserError(
    val code: Int,
    val data: String,
    val status: String
)

val defaultError = UserError(400, "An Unknown Error Occurred, Please try again later", "Error")