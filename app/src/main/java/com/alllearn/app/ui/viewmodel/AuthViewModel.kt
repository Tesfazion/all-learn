package com.alllearn.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alllearn.app.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val userId: String, val isNewUser: Boolean) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val currentUserId = authRepository.currentUserIdFlow

    fun signUp(email: String, password: String, confirmPassword: String) {
        if (password != confirmPassword) {
            _uiState.value = AuthUiState.Error("Passwords do not match")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = authRepository.signUp(email, password)
            result.fold(
                onSuccess = { user ->
                    _uiState.value = AuthUiState.Success(user.id, isNewUser = true)
                },
                onFailure = { error ->
                    _uiState.value = AuthUiState.Error(error.message ?: "Sign up failed")
                }
            )
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = authRepository.signIn(email, password)
            result.fold(
                onSuccess = { user ->
                    _uiState.value = AuthUiState.Success(user.id, isNewUser = false)
                },
                onFailure = { error ->
                    _uiState.value = AuthUiState.Error("Wrong email or password")
                }
            )
        }
    }

    suspend fun sendPasswordReset(email: String): String {
        return authRepository.sendPasswordReset(email)
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}
