package com.alllearn.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alllearn.app.data.local.UserPreferencesRepository
import com.alllearn.app.data.repository.CourseRepository
import com.alllearn.app.data.repository.LessonRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ProgressViewModel(
    private val courseRepository: CourseRepository,
    private val lessonRepository: LessonRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val streakCount: StateFlow<Int> = preferencesRepository.streakCountFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
}
