package com.ammar.vendorapp.common.api

import android.util.Log
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.observer.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

private const val TAG = "KtorClient"

object KtorClient {
    private val json = Json {
        encodeDefaults = false
        ignoreUnknownKeys = false
        isLenient = false
        prettyPrint = true
        coerceInputValues = true
    }

    val httpClient = HttpClient {
        install(JsonFeature){
            serializer = KotlinxSerializer(json)
        }
        install(Logging) {
            logger = object: Logger {
                override fun log(message: String) {
                    Log.d(TAG, "log: $message")
                }
            }
            level = LogLevel.ALL
        }
        install(ResponseObserver) {
            onResponse {
                Log.d(TAG, "HttpStatus: ${it.status.value}")
            }
        }
        install(HttpTimeout) {
            socketTimeoutMillis = 30_000
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 30_000
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }
}