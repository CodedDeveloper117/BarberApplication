package com.ammar.vendorapp.data.firebase

import android.util.Log
import com.ammar.vendorapp.domain.entities.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.Exception
import kotlin.coroutines.cancellation.CancellationException
import com.ammar.vendorapp.common.utils.Result

class Firestore @Inject constructor() {
    private val db = Firebase.firestore
    private val users = db.collection("users")

    suspend fun saveUserInfo(user: User): Result<User> {
        Log.d("TAG", "saveUserInfo: ${user.id}")
        try {
            Log.d("TAG", "saveUserInfo: trycatch")
            users.document(user.id).set(user).await()
            Log.d("TAG", "saveUserInfo: done")
            return Result.Success(user)
        }catch (e: Exception) {
            if(e is CancellationException) {
                print(e.message.toString(), "cancellation exception")
                return Result.Error(e.message.toString(), e)
            }
            print(e.message.toString(), "cancellation exception")
            return Result.Error(e.message.toString(), e)
        }

    }

    suspend fun getUserInfo(userID: String) : Result<User> {
        return try {
            val documentSnapshot = users.document(userID).get().await()

            if (documentSnapshot.exists()) {
                print(documentSnapshot.data.toString(), "check if user exists")
                val user = User(
                    id = documentSnapshot["id"].toString() ?: userID,
                    email = documentSnapshot["email"].toString(),
                    username = documentSnapshot["username"].toString(),
                    fullName = documentSnapshot["fullName"].toString(),
                    photoUrl = documentSnapshot["photoUrl"].toString(),
                    phone = documentSnapshot["phone"] as String?
                )
                print(user, "checkIfUserExists")
                Result.Success(user)
            } else {
                print(userID, "checkIfUserExists: user does not exists")
                Result.Error("User not found")
            }
        }catch (e: Exception) {
            Result.Error(e.message.toString(), e)
        }
    }

    private fun print(data: Any, tag: String) {
        Log.d(tag, "print: $data")
    }
}