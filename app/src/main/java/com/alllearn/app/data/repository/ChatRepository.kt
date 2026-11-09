package com.alllearn.app.data.repository

import com.alllearn.app.data.local.ChatDao
import com.alllearn.app.data.local.ChatEntity
import com.alllearn.app.data.local.UserPreferencesRepository
import com.alllearn.app.data.model.ChatMessage
import com.alllearn.app.data.model.MessageRole
import com.alllearn.app.data.remote.AiCurriculumEngine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class ChatRepository(
    private val chatDao: ChatDao,
    private val aiCurriculumEngine: AiCurriculumEngine,
    private val userPreferencesRepository: UserPreferencesRepository
) {

    fun getChatMessagesFlow(courseId: String): Flow<List<ChatMessage>> {
        return chatDao.getMessagesForCourse(courseId).map { list ->
            list.map { entity ->
                ChatMessage(
                    id = entity.id,
                    courseId = entity.courseId,
                    lessonId = entity.lessonId,
                    role = if (entity.role == "USER") MessageRole.USER else MessageRole.ASSISTANT,
                    text = entity.text,
                    timestamp = entity.timestamp
                )
            }
        }
    }

    suspend fun sendMessage(
        courseId: String,
        lessonId: String,
        courseTopic: String,
        lessonTitle: String,
        userQuery: String
    ): Result<ChatMessage> {
        val remaining = userPreferencesRepository.getRemainingBotMessages()
        if (remaining <= 0) {
            return Result.failure(IllegalStateException("Daily free message limit reached"))
        }

        val userMessageId = UUID.randomUUID().toString()
        val userMsg = ChatEntity(
            id = userMessageId,
            courseId = courseId,
            lessonId = lessonId,
            role = "USER",
            text = userQuery,
            timestamp = System.currentTimeMillis()
        )
        chatDao.insertMessage(userMsg)
        userPreferencesRepository.incrementBotMessageCount()

        val botResponseText = aiCurriculumEngine.generateBotResponse(
            courseTopic = courseTopic,
            lessonTitle = lessonTitle,
            userQuery = userQuery
        )

        val botMessageId = UUID.randomUUID().toString()
        val botMsg = ChatEntity(
            id = botMessageId,
            courseId = courseId,
            lessonId = lessonId,
            role = "ASSISTANT",
            text = botResponseText,
            timestamp = System.currentTimeMillis()
        )
        chatDao.insertMessage(botMsg)

        return Result.success(
            ChatMessage(
                id = botMessageId,
                courseId = courseId,
                lessonId = lessonId,
                role = MessageRole.ASSISTANT,
                text = botResponseText,
                timestamp = botMsg.timestamp
            )
        )
    }

    suspend fun getRemainingMessages(): Int {
        return userPreferencesRepository.getRemainingBotMessages()
    }
}
