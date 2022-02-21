package com.ammar.vendorapp.ui.login

import com.ammar.vendorapp.domain.entities.User

data class LoginState(
    val data: User? = null,
    val loading: Boolean = false,
    val error: String = ""
)
