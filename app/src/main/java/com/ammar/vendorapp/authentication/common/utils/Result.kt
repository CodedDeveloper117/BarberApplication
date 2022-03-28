package com.ammar.vendorapp.authentication.common.utils

sealed class Result<T> {
    data class Success<T>(val data: T?): Result<T>()
    data class Error<T>(val message: String, val exception: Exception? = null): Result<T>()
    class Loading<T>: Result<T>()
}
