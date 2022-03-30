package com.ammar.vendorapp.authentication.data.repositories

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class DatastoreRepository @Inject constructor(
    private val context: Context
) {

    private val Context.datastore by preferencesDataStore("user_store")
    val userPreferences = context.datastore.data
        .catch { exception ->
            Log.d("TAG", "${exception.message}: ")
            if(exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {
            val token = it[PreferencesKeys.TOKEN] ?: ""
            token
        }

    suspend fun saveToken(token: String) {
        context.datastore.edit {
            it[PreferencesKeys.TOKEN] = token
        }
    }

    private object PreferencesKeys {
        val TOKEN = stringPreferencesKey("token")
    }
}