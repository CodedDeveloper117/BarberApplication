package com.ammar.vendorapp.authentication.common.utils

import `in`.aabhasjindal.otptextview.OTPListener
import `in`.aabhasjindal.otptextview.OtpTextView
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Matcher
import java.util.regex.Pattern

fun TextInputEditText.onChange(callback: (String) -> Unit) {
    this.addTextChangedListener(object: TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(value: Editable?) {
            callback(value.toString())
        }
    })
}

fun OtpTextView.onChange(callback: (String) -> Unit) {
    this.otpListener = object: OTPListener {
        override fun onInteractionListener() {

        }

        override fun onOTPComplete(otp: String) {
            callback(otp)
        }
    }
}

fun String.isValid(): Boolean {
    val pattern: Pattern
    val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
    pattern = Pattern.compile(passwordPattern)
    val matcher: Matcher = pattern.matcher(this)
    return matcher.matches() && this.length >= 8
}

fun TextInputLayout.setCustomErrors(error: String) {
    if (error.isNotBlank() && error != "blank") {
        this.isErrorEnabled = true
        this.error = error
    } else {
        this.isErrorEnabled = false
    }
}