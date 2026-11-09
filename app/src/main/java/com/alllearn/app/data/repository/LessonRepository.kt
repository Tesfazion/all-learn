package com.alllearn.app.data.repository

import com.alllearn.app.data.local.LessonDao
import com.alllearn.app.data.local.LessonEntity
import com.alllearn.app.data.model.LessonDetail
import com.alllearn.app.data.model.LessonItem
import com.alllearn.app.data.model.QuizQuestion
import com.alllearn.app.data.model.SourceLink
import com.alllearn.app.data.remote.AiCurriculumEngine
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LessonRepository(
    private val lessonDao: LessonDao,
    private val aiCurriculumEngine: AiCurriculumEngine
) {
    private val gson = Gson()

    fun getLessonsForCourse(courseId: String): Flow<List<LessonDetail>> {
        return lessonDao.getLessonsForCourse(courseId).map { list ->
            list.map { entityToLessonDetail(it) }
        }
    }

    fun getLessonFlow(lessonId: String): Flow<LessonDetail?> {
        return lessonDao.getLessonFlowById(lessonId).map { entity ->
            entity?.let { entityToLessonDetail(it) }
        }
    }

    suspend fun getOrCreateLessonDetail(
        courseId: String,
        lessonItem: LessonItem,
        topic: String
    ): LessonDetail {
        val existing = lessonDao.getLessonById(lessonItem.id)
        if (existing != null) {
            return entityToLessonDetail(existing)
        }

        val generated = aiCurriculumEngine.generateLessonDetail(
            courseId = courseId,
            lessonItem = lessonItem,
            topic = topic
        )

        lessonDao.insertLesson(lessonDetailToEntity(generated))
        return generated
    }

    suspend fun markLessonCompleted(lessonId: String) {
        lessonDao.updateLessonStatus(lessonId, "done", System.currentTimeMillis())
    }

    suspend fun saveQuizResult(lessonId: String, score: Int, total: Int) {
        lessonDao.saveQuizResult(lessonId, score, total, System.currentTimeMillis())
    }

    private fun lessonDetailToEntity(lesson: LessonDetail): LessonEntity {
        return LessonEntity(
            id = lesson.id,
            courseId = lesson.courseId,
            weekNumber = lesson.weekNumber,
            title = lesson.title,
            minutes = lesson.minutes,
            learningGoalsJson = gson.toJson(lesson.learningGoals),
            contentMarkdown = lesson.contentMarkdown,
            sourcesJson = gson.toJson(lesson.sources),
            quizJson = gson.toJson(lesson.quiz),
            status = lesson.status,
            quizScore = lesson.quizScore,
            quizTotal = lesson.quizTotal,
            lastVerified = lesson.lastVerified,
            needsReview = lesson.needsReview,
            completedAt = lesson.completedAt
        )
    }

    private fun entityToLessonDetail(entity: LessonEntity): LessonDetail {
        val goalsType = object : TypeToken<List<String>>() {}.type
        val sourcesType = object : TypeToken<List<SourceLink>>() {}.type
        val quizType = object : TypeToken<List<QuizQuestion>>() {}.type

        val goals: List<String> = try { gson.fromJson(entity.learningGoalsJson, goalsType) } catch (e: Exception) { emptyList() }
        val sources: List<SourceLink> = try { gson.fromJson(entity.sourcesJson, sourcesType) } catch (e: Exception) { emptyList() }
        val quiz: List<QuizQuestion> = try { gson.fromJson(entity.quizJson, quizType) } catch (e: Exception) { emptyList() }

        return LessonDetail(
            id = entity.id,
            courseId = entity.courseId,
            weekNumber = entity.weekNumber,
            title = entity.title,
            minutes = entity.minutes,
            learningGoals = goals,
            contentMarkdown = entity.contentMarkdown,
            sources = sources,
            quiz = quiz,
            status = entity.status,
            quizScore = entity.quizScore,
            quizTotal = entity.quizTotal,
            lastVerified = entity.lastVerified,
            needsReview = entity.needsReview,
            completedAt = entity.completedAt
        )
    }
}
