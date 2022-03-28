package com.ammar.vendorapp.authentication.ui.change_password

import com.ammar.vendorapp.authentication.common.data.TextFieldState
import com.ammar.vendorapp.authentication.data.models.TokenResponse
import com.ammar.vendorapp.authentication.data.models.UserSignupResponse
import com.ammar.vendorapp.authentication.ui.login.LoginState
import kotlinx.coroutines.flow.MutableStateFlow

data class ChangePasswordState(
    val password: TextFieldState = TextFieldState(),
    val retypePassword: TextFieldState = TextFieldState(),
    val data: UserSignupResponse? = null,
    val error: String = "",
    val loading: Boolean = false
)

fun MutableStateFlow<ChangePasswordState>.changeRetypePassword(value: String?, error: String?) {
    value?.let {
        this.value = this.value.copy(
            retypePassword = this.value.retypePassword.copy(value = value)
        )
    }
    error?.let {
        this.value = this.value.copy(
            retypePassword = this.value.retypePassword.copy(error = error)
        )
    }
}

fun MutableStateFlow<ChangePasswordState>.changePasswordState(value: String?, error: String?) {
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

fun MutableStateFlow<ChangePasswordState>.changeState(
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
