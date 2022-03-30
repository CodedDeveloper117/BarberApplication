package com.ammar.vendorapp.authentication.ui.otp

import com.ammar.vendorapp.authentication.data.models.TokenResponse
import kotlinx.coroutines.flow.MutableStateFlow

data class OtpState(
    val data: TokenResponse? = null,
    val loading: Boolean = false,
    val error: String = "",
    val otp: String = "",
    val key: String = "",
    val email: String = ""
)

fun MutableStateFlow<OtpState>.changeState(
    data: TokenResponse? = null,
    loading: Boolean? = null,
    error: String = "",
    otp: String? = null,
    key: String? = null,
    email: String? = null
) {
    value = value.copy(
        data = data ?: value.data,
        loading = loading ?: value.loading,
        error = error,
        otp = otp ?: value.otp,
        key = key ?: value.key,
        email = email ?: value.email
    )
}