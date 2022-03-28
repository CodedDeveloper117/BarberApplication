package com.ammar.vendorapp.authentication.data.api

import android.util.Log
import com.ammar.vendorapp.authentication.common.utils.BASE_URL
import com.ammar.vendorapp.authentication.data.models.*
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*

private const val TAG = "UserAuthenticationApi"

class UserAuthenticationApi(
    private val client: HttpClient
) {
    suspend fun login(user: UserLogin): Either<UserResponse<TokenResponse>, Failure> {
        val url = "$BASE_URL/login"
        return try {
            val response = client.request<UserResponse<TokenResponse>>(url) {
                method = HttpMethod.Post
                body = user
            }
            if(response.code == 203) {
                Either.Success<UserResponse<TokenResponse>, Failure>(null)
            }
            Either.Success(response)
        } catch (e: Exception) {
            Either.Failure(e.catchExceptions())
        }
    }

    suspend fun register(user: UserRegister) : Either<UserResponse<UserSignupResponse>, Failure> {
        val url = "$BASE_URL/sign-up"
        return try {
            val response = client.request<UserResponse<UserSignupResponse>>(url) {
                method = HttpMethod.Post
                body = user
            }
            Log.d(TAG, "register: $response")
            Either.Success(response)
        } catch (e: Exception) {
            Either.Failure(e.catchExceptions())
        }
    }

    suspend fun verifyOtp(verificationKey: String, otp: Int): Either<UserResponse<TokenResponse>, Failure> {
        val url = "$BASE_URL/verify-otp"
        val otpRequest = UserOtpRequest(otp, verificationKey)
        return try {
            val response = client.request<UserResponse<TokenResponse>>(url) {
                method = HttpMethod.Post
                body = otpRequest
            }
            Either.Success(response)
        } catch (e: Exception) {
            Either.Failure(e.catchExceptions())
        }
    }

    suspend fun resendOtp(email: String): Either<UserResponse<UserSignupResponse>, Failure> {
        val url = "$BASE_URL/resend-otp"
        return try {
            val response = client.request<UserResponse<UserSignupResponse>>(url) {
                method = HttpMethod.Post
                body = UserEmailRequest(email)
            }
            Log.d(TAG, "resend otp: $response")
            Either.Success(response)
        } catch (e: Exception) {
            Either.Failure(e.catchExceptions())
        }
    }

    suspend fun forgotPassword(email: String): Either<UserResponse<UserSignupResponse>, Failure> {
        val url = "$BASE_URL/forgot-password"
        return try {
            val response = client.request<UserResponse<UserSignupResponse>>(url) {
                method = HttpMethod.Post
                body = UserEmailRequest(email)
            }
            Either.Success(response)
        } catch (e: Exception) {
            Either.Failure(e.catchExceptions())
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun resetPassword(password: String, token: String): Either<UserResponse<UserSignupResponse>, Failure> {
        val url = "$BASE_URL/reset-password"
        return try {
            val response = client.request<UserResponse<UserSignupResponse>>(url) {
                method = HttpMethod.Post
                body = UserEmailRequest(password)
                headers {
                    append(HttpHeaders.Authorization, token)
                }
            }
            Either.Success(response)
        } catch (e: Exception) {
            Either.Failure(e.catchExceptions())
        }
    }

    suspend fun validateUser(email: String): Either<UserResponse<UserSignupResponse>, Failure> {
        val url = "$BASE_URL/validate-user"
        return try {
            val response = client.request<UserResponse<UserSignupResponse>>(url) {
                method = HttpMethod.Post
                body = UserEmailRequest(email)
            }
            Either.Success(response)
        } catch (e: Exception) {
            Either.Failure(e.catchExceptions())
        }
    }
}

sealed class Either<Response, Error> {
    data class Success<Response, Error>(val response: Response?) : Either<Response, Error>()
    data class Failure<Response, Error>(val error: Error) : Either<Response, Error>()
}

fun Exception.catchExceptions() =
    when (this) {
        is ServerResponseException -> Failure.HttpErrorInternalServerError(Exception("An Unknown Error Occurred, Please try again later"))
        is ClientRequestException -> {
            when (this.response.status.value) {
                400 -> Failure.HttpErrorBadRequest(this)
                401 -> Failure.HttpErrorUnauthorized(this)
                403 -> Failure.HttpErrorForbidden(this)
                404 -> Failure.HttpErrorNotFound(this)
                else -> Failure.HttpError(Exception("An Unknown Error Occurred"))
            }
        }
        is RedirectResponseException -> Failure.HttpError(this)
        else -> Failure.HttpError(Exception("An Unknown Error Occurred"))
    }


sealed class Failure(
    e: Exception
) : Exception(e.message) {
    data class HttpErrorInternalServerError(val e: Exception) : Failure(e)
    data class HttpErrorBadRequest(val e: Exception) : Failure(e)
    data class HttpErrorUnauthorized(val e: Exception) : Failure(e)
    data class HttpErrorForbidden(val e: Exception) : Failure(e)
    data class HttpErrorNotFound(val e: Exception) : Failure(e)
    data class HttpError(val e: Exception) : Failure(e)
}