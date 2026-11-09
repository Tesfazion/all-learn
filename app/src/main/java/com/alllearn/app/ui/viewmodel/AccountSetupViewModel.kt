package com.alllearn.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alllearn.app.data.local.UserPreferencesRepository
import com.alllearn.app.data.model.ThemeAccent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AccountSetupViewModel(
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _selectedHeardSource = MutableStateFlow("")
    val selectedHeardSource: StateFlow<String> = _selectedHeardSource.asStateFlow()

    private val _selectedAccent = MutableStateFlow(ThemeAccent.BLUE)
    val selectedAccent: StateFlow<ThemeAccent> = _selectedAccent.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    fun setHeardSource(source: String) {
        _selectedHeardSource.value = source
    }

    fun setAccent(accent: ThemeAccent) {
        _selectedAccent.value = accent
    }

    fun toggleDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
    }

    fun saveSetup(onComplete: () -> Unit) {
        viewModelScope.launch {
            preferencesRepository.saveHeardAboutUs(_selectedHeardSource.value)
            preferencesRepository.updateTheme(_selectedAccent.value.name, _isDarkMode.value)
            onComplete()
        }
    }
}
