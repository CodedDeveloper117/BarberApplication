package com.ammar.vendorapp.authentication.data.repositories

import com.ammar.vendorapp.authentication.common.utils.Result
import com.ammar.vendorapp.authentication.data.api.Either
import com.ammar.vendorapp.authentication.data.api.UserAuthenticationApi
import com.ammar.vendorapp.authentication.data.models.*
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

    override fun register(user: UserRegister): Flow<Result<UserResponse<UserSignupResponse>>> = flow {
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

    override fun verifyOtp(verificationKey: String, otp: Int): Flow<Result<TokenResponse>> = flow {
        emit(Result.Loading())
        when(val data = api.verifyOtp(verificationKey, otp)) {
            is Either.Success -> {
                emit(Result.Success(data.response.data))
            }
            is Either.Failure -> {
                emit(Result.Error(data.error.message ?: ""))
            }
        }
    }

    override fun resendOtp(email: String): Flow<Result<UserResponse<UserSignupResponse>>> = flow {
        emit(Result.Loading())
        when(val data = api.resendOtp(email)) {
            is Either.Success -> {
                emit(Result.Success(data.response))
            }
            is Either.Failure -> {
                emit(Result.Error(data.error.message ?: ""))
            }
        }
    }

    override fun forgotPassword(email: String): Flow<Result<UserResponse<UserSignupResponse>>> = flow {
        emit(Result.Loading())
        when(val data = api.forgotPassword(email)) {
            is Either.Success -> {
                emit(Result.Success(data.response))
            }
            is Either.Failure -> {
                emit(Result.Error(data.error.message ?: ""))
            }
        }
    }

    override fun resetPassword(
        password: String,
        token: String
    ): Flow<Result<UserResponse<UserSignupResponse>>> = flow{
        emit(Result.Loading())
        when(val data = api.resetPassword(password, token)) {
            is Either.Success -> {
                emit(Result.Success(data.response))
            }
            is Either.Failure -> {
                emit(Result.Error(data.error.message ?: ""))
            }
        }
    }

    override fun validateUser(email: String): Flow<Result<UserResponse<UserSignupResponse>>> = flow {
        emit(Result.Loading())
        when(val data = api.validateUser(email)) {
            is Either.Success -> {
                emit(Result.Success(data.response))
            }
            is Either.Failure -> {
                emit(Result.Error(data.error.message ?: ""))
            }
        }
    }
}