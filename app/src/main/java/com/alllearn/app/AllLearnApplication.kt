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
import java.util.concurrent.TimeUnit

class AllLearnApplication : Application() {

    lateinit var database: AppDatabase
        private set

    lateinit var userPreferencesRepository: UserPreferencesRepository
        private set

    lateinit var aiCurriculumEngine: AiCurriculumEngine
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

        FirebaseApp.initializeApp(this)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)

        database = AppDatabase.getInstance(this)
        userPreferencesRepository = UserPreferencesRepository(this)
        aiCurriculumEngine = AiCurriculumEngine()

        authRepository = AuthRepository(userPreferencesRepository, database.userDao())
        courseRepository = CourseRepository(database.courseDao(), aiCurriculumEngine, userPreferencesRepository)
        lessonRepository = LessonRepository(database.lessonDao(), aiCurriculumEngine)
        chatRepository = ChatRepository(database.chatDao(), aiCurriculumEngine, userPreferencesRepository)

        scheduleDailyReminderWork()
    }

    private fun scheduleDailyReminderWork() {
        val reminderWorkRequest = PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(12, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            ReminderWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            reminderWorkRequest
        )
    }

    companion object {
        lateinit var instance: AllLearnApplication
            private set
    }
}
