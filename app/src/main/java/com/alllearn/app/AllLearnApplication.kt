package com.alllearn.app

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.alllearn.app.data.local.AppDatabase
import com.alllearn.app.data.local.UserPreferencesRepository
import com.alllearn.app.data.remote.AiCurriculumEngine
import com.alllearn.app.data.repository.AuthRepository
import com.alllearn.app.data.repository.ChatRepository
import com.alllearn.app.data.repository.CourseRepository
import com.alllearn.app.data.repository.LessonRepository
import com.alllearn.app.worker.ReminderWorker
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AllLearnApplication : Application() {

    lateinit var database: AppDatabase
        private set

    lateinit var userPreferencesRepository: UserPreferencesRepository
        private set

    // Exposed as var — hot-swapped when BYOK key changes
    var aiCurriculumEngine: AiCurriculumEngine = AiCurriculumEngine()
        private set

    lateinit var authRepository: AuthRepository
        private set

    lateinit var courseRepository: CourseRepository
        private set

    lateinit var lessonRepository: LessonRepository
        private set

    lateinit var chatRepository: ChatRepository
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        try {
            FirebaseApp.initializeApp(this)
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        database = AppDatabase.getInstance(this)
        userPreferencesRepository = UserPreferencesRepository(this)

        // Initial engine with app default key
        aiCurriculumEngine = AiCurriculumEngine()

        authRepository   = AuthRepository(userPreferencesRepository, database.userDao())
        courseRepository = CourseRepository(database.courseDao(), aiCurriculumEngine, userPreferencesRepository)
        lessonRepository = LessonRepository(database.lessonDao(), aiCurriculumEngine)
        chatRepository   = ChatRepository(database.chatDao(), aiCurriculumEngine, userPreferencesRepository)

        // Observe BYOK settings — hot-swap engine on change
        CoroutineScope(Dispatchers.IO).launch {
            userPreferencesRepository.byokEnabledFlow.combine(
                userPreferencesRepository.byokApiKeyFlow
            ) { enabled, key -> Pair(enabled, key) }
            .collect { (enabled, key) ->
                val effectiveKey = if (enabled && key.isNotBlank()) key
                                   else AiCurriculumEngine.DEFAULT_KEY
                aiCurriculumEngine = AiCurriculumEngine(effectiveKey)
            }
        }

        scheduleDailyReminderWork()
    }

    private fun scheduleDailyReminderWork() {
        val reminderWork = PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(12, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            ReminderWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            reminderWork
        )
    }

    companion object {
        lateinit var instance: AllLearnApplication
            private set
    }
}
