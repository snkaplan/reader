package com.sk.reader.ui.screens.home

import com.sk.reader.model.MBook

sealed class HomeScreenState {
    object Loading : HomeScreenState()
    object Idle : HomeScreenState()
    data class Success(val data: List<MBook?>) : HomeScreenState()
    data class Error(val message: String) : HomeScreenState()
}