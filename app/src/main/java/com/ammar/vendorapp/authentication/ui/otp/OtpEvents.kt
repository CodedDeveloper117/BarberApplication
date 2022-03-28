package com.ammar.vendorapp.authentication.ui.otp

sealed class OtpEvents {
    data class ChangeOtp(val otp: Int) : OtpEvents()
    object VerifyOtp: OtpEvents()
    data class ResendOtp(val email: String): OtpEvents()
    data class ChangeKey(val key: String): OtpEvents()
}