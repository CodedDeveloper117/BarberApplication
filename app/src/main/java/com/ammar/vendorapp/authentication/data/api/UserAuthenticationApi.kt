package com.ammar.vendorapp.authentication.data.api

import android.util.Log
import com.ammar.vendorapp.authentication.common.utils.BASE_URL
import com.ammar.vendorapp.authentication.data.models.UserLogin
import com.ammar.vendorapp.authentication.data.models.UserRegister
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*

private const val TAG = "UserAuthenticationApi"

class UserAuthenticationApi(
    private val client: HttpClient
) {
    suspend fun login(user: UserLogin): Either<Any, Failure> {
        val url = "$BASE_URL/login"
        return try {
            val response = client.request<Map<String, Any>>(url) {
                method = HttpMethod.Post
                body = user
            }
            Log.d(TAG, "login: $response")
            Either.Success(response)
        } catch (e: Exception) {
            Either.Failure(e.catchExceptions())
        }
    }

    suspend fun register(user: UserRegister) : Either<Any, Failure> {
        val url = "$BASE_URL/sign-up"
        return try {
            val response = client.request<Any>(url) {
                method = HttpMethod.Post
                body = user
            }
            Log.d(TAG, "register: $response")
            Either.Success(response)
        } catch (e: Exception) {
            Either.Failure(e.catchExceptions())
        }
    }
}

sealed class Either<Response, Error> {
    data class Success<Response, Error>(val response: Response) : Either<Response, Error>()
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