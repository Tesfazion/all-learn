package com.alllearn.app.data.model

import com.google.gson.annotations.SerializedName

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
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String,
    @SerializedName("type") val type: String = "docs"
)

data class LessonItem(
    @SerializedName("id") val id: String,
    @SerializedName("weekNumber") val weekNumber: Int,
    @SerializedName("title") val title: String,
    @SerializedName("minutes") val minutes: Int,
    @SerializedName("sources") val sources: List<SourceLink> = emptyList(),
    @SerializedName("status") val status: String = "not_started"
)

data class WeekPlan(
    @SerializedName("week") val week: Int,
    @SerializedName("title") val title: String,
    @SerializedName("plannedMinutes") val plannedMinutes: Int,
    @SerializedName("lessons") val lessons: List<LessonItem>,
    @SerializedName("hasQuiz") val hasQuiz: Boolean = true
)

data class PhasePlan(
    @SerializedName("phase") val phase: Int,
    @SerializedName("title") val title: String,
    @SerializedName("weeks") val weeks: List<Int>,
    @SerializedName("goal") val goal: String,
    @SerializedName("milestoneProject") val milestoneProject: String
)

data class CourseOutline(
    @SerializedName("title") val title: String,
    @SerializedName("totalWeeks") val totalWeeks: Int,
    @SerializedName("hoursPerWeek") val hoursPerWeek: Int,
    @SerializedName("phases") val phases: List<PhasePlan>,
    @SerializedName("weeks") val weeks: List<WeekPlan>
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
