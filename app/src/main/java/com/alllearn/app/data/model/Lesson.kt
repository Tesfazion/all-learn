package com.alllearn.app.data.model

import com.google.gson.annotations.SerializedName

data class QuizOption(
    @SerializedName("id") val id: String,
    @SerializedName("text") val text: String
)

data class QuizQuestion(
    @SerializedName("id") val id: String,
    @SerializedName("question") val question: String,
    @SerializedName("options") val options: List<QuizOption>,
    @SerializedName("correctOptionId") val correctOptionId: String,
    @SerializedName("explanation") val explanation: String
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
