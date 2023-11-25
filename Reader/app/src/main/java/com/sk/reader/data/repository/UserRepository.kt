package com.sk.reader.data.repository

import com.google.firebase.auth.FirebaseUser
import com.sk.reader.model.User
import com.sk.reader.utils.ApiResult

interface UserRepository {
    suspend fun registerUser(email: String, password: String): ApiResult<FirebaseUser>
    suspend fun signIn(email: String, password: String): ApiResult<FirebaseUser>
    suspend fun createUser(user: User): ApiResult<Unit>
    suspend fun signOut(): ApiResult<Unit>
    fun getCurrentUser(): FirebaseUser?
    suspend fun getUser(): ApiResult<Map<String, Any?>>
}