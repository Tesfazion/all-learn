package com.alllearn.app.data.model

data class QuizOption(
    val id: String,
    val text: String
)

data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<QuizOption>,
    val correctOptionId: String,
    val explanation: String
)

data class LessonDetail(
    val id: String,
    val courseId: String,
    val weekNumber: Int,
    val title: String,
    val minutes: Int,
    val learningGoals: List<String>,
    val contentMarkdown: String,
    val sources: List<SourceLink>,
    val quiz: List<QuizQuestion>,
    val status: String = "not_started", // not_started, in_progress, done
    val quizScore: Int = 0,
    val quizTotal: Int = 0,
    val lastVerified: String = "Today",
    val needsReview: Boolean = false,
    val completedAt: Long? = null
)
