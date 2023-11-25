package com.sk.reader.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.sk.reader.data.repository.UserRepository
import com.sk.reader.model.User
import com.sk.reader.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: UserRepository) :
    ViewModel() {
    var user = MutableStateFlow<User?>(null)
        private set
    var authState = MutableStateFlow<AuthState>(AuthState.Idle)
        private set

    fun createUser(
        name: String,
        surname: String,
        quote: String?,
        profession: String?,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            authState.value = AuthState.CreatingUser()
            when (val result = repository.registerUser(email, password)) {
                is ApiResult.Error -> {
                    authState.value = AuthState.Failed(result.message)
                }

                is ApiResult.Success -> {
                    createUser(name, surname, quote, profession)
                }
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            authState.value = AuthState.Authenticating
            when (val result = repository.signIn(email, password)) {
                is ApiResult.Error -> {
                    authState.value = AuthState.Failed(result.message)
                }

                is ApiResult.Success -> {
                    getUser()
                    authState.value = AuthState.Authenticated(result.data.toString())
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            when (val result = repository.signOut()) {
                is ApiResult.Error -> {
                    authState.value = AuthState.Failed(result.message)
                }

                is ApiResult.Success -> {
                    authState.value = AuthState.SignedOut
                }
            }
        }
    }

    private fun createUser(name: String, surname: String, quote: String?, profession: String?) {
        viewModelScope.launch {
            val userId = repository.getCurrentUser()?.uid
            val user = User(
                uid = userId,
                name = name,
                lastName = surname,
                quote = quote,
                profession = profession
            )
            when (val result = repository.createUser(user)) {
                is ApiResult.Error -> {
                    authState.value = AuthState.Failed(result.message)
                }

                is ApiResult.Success -> {
                    getUser()
                    authState.value = AuthState.Authenticated(result.data.toString())
                }
            }
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return repository.getCurrentUser()
    }

    fun getUser() {
        viewModelScope.launch {
            when (val result = repository.getUser()) {
                is ApiResult.Error -> {
                }

                is ApiResult.Success -> {
                    user.value = result.data?.let { User.fromFirebaseMap(it) }
                }
            }
        }
    }
}