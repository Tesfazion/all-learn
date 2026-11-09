package com.alllearn.app.data.model

enum class ThemeAccent(val displayName: String, val hexColor: String) {
    BLUE("Ocean Blue", "#0284C7"),
    EMERALD("Emerald Green", "#059669"),
    PURPLE("Royal Purple", "#7C3AED"),
    AMBER("Warm Amber", "#D97706"),
    ROSE("Sunset Rose", "#E11D48")
}

enum class PlanType {
    FREE,
    PAID
}

data class UserProfile(
    val id: String = "",
    val email: String = "",
    val heardAboutUs: String = "",
    val themeAccent: ThemeAccent = ThemeAccent.BLUE,
    val isDarkMode: Boolean = false,
    val planType: PlanType = PlanType.FREE,
    val streakCount: Int = 0,
    val lastStudyDate: String = "",
    val reminderHour: Int = 20,
    val reminderMinute: Int = 0,
    val studyDays: List<String> = listOf("Mon", "Wed", "Fri")
)
