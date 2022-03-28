package com.ammar.vendorapp.authentication.ui.change_password

import com.ammar.vendorapp.authentication.common.utils.isValid

sealed class ChangePasswordEvents {
    data class ChangePassword(val value: String): ChangePasswordEvents() {
        val error: String
            get() = if (value.isValid()) "" else "Password must contain at least 1 Number, 1 special character, 1 uppercase letter and must be at least 8 characters long"
    }
    data class ChangeRetypePassword(val value: String, val password: String): ChangePasswordEvents() {
        val error: String
            get() = if(!password.isValid()) "Password is not valid" else if(value != password) "Password not the same" else ""
    }
    data class Continue(val token: String): ChangePasswordEvents()
}