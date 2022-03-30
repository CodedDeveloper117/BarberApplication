package com.ammar.vendorapp.authentication.domain.repositories

import kotlinx.coroutines.flow.Flow
import com.ammar.vendorapp.authentication.common.utils.Result
import com.ammar.vendorapp.authentication.data.models.*
import com.ammar.vendorapp.common.data.User


interface AuthRepository {

    fun login(user: UserLogin): Flow<Result<UserResponse<TokenResponse>>>

    fun register(user: UserRegister): Flow<Result<UserResponse<UserSignupResponse>>>

    fun verifyOtp(verificationKey: String, otp: String): Flow<Result<TokenResponse>>

    fun resendOtp(email: String): Flow<Result<UserResponse<UserSignupResponse>>>
    fun forgotPassword(email: String): Flow<Result<UserResponse<UserSignupResponse>>>
    fun resetPassword(password: String, token: String): Flow<Result<UserResponse<UserSignupResponse>>>
    fun validateUser(email: String): Flow<Result<UserResponse<UserSignupResponse>>>
    fun getUserInfo(token: String): Flow<Result<UserResponse<User>>>
}