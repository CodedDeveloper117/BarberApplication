package com.ammar.vendorapp.authentication.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserRegister(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val username: String
)
