package com.ammar.vendorapp.authentication.ui.email

import android.util.Patterns

sealed class EmailEvents {
    data class ChangeEmail(val email: String) : EmailEvents() {
        val error: String
            get() = if (Patterns.EMAIL_ADDRESS.matcher(email)
                    .matches()
            ) "" else "Email address not valid"
    }

    object ValidateUser : EmailEvents()
    object ForgotPassword : EmailEvents()
}
