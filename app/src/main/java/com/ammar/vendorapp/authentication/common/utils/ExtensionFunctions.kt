package com.ammar.vendorapp.authentication.common.utils

import `in`.aabhasjindal.otptextview.OTPListener
import `in`.aabhasjindal.otptextview.OtpTextView
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText

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