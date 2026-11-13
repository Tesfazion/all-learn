package com.alllearn.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alllearn.app.data.local.UserPreferencesRepository
import com.alllearn.app.data.model.ThemeAccent
import com.alllearn.app.data.repository.AuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
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

    val userEmail: StateFlow<String> = preferencesRepository.userEmailFlow
        .map { it ?: "" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val byokKey: StateFlow<String> = preferencesRepository.byokApiKeyFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val byokEnabled: StateFlow<Boolean> = preferencesRepository.byokEnabledFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun updateTheme(accent: ThemeAccent, darkMode: Boolean) {
        viewModelScope.launch { preferencesRepository.updateTheme(accent.name, darkMode) }
    }

    fun saveBYOKKey(key: String, enabled: Boolean) {
        viewModelScope.launch { preferencesRepository.saveBYOKKey(key.trim(), enabled) }
    }

    fun clearBYOKKey() {
        viewModelScope.launch { preferencesRepository.saveBYOKKey("", false) }
    }

    fun signOut(onComplete: () -> Unit) {
        viewModelScope.launch { authRepository.signOut(); onComplete() }
    }

    fun deleteAccountAndData(onComplete: () -> Unit) {
        viewModelScope.launch { authRepository.signOut(); onComplete() }
    }
}
