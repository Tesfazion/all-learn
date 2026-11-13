package com.alllearn.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alllearn.app.data.model.Course
import com.alllearn.app.data.model.SkillLevel
import com.alllearn.app.data.repository.CourseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class OutlineState {
    object Idle : OutlineState()
    object Loading : OutlineState()
    data class Success(val course: Course) : OutlineState()
    data class Error(val message: String) : OutlineState()
}

class OnboardingViewModel(
    private val courseRepository: CourseRepository
) : ViewModel() {

    private val _outlineState = MutableStateFlow<OutlineState>(OutlineState.Idle)
    val outlineState: StateFlow<OutlineState> = _outlineState.asStateFlow()

    fun generateOutline(
        userId: String,
        area: String,
        topic: String,
        level: SkillLevel,
        levelNote: String,
        deadlineWeeks: Int,
        hoursPerWeek: Int
    ) {
        viewModelScope.launch {
            _outlineState.value = OutlineState.Loading
            val result = courseRepository.generateOutline(
                userId = userId,
                area = area,
                topic = topic,
                level = level,
                levelNote = levelNote,
                deadlineWeeks = deadlineWeeks,
                hoursPerWeek = hoursPerWeek,
                preferences = ""
            )
            result.fold(
                onSuccess = { course ->
                    _outlineState.value = OutlineState.Success(course)
                },
                onFailure = { error ->
                    _outlineState.value = OutlineState.Error(error.message ?: "Failed to generate outline")
                }
            )
        }
    }

    fun regenerateOutline(currentCourse: Course, prompt: String) {
        viewModelScope.launch {
            _outlineState.value = OutlineState.Loading
            val result = courseRepository.regenerateOutline(currentCourse, prompt)
            result.fold(
                onSuccess = { course ->
                    _outlineState.value = OutlineState.Success(course)
                },
                onFailure = { error ->
                    _outlineState.value = OutlineState.Error(error.message ?: "Regeneration failed")
                }
            )
        }
    }

    fun resetState() {
        _outlineState.value = OutlineState.Idle
    }
}
