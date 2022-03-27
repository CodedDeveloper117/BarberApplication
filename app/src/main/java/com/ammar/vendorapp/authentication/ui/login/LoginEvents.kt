package com.ammar.vendorapp.authentication.ui.login

import com.ammar.vendorapp.authentication.data.models.UserLogin

sealed class LoginEvents {
    data class Login(val identifier: String, val password: String) : LoginEvents() {
        val user = UserLogin(identifier, password)
    }
}