package com.ammar.vendorapp.authentication.domain.entities

data class GoogleUser(
    val id: String,
    val email: String,
    val username: String,
    val fullName: String,
    val photoUrl: String,
    val phone: String? = null
)
