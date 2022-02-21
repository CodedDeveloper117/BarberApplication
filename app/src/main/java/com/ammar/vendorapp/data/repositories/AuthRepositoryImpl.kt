package com.ammar.vendorapp.data.repositories

import android.util.Log
import com.ammar.vendorapp.domain.entities.User
import com.ammar.vendorapp.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.ammar.vendorapp.common.utils.Result
import com.ammar.vendorapp.data.firebase.Firestore
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firestore: Firestore,
): AuthRepository {
    override fun loginWithGoogle(user: User): Flow<Result<User>> = flow {
        emit(Result.Loading())
        val result = firestore.getUserInfo(user.id)

        when(result) {
            is Result.Success -> {
                emit(Result.Success(result.data))
            }
            is Result.Error -> {
                emit(Result.Error(result.message))
            }
        }
    }

    override fun register(user: User): Flow<Result<User>> = flow {
        emit(Result.Loading())
        val result = firestore.saveUserInfo(user)
        Log.d("TAG", "register: $result")
        when(result) {
            is Result.Success -> {
                emit(Result.Success(result.data))
            }
            is Result.Error -> {
                emit(Result.Error(result.message))
            }
        }
    }

}