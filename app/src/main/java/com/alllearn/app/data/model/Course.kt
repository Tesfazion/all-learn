package com.alllearn.app.data.model

enum class SkillLevel(val key: String) {
    BEGINNER("beginner"),
    INTERMEDIATE("intermediate"),
    ADVANCED("advanced")
}

enum class CourseStatus {
    OUTLINE_PENDING,
    ACTIVE,
    COMPLETED,
    PAUSED
}

data class SourceLink(
    val title: String,
    val url: String,
    val type: String = "docs" // docs, book, article, video_reference
)

data class LessonItem(
    val id: String,
    val weekNumber: Int,
    val title: String,
    val minutes: Int,
    val sources: List<SourceLink> = emptyList(),
    val status: String = "not_started" // not_started, in_progress, done
)

data class WeekPlan(
    val week: Int,
    val title: String,
    val plannedMinutes: Int,
    val lessons: List<LessonItem>,
    val hasQuiz: Boolean = true
)

data class PhasePlan(
    val phase: Int,
    val title: String,
    val weeks: List<Int>,
    val goal: String,
    val milestoneProject: String
)

data class CourseOutline(
    val title: String,
    val totalWeeks: Int,
    val hoursPerWeek: Int,
    val phases: List<PhasePlan>,
    val weeks: List<WeekPlan>
)

data class Course(
    val id: String,
    val userId: String,
    val area: String,
    val topic: String,
    val level: SkillLevel,
    val levelNote: String = "",
    val deadlineWeeks: Int,
    val hoursPerWeek: Int,
    val targetEndDate: String,
    val status: CourseStatus = CourseStatus.ACTIVE,
    val outline: CourseOutline,
    val regenerationsUsed: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
