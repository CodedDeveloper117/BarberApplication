package com.ammar.vendorapp.common.data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val _id: String,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val isValidated: Boolean,
    val DOB: String,
    val gender: String,
    val address: String
): java.io.Serializable
