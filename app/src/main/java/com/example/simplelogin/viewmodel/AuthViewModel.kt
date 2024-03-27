package com.example.simplelogin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private var userId: String? = null

    fun setUserId(id: String) {
        userId = id
    }

    fun getUserId(): String? {
        return userId
    }
    fun deleteUserId(){
        userId = null
    }
}
