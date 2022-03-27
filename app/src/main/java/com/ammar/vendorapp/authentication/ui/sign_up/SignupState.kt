package com.ammar.vendorapp.authentication.ui.sign_up

import com.ammar.vendorapp.authentication.domain.entities.User

data class SignupState(
    val data: User? = null,
    val loading: Boolean = false,
    val error: String = ""
)
