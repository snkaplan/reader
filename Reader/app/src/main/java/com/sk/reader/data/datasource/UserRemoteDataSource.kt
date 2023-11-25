package com.sk.reader.data.datasource

import com.google.firebase.auth.FirebaseUser
import com.sk.reader.utils.ApiResult

interface UserRemoteDataSource {
    val currentUser: FirebaseUser?
    suspend fun registerUser(email: String, password: String): ApiResult<FirebaseUser>
    suspend fun signIn(email: String, password: String): ApiResult<FirebaseUser>
    suspend fun createUser(user: MutableMap<String, Any?>): ApiResult<Unit>
    suspend fun signOut(): ApiResult<Unit>
    suspend fun getUser(): ApiResult<Map<String, Any?>>
}