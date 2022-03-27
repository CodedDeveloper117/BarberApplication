package com.ammar.vendorapp.authentication.ui.sign_up

import com.ammar.vendorapp.authentication.data.models.UserRegister

sealed class SignupEvents {
    data class Register(val email: String, val password: String, val firstName: String, val lastName: String, val username: String): SignupEvents() {
        val user = UserRegister(
            email, password, firstName, lastName, username
        )
    }
}
