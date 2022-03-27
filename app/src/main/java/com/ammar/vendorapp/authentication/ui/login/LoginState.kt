package com.ammar.vendorapp.authentication.ui.login

import com.ammar.vendorapp.authentication.common.data.TextFieldState
import com.ammar.vendorapp.authentication.domain.entities.User

data class LoginState(
    val data: User? = null,
    val loading: Boolean = false,
    val error: String = "",
    val emailTextField: TextFieldState = TextFieldState(),
    val passwordTextField: TextFieldState = TextFieldState()
)
