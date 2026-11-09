package com.alllearn.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alllearn.app.data.model.LessonDetail
import com.alllearn.app.data.model.LessonItem
import com.alllearn.app.data.repository.LessonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LessonViewModel(
    private val lessonRepository: LessonRepository
) : ViewModel() {

    private val _lessonDetail = MutableStateFlow<LessonDetail?>(null)
    val lessonDetail: StateFlow<LessonDetail?> = _lessonDetail.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadLesson(courseId: String, lessonItem: LessonItem, topic: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val detail = lessonRepository.getOrCreateLessonDetail(courseId, lessonItem, topic)
            _lessonDetail.value = detail
            _isLoading.value = false
        }
    }

    fun markAsDone(lessonId: String) {
        viewModelScope.launch {
            lessonRepository.markLessonCompleted(lessonId)
            _lessonDetail.value = _lessonDetail.value?.copy(status = "done")
        }
    }

    fun submitQuizScore(lessonId: String, score: Int, total: Int) {
        viewModelScope.launch {
            lessonRepository.saveQuizResult(lessonId, score, total)
            _lessonDetail.value = _lessonDetail.value?.copy(
                status = "done",
                quizScore = score,
                quizTotal = total
            )
        }
    }
}
