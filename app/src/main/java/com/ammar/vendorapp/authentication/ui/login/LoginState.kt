package com.ammar.vendorapp.authentication.ui.login

import com.ammar.vendorapp.authentication.common.data.TextFieldState
import com.ammar.vendorapp.authentication.domain.entities.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class LoginState(
    val data: User? = null,
    val loading: Boolean = false,
    val error: String = "",
    val email: TextFieldState = TextFieldState(),
    val password: TextFieldState = TextFieldState()
)


fun MutableStateFlow<LoginState>.changeEmailState(value: String?, error: String?) {
    value?.let {
        this.value = this.value.copy(
            email = this.value.email.copy(value = value)
        )
    }
    error?.let {
        this.value = this.value.copy(
            email = this.value.email.copy(error = error)
        )
    }
}

fun MutableStateFlow<LoginState>.changePasswordState(value: String?, error: String?) {
    value?.let {
        this.value = this.value.copy(
            password = this.value.password.copy(value = value)
        )
    }
    error?.let {
        this.value = this.value.copy(
            password = this.value.password.copy(error = error)
        )
    }
}

fun MutableStateFlow<LoginState>.changeState(data: User?, error: String?, loading: Boolean?) {
    this.value = this.value.copy(
        data = data,
        error = if(!error.isNullOrEmpty()) error else "",
        loading = loading ?: false
    )
}