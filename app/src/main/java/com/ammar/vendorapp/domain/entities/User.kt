package com.ammar.vendorapp.domain.entities

data class User(
    val id: String,
    val email: String,
    val username: String,
    val fullName: String,
    val photoUrl: String,
    val phone: String? = null
)
