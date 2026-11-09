package com.alllearn.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alllearn.app.data.local.UserPreferencesRepository
import com.alllearn.app.data.model.ThemeAccent
import com.alllearn.app.data.repository.AuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val authRepository: AuthRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val currentAccent: StateFlow<String> = preferencesRepository.themeAccentFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "BLUE")

    val isDarkMode: StateFlow<Boolean> = preferencesRepository.isDarkModeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun updateTheme(accent: ThemeAccent, darkMode: Boolean) {
        viewModelScope.launch {
            preferencesRepository.updateTheme(accent.name, darkMode)
        }
    }

    fun signOut(onComplete: () -> Unit) {
        viewModelScope.launch {
            authRepository.signOut()
            onComplete()
        }
    }

    fun deleteAccountAndData(onComplete: () -> Unit) {
        viewModelScope.launch {
            authRepository.signOut()
            onComplete()
        }
    }
}
