package com.alllearn.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {

    private object Keys {
        val USER_ID = stringPreferencesKey("user_id")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val THEME_ACCENT = stringPreferencesKey("theme_accent")
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val ACTIVE_COURSE_ID = stringPreferencesKey("active_course_id")
        val HEARD_ABOUT_US = stringPreferencesKey("heard_about_us")
        val PLAN_TYPE = stringPreferencesKey("plan_type")
        val BOT_MESSAGES_COUNT = intPreferencesKey("bot_messages_count")
        val BOT_LAST_RESET_DATE = stringPreferencesKey("bot_last_reset_date")
        val REGENERATIONS_COUNT = intPreferencesKey("regenerations_count")
        val STREAK_COUNT = intPreferencesKey("streak_count")
        val LAST_STUDY_DATE = stringPreferencesKey("last_study_date")
    }

    val userIdFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[Keys.USER_ID]
    }

    val userEmailFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[Keys.USER_EMAIL]
    }

    val themeAccentFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[Keys.THEME_ACCENT] ?: "BLUE"
    }

    val isDarkModeFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.IS_DARK_MODE] ?: false
    }

    val activeCourseIdFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[Keys.ACTIVE_COURSE_ID]
    }

    val planTypeFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[Keys.PLAN_TYPE] ?: "FREE"
    }

    val streakCountFlow: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[Keys.STREAK_COUNT] ?: 0
    }

    suspend fun saveUserSession(userId: String, email: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.USER_ID] = userId
            prefs[Keys.USER_EMAIL] = email
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.remove(Keys.USER_ID)
            prefs.remove(Keys.USER_EMAIL)
            prefs.remove(Keys.ACTIVE_COURSE_ID)
        }
    }

    suspend fun updateTheme(accentName: String, isDarkMode: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.THEME_ACCENT] = accentName
            prefs[Keys.IS_DARK_MODE] = isDarkMode
        }
    }

    suspend fun saveHeardAboutUs(source: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.HEARD_ABOUT_US] = source
        }
    }

    suspend fun setActiveCourseId(courseId: String?) {
        context.dataStore.edit { prefs ->
            if (courseId != null) {
                prefs[Keys.ACTIVE_COURSE_ID] = courseId
            } else {
                prefs.remove(Keys.ACTIVE_COURSE_ID)
            }
        }
    }

    suspend fun getRemainingBotMessages(): Int {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        var remaining = 20
        context.dataStore.edit { prefs ->
            val lastDate = prefs[Keys.BOT_LAST_RESET_DATE] ?: ""
            if (lastDate != today) {
                prefs[Keys.BOT_LAST_RESET_DATE] = today
                prefs[Keys.BOT_MESSAGES_COUNT] = 0
                remaining = 20
            } else {
                val used = prefs[Keys.BOT_MESSAGES_COUNT] ?: 0
                remaining = (20 - used).coerceAtLeast(0)
            }
        }
        return remaining
    }

    suspend fun incrementBotMessageCount() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        context.dataStore.edit { prefs ->
            val lastDate = prefs[Keys.BOT_LAST_RESET_DATE] ?: ""
            if (lastDate != today) {
                prefs[Keys.BOT_LAST_RESET_DATE] = today
                prefs[Keys.BOT_MESSAGES_COUNT] = 1
            } else {
                val used = prefs[Keys.BOT_MESSAGES_COUNT] ?: 0
                prefs[Keys.BOT_MESSAGES_COUNT] = used + 1
            }
        }
    }

    suspend fun recordStudySession(): Int {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        var newStreak = 1
        context.dataStore.edit { prefs ->
            val lastDate = prefs[Keys.LAST_STUDY_DATE] ?: ""
            val currentStreak = prefs[Keys.STREAK_COUNT] ?: 0
            if (lastDate == today) {
                newStreak = currentStreak
            } else if (isYesterday(lastDate, today)) {
                newStreak = currentStreak + 1
                prefs[Keys.STREAK_COUNT] = newStreak
                prefs[Keys.LAST_STUDY_DATE] = today
            } else {
                newStreak = 1
                prefs[Keys.STREAK_COUNT] = 1
                prefs[Keys.LAST_STUDY_DATE] = today
            }
        }
        return newStreak
    }

    private fun isYesterday(lastDate: String, today: String): Boolean {
        if (lastDate.isEmpty()) return false
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val last = sdf.parse(lastDate)?.time ?: return false
            val current = sdf.parse(today)?.time ?: return false
            val diffHours = (current - last) / (1000 * 60 * 60)
            diffHours in 18..36
        } catch (e: Exception) {
            false
        }
    }
}
