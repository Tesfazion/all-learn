package com.alllearn.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val email: String,
    val heardAboutUs: String,
    val themeAccent: String,
    val isDarkMode: Boolean,
    val planType: String,
    val streakCount: Int,
    val lastStudyDate: String,
    val reminderHour: Int,
    val reminderMinute: Int,
    val studyDaysJson: String
)

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val area: String,
    val topic: String,
    val levelKey: String,
    val levelNote: String,
    val deadlineWeeks: Int,
    val hoursPerWeek: Int,
    val targetEndDate: String,
    val status: String,
    val outlineJson: String,
    val regenerationsUsed: Int,
    val createdAt: Long,
    val updatedAt: Long
)

@Entity(tableName = "lessons")
data class LessonEntity(
    @PrimaryKey val id: String,
    val courseId: String,
    val weekNumber: Int,
    val title: String,
    val minutes: Int,
    val learningGoalsJson: String,
    val contentMarkdown: String,
    val sourcesJson: String,
    val quizJson: String,
    val status: String,
    val quizScore: Int,
    val quizTotal: Int,
    val lastVerified: String,
    val needsReview: Boolean,
    val completedAt: Long?
)

@Entity(tableName = "chat_messages")
data class ChatEntity(
    @PrimaryKey val id: String,
    val courseId: String,
    val lessonId: String,
    val role: String,
    val text: String,
    val timestamp: Long
)
