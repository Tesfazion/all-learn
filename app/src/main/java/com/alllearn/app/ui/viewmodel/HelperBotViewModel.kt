package com.alllearn.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alllearn.app.data.model.ChatMessage
import com.alllearn.app.data.repository.ChatRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HelperBotViewModel(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _remainingMessages = MutableStateFlow(20)
    val remainingMessages: StateFlow<Int> = _remainingMessages.asStateFlow()

    private val _isSending = MutableStateFlow(false)
    val isSending: StateFlow<Boolean> = _isSending.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun getChatMessagesFlow(courseId: String): Flow<List<ChatMessage>> {
        return chatRepository.getChatMessagesFlow(courseId)
    }

    fun loadRemainingMessages() {
        viewModelScope.launch {
            _remainingMessages.value = chatRepository.getRemainingMessages()
        }
    }

    fun sendMessage(
        courseId: String,
        lessonId: String,
        courseTopic: String,
        lessonTitle: String,
        query: String
    ) {
        if (query.isBlank()) return

        viewModelScope.launch {
            _isSending.value = true
            _errorMessage.value = null

            val result = chatRepository.sendMessage(
                courseId = courseId,
                lessonId = lessonId,
                courseTopic = courseTopic,
                lessonTitle = lessonTitle,
                userQuery = query
            )

            result.fold(
                onSuccess = {
                    _remainingMessages.value = chatRepository.getRemainingMessages()
                },
                onFailure = { error ->
                    _errorMessage.value = error.message ?: "Failed to send message"
                }
            )

            _isSending.value = false
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
