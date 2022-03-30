package com.ammar.vendorapp.authentication.common.data

data class TextFieldState(
    val value: String = "",
    val error: String = "blank"
)

fun TextFieldState.changeState(value: String?, error: String?): TextFieldState {
    return TextFieldState(
        value = if(value.isNullOrBlank()) this.value else value,
        error = if(error.isNullOrBlank()) "" else error,
    )
}
