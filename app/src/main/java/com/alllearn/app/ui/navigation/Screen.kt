package com.alllearn.app.ui.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object SignIn : Screen("auth_signin")
    object SignUp : Screen("auth_signup")
    object AccountSetup : Screen("setup_account")
    
    object SelectArea : Screen("onboard_area")
    object SelectTopic : Screen("onboard_topic/{area}") {
        fun createRoute(area: String) = "onboard_topic/${Uri.encode(area)}"
    }
    object SkillLevel : Screen("onboard_level/{area}/{topic}") {
        fun createRoute(area: String, topic: String) = "onboard_level/${Uri.encode(area)}/${Uri.encode(topic)}"
    }
    object Deadline : Screen("onboard_deadline/{area}/{topic}/{level}/{levelNote}") {
        fun createRoute(area: String, topic: String, level: String, levelNote: String) =
            "onboard_deadline/${Uri.encode(area)}/${Uri.encode(topic)}/${Uri.encode(level)}/${Uri.encode(if (levelNote.isBlank()) "none" else levelNote)}"
    }
    object WeeklyHours : Screen("onboard_hours/{area}/{topic}/{level}/{levelNote}/{deadlineWeeks}") {
        fun createRoute(area: String, topic: String, level: String, levelNote: String, deadlineWeeks: Int) =
            "onboard_hours/${Uri.encode(area)}/${Uri.encode(topic)}/${Uri.encode(level)}/${Uri.encode(if (levelNote.isBlank()) "none" else levelNote)}/$deadlineWeeks"
    }
    object OutlinePreview : Screen("onboard_preview/{area}/{topic}/{level}/{levelNote}/{deadlineWeeks}/{hoursPerWeek}") {
        fun createRoute(area: String, topic: String, level: String, levelNote: String, deadlineWeeks: Int, hoursPerWeek: Int) =
            "onboard_preview/${Uri.encode(area)}/${Uri.encode(topic)}/${Uri.encode(level)}/${Uri.encode(if (levelNote.isBlank()) "none" else levelNote)}/$deadlineWeeks/$hoursPerWeek"
    }

    object Home : Screen("home")
    object Courses : Screen("courses")
    object WeekView : Screen("week_view/{courseId}") {
        fun createRoute(courseId: String) = "week_view/${Uri.encode(courseId)}"
    }
    object LessonDetail : Screen("lesson_detail/{courseId}/{lessonId}") {
        fun createRoute(courseId: String, lessonId: String) = "lesson_detail/${Uri.encode(courseId)}/${Uri.encode(lessonId)}"
    }
    object Quiz : Screen("quiz/{courseId}/{lessonId}") {
        fun createRoute(courseId: String, lessonId: String) = "quiz/${Uri.encode(courseId)}/${Uri.encode(lessonId)}"
    }
    object HelperBot : Screen("helper_bot/{courseId}/{lessonId}") {
        fun createRoute(courseId: String, lessonId: String = "none") = "helper_bot/${Uri.encode(courseId)}/${Uri.encode(lessonId)}"
    }
    object Flashcards : Screen("flashcards/{courseTitle}") {
        fun createRoute(courseTitle: String) = "flashcards/${Uri.encode(courseTitle)}"
    }
    object Certificate : Screen("certificate/{courseTitle}") {
        fun createRoute(courseTitle: String) = "certificate/${Uri.encode(courseTitle)}"
    }
    object PrivacyPolicy : Screen("privacy")
    object Progress : Screen("progress")
    object Settings : Screen("settings")
    object Paywall : Screen("paywall")
}
