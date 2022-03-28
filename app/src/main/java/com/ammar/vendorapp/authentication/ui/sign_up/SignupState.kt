package com.ammar.vendorapp.authentication.ui.sign_up

import com.ammar.vendorapp.authentication.common.data.TextFieldState
import com.ammar.vendorapp.authentication.common.data.changeState
import com.ammar.vendorapp.authentication.domain.entities.User
import com.ammar.vendorapp.authentication.ui.login.LoginState
import kotlinx.coroutines.flow.MutableStateFlow

data class SignupState(
    val data: User? = null,
    val loading: Boolean = false,
    val error: String = "",
    val email: TextFieldState = TextFieldState(),
    val password: TextFieldState = TextFieldState(),
    val username: TextFieldState = TextFieldState(),
    val lastname: TextFieldState = TextFieldState(),
    val firstname: TextFieldState = TextFieldState(),
)

fun MutableStateFlow<SignupState>.changeEmailState(value: String?, error: String?) {
    this.value = this.value.copy(
        email = this.value.email.changeState(value, error)
    )
}

fun MutableStateFlow<SignupState>.changePasswordState(value: String?, error: String?) {
    this.value = this.value.copy(
        password = this.value.password.changeState(value, error)
    )
}

fun MutableStateFlow<SignupState>.changeUsername(value: String?, error: String?) {
    this.value = this.value.copy(
        username = this.value.username.changeState(value, error)
    )
}

fun MutableStateFlow<SignupState>.changeLastName(value: String?, error: String?) {
    this.value = this.value.copy(
        lastname = this.value.lastname.changeState(value, error)
    )
}

fun MutableStateFlow<SignupState>.changeFirstName(value: String?, error: String?) {
    this.value = this.value.copy(
        firstname = this.value.firstname.changeState(value, error)
    )
}

fun SignupState.isValid(): Boolean {
    if(
        email.error.isNotBlank() && password.error.isNotBlank() && firstname.error.isNotBlank()
        && username.error.isNotBlank() && lastname.error.isNotBlank()
    ) return true
    return false
}

fun MutableStateFlow<SignupState>.changeState(data: User?, error: String?, loading: Boolean?) {
    this.value = this.value.copy(
        data = data,
        error = if(!error.isNullOrEmpty()) error else "",
        loading = loading ?: false
    )
}
