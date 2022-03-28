package com.ammar.vendorapp.authentication.common.utils

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