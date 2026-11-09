package com.alllearn.app.data.model

data class MistakeReport(
    val id: String = "",
    val userId: String,
    val courseId: String,
    val lessonId: String,
    val message: String,
    val createdAt: Long = System.currentTimeMillis()
)
