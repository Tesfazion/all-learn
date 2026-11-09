package com.alllearn.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.alllearn.app.data.model.ThemeAccent
import com.alllearn.app.ui.navigation.AppNavGraph
import com.alllearn.app.ui.theme.AllLearnTheme
import com.alllearn.app.ui.viewmodel.AccountSetupViewModel
import com.alllearn.app.ui.viewmodel.AuthViewModel
import com.alllearn.app.ui.viewmodel.DashboardViewModel
import com.alllearn.app.ui.viewmodel.HelperBotViewModel
import com.alllearn.app.ui.viewmodel.LessonViewModel
import com.alllearn.app.ui.viewmodel.OnboardingViewModel
import com.alllearn.app.ui.viewmodel.ProgressViewModel
import com.alllearn.app.ui.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as AllLearnApplication

        val authViewModel = AuthViewModel(app.authRepository)
        val setupViewModel = AccountSetupViewModel(app.userPreferencesRepository)
        val onboardingViewModel = OnboardingViewModel(app.courseRepository)
        val dashboardViewModel = DashboardViewModel(app.courseRepository, app.userPreferencesRepository)
        val lessonViewModel = LessonViewModel(app.lessonRepository)
        val botViewModel = HelperBotViewModel(app.chatRepository)
        val progressViewModel = ProgressViewModel(app.courseRepository, app.lessonRepository, app.userPreferencesRepository)
        val settingsViewModel = SettingsViewModel(app.authRepository, app.userPreferencesRepository)

        setContent {
            val accentName by app.userPreferencesRepository.themeAccentFlow.collectAsState(initial = "BLUE")
            val isDarkMode by app.userPreferencesRepository.isDarkModeFlow.collectAsState(initial = false)

            val currentAccent = try {
                ThemeAccent.valueOf(accentName)
            } catch (e: Exception) {
                ThemeAccent.BLUE
            }

            AllLearnTheme(accent = currentAccent, darkTheme = isDarkMode) {
                val navController = rememberNavController()

                AppNavGraph(
                    navController = navController,
                    authViewModel = authViewModel,
                    setupViewModel = setupViewModel,
                    onboardingViewModel = onboardingViewModel,
                    dashboardViewModel = dashboardViewModel,
                    lessonViewModel = lessonViewModel,
                    botViewModel = botViewModel,
                    progressViewModel = progressViewModel,
                    settingsViewModel = settingsViewModel
                )
            }
        }
    }
}
