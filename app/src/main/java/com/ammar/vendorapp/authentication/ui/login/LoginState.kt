package com.ammar.vendorapp.authentication.ui.login

import com.ammar.vendorapp.authentication.common.data.TextFieldState
import com.ammar.vendorapp.authentication.data.models.TokenResponse
import kotlinx.coroutines.flow.MutableStateFlow

data class LoginState(
    val data: TokenResponse? = null,
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

fun MutableStateFlow<LoginState>.changeState(
    data: TokenResponse? = null,
    error: String = "",
    loading: Boolean? = null
) {
    this.value = this.value.copy(
        data = data ?: value.data,
        error = error,
        loading = loading ?: value.loading
    )
}