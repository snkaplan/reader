package com.sk.reader.ui.screens.login

sealed class AuthState {
    object Idle : AuthState()
    object Authenticating : AuthState()
    data class CreatingUser(val message: String? = null) : AuthState()
    data class Authenticated(val message: String? = null) : AuthState()
    data class Failed(val message: String? = null) : AuthState()
}