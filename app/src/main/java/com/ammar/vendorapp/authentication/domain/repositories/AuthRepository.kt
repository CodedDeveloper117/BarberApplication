package com.ammar.vendorapp.authentication.domain.repositories

import kotlinx.coroutines.flow.Flow
import com.ammar.vendorapp.authentication.common.utils.Result
import com.ammar.vendorapp.authentication.data.models.UserLogin
import com.ammar.vendorapp.authentication.data.models.UserRegister


interface AuthRepository {

    fun login(user: UserLogin): Flow<Result<Any>>

    fun register(user: UserRegister): Flow<Result<Any>>
}