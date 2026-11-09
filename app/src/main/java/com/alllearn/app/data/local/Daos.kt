package com.alllearn.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun getUserFlow(userId: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUser(userId: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("UPDATE users SET streakCount = :streak, lastStudyDate = :date WHERE id = :userId")
    suspend fun updateStreak(userId: String, streak: Int, date: String)

    @Query("UPDATE users SET themeAccent = :theme, isDarkMode = :darkMode WHERE id = :userId")
    suspend fun updateTheme(userId: String, theme: String, darkMode: Boolean)
}

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses WHERE userId = :userId ORDER BY updatedAt DESC")
    fun getCoursesForUser(userId: String): Flow<List<CourseEntity>>

    @Query("SELECT * FROM courses WHERE id = :courseId LIMIT 1")
    suspend fun getCourseById(courseId: String): CourseEntity?

    @Query("SELECT * FROM courses WHERE id = :courseId LIMIT 1")
    fun getCourseFlowById(courseId: String): Flow<CourseEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: CourseEntity)

    @Query("UPDATE courses SET outlineJson = :outlineJson, updatedAt = :updatedAt WHERE id = :courseId")
    suspend fun updateCourseOutline(courseId: String, outlineJson: String, updatedAt: Long)

    @Query("UPDATE courses SET status = :status WHERE id = :courseId")
    suspend fun updateCourseStatus(courseId: String, status: String)

    @Query("DELETE FROM courses WHERE id = :courseId")
    suspend fun deleteCourse(courseId: String)
}

@Dao
interface LessonDao {
    @Query("SELECT * FROM lessons WHERE courseId = :courseId ORDER BY weekNumber ASC, id ASC")
    fun getLessonsForCourse(courseId: String): Flow<List<LessonEntity>>

    @Query("SELECT * FROM lessons WHERE id = :lessonId LIMIT 1")
    suspend fun getLessonById(lessonId: String): LessonEntity?

    @Query("SELECT * FROM lessons WHERE id = :lessonId LIMIT 1")
    fun getLessonFlowById(lessonId: String): Flow<LessonEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLesson(lesson: LessonEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(lessons: List<LessonEntity>)

    @Query("UPDATE lessons SET status = :status, completedAt = :completedAt WHERE id = :lessonId")
    suspend fun updateLessonStatus(lessonId: String, status: String, completedAt: Long?)

    @Query("UPDATE lessons SET quizScore = :score, quizTotal = :total, status = 'done', completedAt = :completedAt WHERE id = :lessonId")
    suspend fun saveQuizResult(lessonId: String, score: Int, total: Int, completedAt: Long)
}

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages WHERE courseId = :courseId ORDER BY timestamp ASC")
    fun getMessagesForCourse(courseId: String): Flow<List<ChatEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatEntity)

    @Query("DELETE FROM chat_messages WHERE courseId = :courseId")
    suspend fun clearChatHistory(courseId: String)
}
