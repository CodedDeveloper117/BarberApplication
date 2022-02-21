package com.ammar.vendorapp.ui.sign_up

import com.ammar.vendorapp.domain.entities.User

data class SignupState(
    val data: User? = null,
    val loading: Boolean = false,
    val error: String = ""
)
