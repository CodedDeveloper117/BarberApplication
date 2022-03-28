package com.ammar.vendorapp.authentication.ui.email

import com.ammar.vendorapp.authentication.common.data.TextFieldState
import com.ammar.vendorapp.authentication.data.models.TokenResponse
import com.ammar.vendorapp.authentication.data.models.UserSignupResponse
import com.ammar.vendorapp.authentication.ui.login.LoginState
import kotlinx.coroutines.flow.MutableStateFlow

data class EmailState(
    val email: TextFieldState = TextFieldState(),
    val data: UserSignupResponse? = null,
    val loading: Boolean = false,
    val error: String = ""
)

fun MutableStateFlow<EmailState>.changeEmailState(value: String?, error: String?) {
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

fun MutableStateFlow<EmailState>.changeState(
    data: UserSignupResponse? = null,
    error: String = "",
    loading: Boolean? = null
) {
    this.value = this.value.copy(
        data = data ?: value.data,
        error = error,
        loading = loading ?: value.loading
    )
}
