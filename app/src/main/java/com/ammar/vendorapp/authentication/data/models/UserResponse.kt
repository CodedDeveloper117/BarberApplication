package com.ammar.vendorapp.authentication.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse<T>(
    val code: Int,
    val status: String,
    val data: T
)

@Serializable
data class UserSignupResponse(
    val message: String = "",
    val verificationKey: String = ""
)

@Serializable
data class TokenResponse(
    val token: String,
    val expires: String = ""
)

