package com.ammar.vendorapp.authentication.common.data

data class BaseState<T>(
    val loading: Boolean = false,
    val data: T? = null,
    val error: String? = null
)
