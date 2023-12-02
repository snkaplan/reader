package com.sk.reader.data.repository.user

import com.google.firebase.auth.FirebaseUser
import com.sk.reader.model.User
import com.sk.reader.utils.Resource

interface UserRepository {
    suspend fun registerUser(email: String, password: String): Resource<FirebaseUser>
    suspend fun signIn(email: String, password: String): Resource<FirebaseUser>
    suspend fun createUser(user: User): Resource<Unit>
    suspend fun signOut(): Resource<Unit>
    fun getCurrentUser(): FirebaseUser?
    suspend fun getUser(): Resource<Map<String, Any?>>
}