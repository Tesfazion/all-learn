package com.alllearn.app.data.model

enum class MessageRole {
    USER,
    ASSISTANT
}

data class ChatMessage(
    val id: String = "",
    val courseId: String,
    val lessonId: String = "",
    val role: MessageRole,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)
