package com.alllearn.app.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object SignIn : Screen("auth_signin")
    object SignUp : Screen("auth_signup")
    object AccountSetup : Screen("setup_account")
    
    object SelectArea : Screen("onboard_area")
    object SelectTopic : Screen("onboard_topic/{area}") {
        fun createRoute(area: String) = "onboard_topic/$area"
    }
    object SkillLevel : Screen("onboard_level/{area}/{topic}") {
        fun createRoute(area: String, topic: String) = "onboard_level/$area/$topic"
    }
    object Deadline : Screen("onboard_deadline/{area}/{topic}/{level}/{levelNote}") {
        fun createRoute(area: String, topic: String, level: String, levelNote: String) =
            "onboard_deadline/$area/$topic/$level/${if (levelNote.isBlank()) "none" else levelNote}"
    }
    object WeeklyHours : Screen("onboard_hours/{area}/{topic}/{level}/{levelNote}/{deadlineWeeks}") {
        fun createRoute(area: String, topic: String, level: String, levelNote: String, deadlineWeeks: Int) =
            "onboard_hours/$area/$topic/$level/${if (levelNote.isBlank()) "none" else levelNote}/$deadlineWeeks"
    }
    object OutlinePreview : Screen("onboard_preview/{area}/{topic}/{level}/{levelNote}/{deadlineWeeks}/{hoursPerWeek}") {
        fun createRoute(area: String, topic: String, level: String, levelNote: String, deadlineWeeks: Int, hoursPerWeek: Int) =
            "onboard_preview/$area/$topic/$level/${if (levelNote.isBlank()) "none" else levelNote}/$deadlineWeeks/$hoursPerWeek"
    }

    object Home : Screen("home")
    object Courses : Screen("courses")
    object WeekView : Screen("week_view/{courseId}") {
        fun createRoute(courseId: String) = "week_view/$courseId"
    }
    object LessonDetail : Screen("lesson_detail/{courseId}/{lessonId}") {
        fun createRoute(courseId: String, lessonId: String) = "lesson_detail/$courseId/$lessonId"
    }
    object Quiz : Screen("quiz/{courseId}/{lessonId}") {
        fun createRoute(courseId: String, lessonId: String) = "quiz/$courseId/$lessonId"
    }
    object HelperBot : Screen("helper_bot/{courseId}/{lessonId}") {
        fun createRoute(courseId: String, lessonId: String = "none") = "helper_bot/$courseId/$lessonId"
    }
    object Flashcards : Screen("flashcards/{courseTitle}") {
        fun createRoute(courseTitle: String) = "flashcards/$courseTitle"
    }
    object Certificate : Screen("certificate/{courseTitle}") {
        fun createRoute(courseTitle: String) = "certificate/$courseTitle"
    }
    object PrivacyPolicy : Screen("privacy")
    object Progress : Screen("progress")
    object Settings : Screen("settings")
    object Paywall : Screen("paywall")
}
