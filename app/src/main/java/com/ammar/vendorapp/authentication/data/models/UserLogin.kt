package com.ammar.vendorapp.authentication.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserLogin(
    val identifier: String,
    val password: String
)
