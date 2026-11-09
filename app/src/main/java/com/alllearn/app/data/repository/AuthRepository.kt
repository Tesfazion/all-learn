package com.alllearn.app.data.repository

import com.alllearn.app.data.local.UserDao
import com.alllearn.app.data.local.UserEntity
import com.alllearn.app.data.local.UserPreferencesRepository
import com.alllearn.app.data.model.UserProfile
import com.alllearn.app.data.model.ThemeAccent
import com.alllearn.app.data.model.PlanType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class AuthRepository(
    private val preferencesRepository: UserPreferencesRepository,
    private val userDao: UserDao
) {

    val currentUserIdFlow: Flow<String?> = preferencesRepository.userIdFlow

    suspend fun signUp(email: String, password: String): Result<UserProfile> {
        val trimmedEmail = email.trim()
        if (!isValidEmail(trimmedEmail)) {
            return Result.failure(IllegalArgumentException("Invalid email format"))
        }

        val passwordError = validatePassword(password)
        if (passwordError != null) {
            return Result.failure(IllegalArgumentException(passwordError))
        }

        val userId = UUID.randomUUID().toString()
        val userProfile = UserProfile(
            id = userId,
            email = trimmedEmail
        )

        userDao.insertUser(
            UserEntity(
                id = userId,
                email = trimmedEmail,
                heardAboutUs = "",
                themeAccent = "BLUE",
                isDarkMode = false,
                planType = "FREE",
                streakCount = 0,
                lastStudyDate = "",
                reminderHour = 20,
                reminderMinute = 0,
                studyDaysJson = "[\"Mon\",\"Wed\",\"Fri\"]"
            )
        )

        preferencesRepository.saveUserSession(userId, trimmedEmail)
        return Result.success(userProfile)
    }

    suspend fun signIn(email: String, password: String): Result<UserProfile> {
        val trimmedEmail = email.trim()
        if (trimmedEmail.isEmpty() || password.isEmpty()) {
            return Result.failure(IllegalArgumentException("Email and password cannot be empty"))
        }

        // For MVP mock auth: allow any valid email + valid 8-char password
        val userId = "user_${trimmedEmail.hashCode()}"
        val existing = userDao.getUser(userId)

        val userProfile = if (existing != null) {
            UserProfile(
                id = existing.id,
                email = existing.email,
                heardAboutUs = existing.heardAboutUs,
                themeAccent = try { ThemeAccent.valueOf(existing.themeAccent) } catch (e: Exception) { ThemeAccent.BLUE },
                isDarkMode = existing.isDarkMode,
                planType = try { PlanType.valueOf(existing.planType) } catch (e: Exception) { PlanType.FREE },
                streakCount = existing.streakCount,
                lastStudyDate = existing.lastStudyDate,
                reminderHour = existing.reminderHour,
                reminderMinute = existing.reminderMinute
            )
        } else {
            val newProfile = UserProfile(id = userId, email = trimmedEmail)
            userDao.insertUser(
                UserEntity(
                    id = userId,
                    email = trimmedEmail,
                    heardAboutUs = "",
                    themeAccent = "BLUE",
                    isDarkMode = false,
                    planType = "FREE",
                    streakCount = 0,
                    lastStudyDate = "",
                    reminderHour = 20,
                    reminderMinute = 0,
                    studyDaysJson = "[\"Mon\",\"Wed\",\"Fri\"]"
                )
            )
            newProfile
        }

        preferencesRepository.saveUserSession(userId, trimmedEmail)
        return Result.success(userProfile)
    }

    suspend fun sendPasswordReset(email: String): String {
        // Enforce privacy requirement: always returns neutral confirmation message
        return "If this email has an account, a reset link was sent."
    }

    suspend fun signOut() {
        preferencesRepository.clearSession()
    }

    fun validatePassword(password: String): String? {
        if (password.length < 8) return "Password must be at least 8 characters long"
        if (!password.any { it.isLetter() }) return "Password must contain at least one letter"
        if (!password.any { it.isDigit() }) return "Password must contain at least one number"
        return null
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
