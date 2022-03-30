package com.ammar.vendorapp.authentication.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserOtpRequest(
    val otp: Int,
    val verificationKey: String
)

@Serializable
data class UserEmailRequest(
    val email: String
)

@Serializable
data class UserPasswordRequest(
    val password: String
)