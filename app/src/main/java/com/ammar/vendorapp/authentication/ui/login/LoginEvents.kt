package com.ammar.vendorapp.authentication.ui.login

import android.util.Patterns
import com.ammar.vendorapp.authentication.common.utils.isValid
import com.ammar.vendorapp.authentication.data.models.UserLogin
import java.util.regex.Matcher
import java.util.regex.Pattern

sealed class LoginEvents {
    object Login : LoginEvents()

    data class ChangeEmail(val email: String) : LoginEvents() {
        val error: String
        get() = if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) "" else "Email address not valid"

    }

    data class ChangePassword(val password: String) : LoginEvents() {
        val error: String
            get() = if (password.isValid()) "" else "Password must contain at least 1 Number, 1 special character, 1 uppercase letter and must be at least 8 characters long"

    }
}