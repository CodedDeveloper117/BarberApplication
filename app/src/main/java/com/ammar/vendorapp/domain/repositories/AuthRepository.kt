package com.ammar.vendorapp.domain.repositories

import com.ammar.vendorapp.domain.entities.User
import kotlinx.coroutines.flow.Flow
import com.ammar.vendorapp.common.utils.Result


interface AuthRepository {

    fun loginWithGoogle(user: User): Flow<Result<User>>

    fun register(user: User): Flow<Result<User>>
}