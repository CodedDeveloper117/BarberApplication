package com.ammar.vendorapp.authentication.data.repositories

import com.ammar.vendorapp.authentication.common.utils.Result
import com.ammar.vendorapp.authentication.data.api.Either
import com.ammar.vendorapp.authentication.data.api.UserAuthenticationApi
import com.ammar.vendorapp.authentication.data.models.UserLogin
import com.ammar.vendorapp.authentication.data.models.UserRegister
import com.ammar.vendorapp.authentication.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: UserAuthenticationApi
): AuthRepository {

    override fun login(user: UserLogin): Flow<Result<Any>> = flow {
        emit(Result.Loading())
        when(val data = api.login(user)) {
            is Either.Success -> {
                emit(Result.Success(data.response))
            }
            is Either.Failure -> {
                emit(Result.Error(data.error.message!!))
            }
        }
    }

    override fun register(user: UserRegister): Flow<Result<Any>> = flow {
        emit(Result.Loading())
        when(val data = api.register(user)) {
            is Either.Success -> {
                emit(Result.Success(data.response))
            }
            is Either.Failure -> {
                emit(Result.Error(data.error.message!!))
            }
        }
    }
}