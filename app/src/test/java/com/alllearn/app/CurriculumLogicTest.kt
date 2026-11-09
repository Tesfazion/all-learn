package com.alllearn.app

import com.alllearn.app.data.model.SkillLevel
import com.alllearn.app.data.remote.AiCurriculumEngine
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CurriculumLogicTest {

    private lateinit var aiEngine: AiCurriculumEngine

    @Before
    fun setup() {
        aiEngine = AiCurriculumEngine()
    }

    @Test
    fun testOutlineGeneration_respectsDeadlineAndWeeklyMinutes() = runBlocking {
        val deadlineWeeks = 12
        val hoursPerWeek = 7
        val outline = aiEngine.generateOutline(
            area = "Programming",
            topic = "C++",
            level = SkillLevel.BEGINNER,
            levelNote = "No prior experience",
            deadlineWeeks = deadlineWeeks,
            hoursPerWeek = hoursPerWeek,
            preferences = ""
        )

        assertEquals(deadlineWeeks, outline.totalWeeks)
        assertEquals(hoursPerWeek, outline.hoursPerWeek)
        assertEquals(deadlineWeeks, outline.weeks.size)

        // Check that weekly planned minutes are between 85% and 100% of hoursPerWeek * 60
        val targetMinutes = hoursPerWeek * 60
        val minAllowed = (targetMinutes * 0.85).toInt()
        val maxAllowed = targetMinutes

        outline.weeks.forEach { week ->
            assertTrue(
                "Week ${week.week} minutes (${week.plannedMinutes}) must be >= $minAllowed and <= $maxAllowed",
                week.plannedMinutes in minAllowed..maxAllowed
            )
            assertTrue("Every week must have at least one lesson", week.lessons.isNotEmpty())
            week.lessons.forEach { lesson ->
                assertTrue("Lesson source links must not be empty", lesson.sources.isNotEmpty())
            }
        }
    }

    @Test
    fun testTopicSafetyCheck() {
        assertTrue(aiEngine.isTopicSafe("Kotlin Android Development"))
        assertTrue(aiEngine.isTopicSafe("Data Structures & Algorithms"))
        assertFalse(aiEngine.isTopicSafe("How to make bomb at home"))
    }

    @Test
    fun testComputeEndDate() {
        val dateString = aiEngine.computeEndDate(4)
        assertNotNull(dateString)
        assertTrue(dateString.isNotBlank())
    }
}
