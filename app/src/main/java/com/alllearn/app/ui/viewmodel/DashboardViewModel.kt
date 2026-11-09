package com.alllearn.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alllearn.app.data.local.UserPreferencesRepository
import com.alllearn.app.data.model.Course
import com.alllearn.app.data.repository.CourseRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val courseRepository: CourseRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val activeCourseId: StateFlow<String?> = preferencesRepository.activeCourseIdFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val streakCount: StateFlow<Int> = preferencesRepository.streakCountFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun getCourseFlow(courseId: String): Flow<Course?> {
        return courseRepository.getCourseFlow(courseId)
    }

    fun getCoursesForUser(userId: String): Flow<List<Course>> {
        return courseRepository.getCoursesForUser(userId)
    }

    suspend fun getCourseById(courseId: String): Course? {
        return courseRepository.getCourseById(courseId)
    }

    fun recordStudySession() {
        viewModelScope.launch {
            preferencesRepository.recordStudySession()
        }
    }
}
