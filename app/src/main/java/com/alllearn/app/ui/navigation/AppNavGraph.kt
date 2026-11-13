package com.alllearn.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.alllearn.app.ui.screens.auth.SignInScreen
import com.alllearn.app.ui.screens.auth.SignUpScreen
import com.alllearn.app.ui.screens.certificate.CertificateScreen
import com.alllearn.app.ui.screens.chat.HelperBotScreen
import com.alllearn.app.ui.screens.courses.CourseListScreen
import com.alllearn.app.ui.screens.dashboard.DashboardScreen
import com.alllearn.app.ui.screens.lesson.LessonDetailScreen
import com.alllearn.app.ui.screens.lesson.QuizScreen
import com.alllearn.app.ui.screens.lesson.WeekViewScreen
import com.alllearn.app.ui.screens.onboarding.DeadlineScreen
import com.alllearn.app.ui.screens.onboarding.OutlinePreviewScreen
import com.alllearn.app.ui.screens.onboarding.SelectAreaScreen
import com.alllearn.app.ui.screens.onboarding.SelectTopicScreen
import com.alllearn.app.ui.screens.onboarding.SkillLevelScreen
import com.alllearn.app.ui.screens.onboarding.WeeklyHoursScreen
import com.alllearn.app.ui.screens.paywall.PaywallScreen
import com.alllearn.app.ui.screens.privacy.PrivacyPolicyScreen
import com.alllearn.app.ui.screens.progress.ProgressScreen
import com.alllearn.app.ui.screens.splash.SplashScreen
import com.alllearn.app.ui.screens.review.FlashcardScreen
import com.alllearn.app.ui.screens.settings.SettingsScreen
import com.alllearn.app.ui.screens.setup.AccountSetupScreen
import com.alllearn.app.ui.viewmodel.AccountSetupViewModel
import com.alllearn.app.ui.viewmodel.AuthViewModel
import com.alllearn.app.ui.viewmodel.DashboardViewModel
import com.alllearn.app.ui.viewmodel.HelperBotViewModel
import com.alllearn.app.ui.viewmodel.LessonViewModel
import com.alllearn.app.ui.viewmodel.OnboardingViewModel
import com.alllearn.app.ui.viewmodel.ProgressViewModel
import com.alllearn.app.ui.viewmodel.SettingsViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    setupViewModel: AccountSetupViewModel,
    onboardingViewModel: OnboardingViewModel,
    dashboardViewModel: DashboardViewModel,
    lessonViewModel: LessonViewModel,
    botViewModel: HelperBotViewModel,
    progressViewModel: ProgressViewModel,
    settingsViewModel: SettingsViewModel
) {
    val currentUserId by authViewModel.currentUserId.collectAsState(initial = null)
    val startDestination = if (currentUserId != null) Screen.Home.route else Screen.Splash.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToAuth = {
                    navController.navigate(Screen.SignIn.route)
                }
            )
        }

        composable(Screen.SignIn.route) {
            SignInScreen(
                viewModel = authViewModel,
                onSignInSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }
                },
                onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) }
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                viewModel = authViewModel,
                onSignUpSuccess = {
                    navController.navigate(Screen.AccountSetup.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                },
                onNavigateToSignIn = { navController.navigate(Screen.SignIn.route) }
            )
        }

        composable(Screen.AccountSetup.route) {
            AccountSetupScreen(
                viewModel = setupViewModel,
                onSetupComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.AccountSetup.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            DashboardScreen(
                viewModel = dashboardViewModel,
                userId = currentUserId ?: "",
                onStartNewCourse = { navController.navigate(Screen.SelectArea.route) },
                onOpenCourse = { courseId -> navController.navigate(Screen.WeekView.createRoute(courseId)) },
                onOpenBot = { courseId -> navController.navigate(Screen.HelperBot.createRoute(courseId)) },
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable(Screen.Courses.route) {
            CourseListScreen(
                viewModel = dashboardViewModel,
                userId = currentUserId ?: "",
                onOpenCourse = { courseId -> navController.navigate(Screen.WeekView.createRoute(courseId)) },
                onStartNewCourse = { navController.navigate(Screen.SelectArea.route) },
                onNavigate = { route -> navController.navigate(route) },
                onOpenBot = { courseId -> navController.navigate(Screen.HelperBot.createRoute(courseId)) }
            )
        }

        // Onboarding Flow
        composable(Screen.SelectArea.route) {
            SelectAreaScreen(
                onAreaSelected = { area ->
                    onboardingViewModel.resetState()
                    navController.navigate(Screen.SelectTopic.createRoute(area))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.SelectTopic.route,
            arguments = listOf(navArgument("area") { type = NavType.StringType })
        ) { backStackEntry ->
            val area = android.net.Uri.decode(backStackEntry.arguments?.getString("area") ?: "")
            SelectTopicScreen(
                area = area,
                onTopicSelected = { topic -> navController.navigate(Screen.SkillLevel.createRoute(area, topic)) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.SkillLevel.route,
            arguments = listOf(
                navArgument("area") { type = NavType.StringType },
                navArgument("topic") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val area = android.net.Uri.decode(backStackEntry.arguments?.getString("area") ?: "")
            val topic = android.net.Uri.decode(backStackEntry.arguments?.getString("topic") ?: "")
            SkillLevelScreen(
                area = area,
                topic = topic,
                onLevelSelected = { level, note ->
                    navController.navigate(Screen.Deadline.createRoute(area, topic, level.key, note))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Deadline.route,
            arguments = listOf(
                navArgument("area") { type = NavType.StringType },
                navArgument("topic") { type = NavType.StringType },
                navArgument("level") { type = NavType.StringType },
                navArgument("levelNote") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val area = android.net.Uri.decode(backStackEntry.arguments?.getString("area") ?: "")
            val topic = android.net.Uri.decode(backStackEntry.arguments?.getString("topic") ?: "")
            val level = android.net.Uri.decode(backStackEntry.arguments?.getString("level") ?: "")
            val rawNote = android.net.Uri.decode(backStackEntry.arguments?.getString("levelNote") ?: "")
            val levelNote = if (rawNote == "none") "" else rawNote
            DeadlineScreen(
                area = area,
                topic = topic,
                level = level,
                levelNote = levelNote,
                onDeadlineSelected = { weeks ->
                    navController.navigate(Screen.WeeklyHours.createRoute(area, topic, level, levelNote, weeks))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.WeeklyHours.route,
            arguments = listOf(
                navArgument("area") { type = NavType.StringType },
                navArgument("topic") { type = NavType.StringType },
                navArgument("level") { type = NavType.StringType },
                navArgument("levelNote") { type = NavType.StringType },
                navArgument("deadlineWeeks") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val area = android.net.Uri.decode(backStackEntry.arguments?.getString("area") ?: "")
            val topic = android.net.Uri.decode(backStackEntry.arguments?.getString("topic") ?: "")
            val level = android.net.Uri.decode(backStackEntry.arguments?.getString("level") ?: "")
            val rawNote = android.net.Uri.decode(backStackEntry.arguments?.getString("levelNote") ?: "")
            val levelNote = if (rawNote == "none") "" else rawNote
            val weeks = backStackEntry.arguments?.getInt("deadlineWeeks") ?: 12
            WeeklyHoursScreen(
                area = area,
                topic = topic,
                level = level,
                levelNote = levelNote,
                deadlineWeeks = weeks,
                onHoursSelected = { hours ->
                    navController.navigate(Screen.OutlinePreview.createRoute(area, topic, level, levelNote, weeks, hours))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.OutlinePreview.route,
            arguments = listOf(
                navArgument("area") { type = NavType.StringType },
                navArgument("topic") { type = NavType.StringType },
                navArgument("level") { type = NavType.StringType },
                navArgument("levelNote") { type = NavType.StringType },
                navArgument("deadlineWeeks") { type = NavType.IntType },
                navArgument("hoursPerWeek") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val area = android.net.Uri.decode(backStackEntry.arguments?.getString("area") ?: "")
            val topic = android.net.Uri.decode(backStackEntry.arguments?.getString("topic") ?: "")
            val levelKey = android.net.Uri.decode(backStackEntry.arguments?.getString("level") ?: "")
            val rawNote = android.net.Uri.decode(backStackEntry.arguments?.getString("levelNote") ?: "")
            val levelNote = if (rawNote == "none") "" else rawNote
            val weeks = backStackEntry.arguments?.getInt("deadlineWeeks") ?: 12
            val hours = backStackEntry.arguments?.getInt("hoursPerWeek") ?: 7
            OutlinePreviewScreen(
                viewModel = onboardingViewModel,
                userId = currentUserId ?: "",
                area = area,
                topic = topic,
                levelKey = levelKey,
                levelNote = levelNote,
                deadlineWeeks = weeks,
                hoursPerWeek = hours,
                onApproveCourse = { courseId ->
                    navController.navigate(Screen.WeekView.createRoute(courseId)) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.WeekView.route,
            arguments = listOf(navArgument("courseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val courseId = android.net.Uri.decode(backStackEntry.arguments?.getString("courseId") ?: "")
            WeekViewScreen(
                courseId = courseId,
                viewModel = dashboardViewModel,
                onOpenLesson = { lesson ->
                    navController.navigate(Screen.LessonDetail.createRoute(courseId, lesson.id))
                },
                onOpenQuiz = { lessonId ->
                    navController.navigate(Screen.Quiz.createRoute(courseId, lessonId))
                },
                onOpenFlashcards = {
                    navController.navigate(Screen.Flashcards.createRoute("Course Review"))
                },
                onOpenCertificate = {
                    navController.navigate(Screen.Certificate.createRoute("Course Completion"))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.LessonDetail.route,
            arguments = listOf(
                navArgument("courseId") { type = NavType.StringType },
                navArgument("lessonId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val courseId = android.net.Uri.decode(backStackEntry.arguments?.getString("courseId") ?: "")
            val lessonId = android.net.Uri.decode(backStackEntry.arguments?.getString("lessonId") ?: "")

            LessonDetailScreen(
                courseId = courseId,
                lessonId = lessonId,
                topic = "",
                viewModel = lessonViewModel,
                dashboardViewModel = dashboardViewModel,
                onOpenQuiz = { lId -> navController.navigate(Screen.Quiz.createRoute(courseId, lId)) },
                onOpenBot = { cId, lId -> navController.navigate(Screen.HelperBot.createRoute(cId, lId)) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Quiz.route,
            arguments = listOf(
                navArgument("courseId") { type = NavType.StringType },
                navArgument("lessonId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val courseId = android.net.Uri.decode(backStackEntry.arguments?.getString("courseId") ?: "")
            val lessonId = android.net.Uri.decode(backStackEntry.arguments?.getString("lessonId") ?: "")
            QuizScreen(
                courseId = courseId,
                lessonId = lessonId,
                viewModel = lessonViewModel,
                onQuizComplete = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.HelperBot.route,
            arguments = listOf(
                navArgument("courseId") { type = NavType.StringType },
                navArgument("lessonId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val courseId = android.net.Uri.decode(backStackEntry.arguments?.getString("courseId") ?: "")
            val lessonId = android.net.Uri.decode(backStackEntry.arguments?.getString("lessonId") ?: "")
            HelperBotScreen(
                courseId = courseId,
                lessonId = lessonId,
                viewModel = botViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Flashcards.route,
            arguments = listOf(navArgument("courseTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            val courseTitle = android.net.Uri.decode(backStackEntry.arguments?.getString("courseTitle") ?: "Course Review")
            FlashcardScreen(
                courseTitle = courseTitle,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Certificate.route,
            arguments = listOf(navArgument("courseTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            val courseTitle = android.net.Uri.decode(backStackEntry.arguments?.getString("courseTitle") ?: "Course Completion")
            CertificateScreen(
                courseTitle = courseTitle,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.PrivacyPolicy.route) {
            PrivacyPolicyScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Progress.route) {
            ProgressScreen(
                viewModel = progressViewModel,
                onNavigate = { route -> navController.navigate(route) },
                onOpenBot = { navController.navigate(Screen.HelperBot.createRoute("none")) }
            )
        }

        composable(Screen.Settings.route) {
            val userEmail by settingsViewModel.userEmail.collectAsState()
            SettingsScreen(
                viewModel = settingsViewModel,
                onNavigateToPaywall = { navController.navigate(Screen.Paywall.route) },
                onSignOut = {
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigate = { route -> navController.navigate(route) },
                onOpenBot  = { navController.navigate(Screen.HelperBot.createRoute("none")) },
                userEmail  = userEmail
            )
        }

        composable(Screen.Paywall.route) {
            PaywallScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
