package com.ammar.vendorapp.common.data

data class BaseState<T>(
    val loading: Boolean = false,
    val data: T? = null,
    val error: String? = null
)
