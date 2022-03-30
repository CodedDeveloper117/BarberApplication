package com.ammar.vendorapp.authentication.data.api

import android.util.Log
import com.ammar.vendorapp.authentication.common.utils.BASE_URL
import com.ammar.vendorapp.authentication.data.models.*
import com.ammar.vendorapp.common.data.User
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.serialization.json.Json

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

    suspend fun verifyOtp(verificationKey: String, otp: String): Either<UserResponse<TokenResponse>, Failure> {
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
                body = UserPasswordRequest(password)
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

    @OptIn(InternalAPI::class)
    suspend fun getUserInfo(token: String): Either<UserResponse<User>, Failure> {
        val url = "$BASE_URL/user-info"
        return try {
            val response = client.request<UserResponse<User>>(url) {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
            }
            Either.Success(response)
        }catch(e: Exception) {
            Either.Failure(e.catchExceptions())
        }
    }
}

sealed class Either<Response, Error> {
    data class Success<Response, Error>(val response: Response) : Either<Response, Error>()
    data class Failure<Response, Error>(val error: Error) : Either<Response, Error>()
}

private val json: Json = Json {
    ignoreUnknownKeys = true
}

suspend fun Exception.catchExceptions() =
    when (this) {
        is ServerResponseException -> Failure.HttpErrorInternalServerError(defaultError)
        is ClientRequestException -> {
            val content = this.response.readText(Charsets.UTF_8)
            val exception = json.decodeFromString(UserError.serializer(), content)
            Log.d(TAG, "exception: $exception, content: $content")
            when (this.response.status.value) {
                400 -> Failure.HttpErrorBadRequest(exception)
                401 -> Failure.HttpErrorUnauthorized(exception)
                403 -> Failure.HttpErrorForbidden(exception)
                404 -> Failure.HttpErrorNotFound(exception)
                else -> Failure.HttpError(defaultError)
            }
        }
        is RedirectResponseException -> Failure.HttpError(defaultError)
        else -> Failure.HttpError(defaultError)
    }


sealed class Failure(
    val code: Int,
    val data: String,
    val status: String
) : RuntimeException() {
    data class HttpErrorInternalServerError(val e: UserError) : Failure(e.code, e.data, e.status)
    data class HttpErrorBadRequest(val e: UserError) : Failure(e.code, e.data, e.status)
    data class HttpErrorUnauthorized(val e: UserError) : Failure(e.code, e.data, e.status)
    data class HttpErrorForbidden(val e: UserError) : Failure(e.code, e.data, e.status)
    data class HttpErrorNotFound(val e: UserError) : Failure(e.code, e.data, e.status)
    data class HttpError(val e: UserError) : Failure(e.code, e.data, e.status)
}