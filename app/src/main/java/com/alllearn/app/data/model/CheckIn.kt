package com.alllearn.app.data.model

enum class AdjustmentChoice {
    CONTINUE,
    COMPRESS,
    EXTEND
}

data class WeeklyCheckIn(
    val id: String = "",
    val courseId: String,
    val weekNumber: Int,
    val completionRate: Int, // 0 to 100%
    val choice: AdjustmentChoice = AdjustmentChoice.CONTINUE,
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
