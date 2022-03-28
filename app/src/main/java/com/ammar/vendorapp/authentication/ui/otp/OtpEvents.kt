package com.ammar.vendorapp.authentication.ui.otp

sealed class OtpEvents {
    data class ChangeOtp(val otp: Int) : OtpEvents()
    object VerifyOtp: OtpEvents()
    object ResendOtp : OtpEvents()
    data class ChangeParameters(val key: String, val email: String): OtpEvents()
}