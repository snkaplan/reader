package com.sk.reader.data.datasource.user

import com.google.firebase.auth.FirebaseUser
import com.sk.reader.utils.Resource

interface UserRemoteDataSource {
    val currentUser: FirebaseUser?
    suspend fun registerUser(email: String, password: String): Resource<FirebaseUser>
    suspend fun signIn(email: String, password: String): Resource<FirebaseUser>
    suspend fun createUser(user: MutableMap<String, Any?>): Resource<Unit>
    suspend fun signOut(): Resource<Unit>
    suspend fun getUser(): Resource<Map<String, Any?>>
}