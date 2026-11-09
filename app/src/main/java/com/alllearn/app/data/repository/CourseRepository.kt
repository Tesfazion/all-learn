package com.alllearn.app.data.repository

import com.alllearn.app.data.local.CourseDao
import com.alllearn.app.data.local.CourseEntity
import com.alllearn.app.data.local.UserPreferencesRepository
import com.alllearn.app.data.model.Course
import com.alllearn.app.data.model.CourseOutline
import com.alllearn.app.data.model.CourseStatus
import com.alllearn.app.data.model.SkillLevel
import com.alllearn.app.data.remote.AiCurriculumEngine
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class CourseRepository(
    private val courseDao: CourseDao,
    private val aiCurriculumEngine: AiCurriculumEngine,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    private val gson = Gson()

    fun getCoursesForUser(userId: String): Flow<List<Course>> {
        return courseDao.getCoursesForUser(userId).map { list ->
            list.map { entityToCourse(it) }
        }
    }

    fun getCourseFlow(courseId: String): Flow<Course?> {
        return courseDao.getCourseFlowById(courseId).map { entity ->
            entity?.let { entityToCourse(it) }
        }
    }

    suspend fun getCourseById(courseId: String): Course? {
        val entity = courseDao.getCourseById(courseId) ?: return null
        return entityToCourse(entity)
    }

    suspend fun generateOutline(
        userId: String,
        area: String,
        topic: String,
        level: SkillLevel,
        levelNote: String,
        deadlineWeeks: Int,
        hoursPerWeek: Int,
        preferences: String
    ): Result<Course> {
        return try {
            val outline = aiCurriculumEngine.generateOutline(
                area = area,
                topic = topic,
                level = level,
                levelNote = levelNote,
                deadlineWeeks = deadlineWeeks,
                hoursPerWeek = hoursPerWeek,
                preferences = preferences
            )

            val courseId = UUID.randomUUID().toString()
            val targetEndDate = aiCurriculumEngine.computeEndDate(deadlineWeeks)

            val course = Course(
                id = courseId,
                userId = userId,
                area = area,
                topic = topic,
                level = level,
                levelNote = levelNote,
                deadlineWeeks = deadlineWeeks,
                hoursPerWeek = hoursPerWeek,
                targetEndDate = targetEndDate,
                status = CourseStatus.ACTIVE,
                outline = outline,
                regenerationsUsed = 0
            )

            val entity = courseToEntity(course)
            courseDao.insertCourse(entity)
            userPreferencesRepository.setActiveCourseId(courseId)

            Result.success(course)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun regenerateOutline(
        course: Course,
        changePrompt: String
    ): Result<Course> {
        if (course.regenerationsUsed >= 3) {
            return Result.failure(IllegalStateException("Maximum regenerations reached for free tier"))
        }

        return try {
            val newOutline = aiCurriculumEngine.generateOutline(
                area = course.area,
                topic = course.topic,
                level = course.level,
                levelNote = course.levelNote,
                deadlineWeeks = course.deadlineWeeks,
                hoursPerWeek = course.hoursPerWeek,
                preferences = changePrompt
            )

            val updatedCourse = course.copy(
                outline = newOutline,
                regenerationsUsed = course.regenerationsUsed + 1,
                updatedAt = System.currentTimeMillis()
            )

            courseDao.insertCourse(courseToEntity(updatedCourse))
            Result.success(updatedCourse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun applyPlanAdjustment(
        courseId: String,
        isCompress: Boolean
    ): Result<Course> {
        val course = getCourseById(courseId) ?: return Result.failure(IllegalArgumentException("Course not found"))

        val newWeeks = if (isCompress) {
            // Compress: streamline remaining weeks while keeping total weeks same
            course.outline.weeks
        } else {
            // Extend: move deadline out by 1 week
            val newDeadline = course.deadlineWeeks + 1
            val updatedOutline = aiCurriculumEngine.generateOutline(
                area = course.area,
                topic = course.topic,
                level = course.level,
                levelNote = course.levelNote,
                deadlineWeeks = newDeadline,
                hoursPerWeek = course.hoursPerWeek,
                preferences = "Extended timeline due to lower study speed"
            )
            val updatedCourse = course.copy(
                deadlineWeeks = newDeadline,
                targetEndDate = aiCurriculumEngine.computeEndDate(newDeadline),
                outline = updatedOutline,
                updatedAt = System.currentTimeMillis()
            )
            courseDao.insertCourse(courseToEntity(updatedCourse))
            return Result.success(updatedCourse)
        }

        val updatedCourse = course.copy(updatedAt = System.currentTimeMillis())
        courseDao.insertCourse(courseToEntity(updatedCourse))
        return Result.success(updatedCourse)
    }

    private fun courseToEntity(course: Course): CourseEntity {
        return CourseEntity(
            id = course.id,
            userId = course.userId,
            area = course.area,
            topic = course.topic,
            levelKey = course.level.key,
            levelNote = course.levelNote,
            deadlineWeeks = course.deadlineWeeks,
            hoursPerWeek = course.hoursPerWeek,
            targetEndDate = course.targetEndDate,
            status = course.status.name,
            outlineJson = gson.toJson(course.outline),
            regenerationsUsed = course.regenerationsUsed,
            createdAt = course.createdAt,
            updatedAt = course.updatedAt
        )
    }

    private fun entityToCourse(entity: CourseEntity): Course {
        val outline = try {
            gson.fromJson(entity.outlineJson, CourseOutline::class.java)
        } catch (e: Exception) {
            CourseOutline("Error Course", 1, 5, emptyList(), emptyList())
        }

        val level = try {
            SkillLevel.entries.first { it.key == entity.levelKey }
        } catch (e: Exception) {
            SkillLevel.BEGINNER
        }

        val status = try {
            CourseStatus.valueOf(entity.status)
        } catch (e: Exception) {
            CourseStatus.ACTIVE
        }

        return Course(
            id = entity.id,
            userId = entity.userId,
            area = entity.area,
            topic = entity.topic,
            level = level,
            levelNote = entity.levelNote,
            deadlineWeeks = entity.deadlineWeeks,
            hoursPerWeek = entity.hoursPerWeek,
            targetEndDate = entity.targetEndDate,
            status = status,
            outline = outline,
            regenerationsUsed = entity.regenerationsUsed,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
}
