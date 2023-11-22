package com.sk.reader.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.sk.reader.utils.ApiResult
import com.sk.reader.utils.await

private const val USERS_TABLE_NAME = "users"

class UserRemoteDataSourceImpl(
    private val auth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : UserRemoteDataSource {

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun registerUser(email: String, password: String): ApiResult<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            return ApiResult.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResult.Error(null, e.message ?: "General exception")
        }
    }

    override suspend fun signIn(email: String, password: String): ApiResult<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            ApiResult.Success(result.user)
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResult.Error(null, e.message ?: "General Exception")
        }
    }

    override suspend fun createUser(user: MutableMap<String, Any?>): ApiResult<Unit> {
        return try {
            firebaseFirestore.collection(USERS_TABLE_NAME).add(user).await()
            return ApiResult.Success(null)
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResult.Error(null, e.message ?: "General exception")
        }
    }
}