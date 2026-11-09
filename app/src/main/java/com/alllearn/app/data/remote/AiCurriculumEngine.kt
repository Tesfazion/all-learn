package com.alllearn.app.data.remote

import com.alllearn.app.data.model.CourseOutline
import com.alllearn.app.data.model.LessonDetail
import com.alllearn.app.data.model.LessonItem
import com.alllearn.app.data.model.PhasePlan
import com.alllearn.app.data.model.QuizOption
import com.alllearn.app.data.model.QuizQuestion
import com.alllearn.app.data.model.SkillLevel
import com.alllearn.app.data.model.SourceLink
import com.alllearn.app.data.model.WeekPlan
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

class AiCurriculumEngine {

    // Reject harmful or illegal topics
    fun isTopicSafe(topic: String): Boolean {
        val lower = topic.lowercase().trim()
        val bannedWords = listOf("hack bank", "make bomb", "malware creation", "illegal drug", "weapon building", "phishing attack")
        return bannedWords.none { lower.contains(it) }
    }

    suspend fun generateOutline(
        area: String,
        topic: String,
        level: SkillLevel,
        levelNote: String,
        deadlineWeeks: Int,
        hoursPerWeek: Int,
        preferences: String
    ): CourseOutline {
        // Simulate Network/AI latency
        delay(1800)

        val targetWeeklyMinutes = (hoursPerWeek * 60 * 0.92).toInt() // ~92% of target
        val title = "$topic for ${level.key.replaceFirstChar { it.uppercase() }}s in $deadlineWeeks Weeks"

        val phases = mutableListOf<PhasePlan>()
        val weeks = mutableListOf<WeekPlan>()

        val numPhases = when {
            deadlineWeeks <= 4 -> 2
            deadlineWeeks <= 8 -> 3
            else -> 4
        }

        val weeksPerPhase = (deadlineWeeks + numPhases - 1) / numPhases

        for (p in 1..numPhases) {
            val startWeek = (p - 1) * weeksPerPhase + 1
            val endWeek = (p * weeksPerPhase).coerceAtMost(deadlineWeeks)
            val phaseWeeks = (startWeek..endWeek).toList()

            val phaseTitle = when (p) {
                1 -> "Foundations & Environment Setup"
                2 -> "Core Concepts & Practical Exercises"
                3 -> "Intermediate Application & Patterns"
                else -> "Advanced Mastery & Capstone Project"
            }

            val milestone = when (p) {
                1 -> "Simple $topic Baseline Application"
                2 -> "Interactive $topic Functional Module"
                3 -> "Data-driven $topic Service"
                else -> "Complete Production-ready $topic Capstone"
            }

            phases.add(
                PhasePlan(
                    phase = p,
                    title = phaseTitle,
                    weeks = phaseWeeks,
                    goal = "Master phase $p competencies for $topic with hands-on practice",
                    milestoneProject = milestone
                )
            )
        }

        for (w in 1..deadlineWeeks) {
            val weekTitle = when (w) {
                1 -> "Environment Setup, Syntax & First $topic Program"
                2 -> "Variables, Data Types & Control Statements"
                3 -> "Functions, Scope & Modular Code"
                4 -> "Object-Oriented / Structural Design in $topic"
                deadlineWeeks -> "Final Review, Optimization & Capstone Polish"
                else -> "Deep Dive into $topic Module #$w"
            }

            // Create 3 lessons per week matching target minutes
            val minsPerLesson = (targetWeeklyMinutes / 3).coerceAtLeast(30)
            val lessons = listOf(
                LessonItem(
                    id = "w$w-l1",
                    weekNumber = w,
                    title = "$weekTitle - Core Theory",
                    minutes = minsPerLesson,
                    sources = listOf(
                        SourceLink("Official $topic Documentation", getOfficialDocsUrl(topic), "docs"),
                        SourceLink("Open Educational Reference - $topic", "https://developer.mozilla.org", "article")
                    )
                ),
                LessonItem(
                    id = "w$w-l2",
                    weekNumber = w,
                    title = "$weekTitle - Hands-on Examples",
                    minutes = minsPerLesson,
                    sources = listOf(
                        SourceLink("Standard $topic Specification", getOfficialDocsUrl(topic), "docs")
                    )
                ),
                LessonItem(
                    id = "w$w-l3",
                    weekNumber = w,
                    title = "$weekTitle - Practice & Challenge",
                    minutes = minsPerLesson,
                    sources = listOf(
                        SourceLink("$topic Community Guide", "https://stackoverflow.com", "article")
                    )
                )
            )

            weeks.add(
                WeekPlan(
                    week = w,
                    title = weekTitle,
                    plannedMinutes = targetWeeklyMinutes,
                    lessons = lessons,
                    hasQuiz = true
                )
            )
        }

        return CourseOutline(
            title = title,
            totalWeeks = deadlineWeeks,
            hoursPerWeek = hoursPerWeek,
            phases = phases,
            weeks = weeks
        )
    }

    suspend fun generateLessonDetail(
        courseId: String,
        lessonItem: LessonItem,
        topic: String
    ): LessonDetail {
        delay(1200)

        val goals = listOf(
            "Understand the core mechanics of ${lessonItem.title}",
            "Apply ${lessonItem.title} principles in clean, readable code/examples",
            "Identify common errors and debug issues related to $topic"
        )

        val markdownContent = """
            # ${lessonItem.title}
            
            Welcome to this grounded lesson on **$topic**. In this unit, we break down essential concepts, practical syntax, and verified industry best practices.
            
            ## 1. Core Principles
            
            When working with **$topic**, understanding structured execution is vital:
            * **Precision:** Always follow standard style guides and documentation conventions.
            * **Efficiency:** Focus on algorithm time and space complexity where applicable.
            * **Maintainability:** Keep components or modules concise and single-purpose.
            
            ```kotlin
            // Practical Code Example in $topic Context
            fun executeTopicTask(input: String): Boolean {
                println("Executing $topic task for input: ${'$'}input")
                return input.isNotBlank()
            }
            ```
            
            ## 2. Key Takeaways
            
            1. Validate inputs early to prevent unexpected runtime failures.
            2. Consult the trusted sources below for full API reference details.
            3. Practice with the lesson quiz to reinforce your knowledge.
        """.trimIndent()

        val quiz = listOf(
            QuizQuestion(
                id = "q1",
                question = "What is the primary goal of ${lessonItem.title} in $topic?",
                options = listOf(
                    QuizOption("a", "To write unorganized code as fast as possible"),
                    QuizOption("b", "To establish clean, verified, and maintainable concepts"),
                    QuizOption("c", "To bypass compiler and syntax verification"),
                    QuizOption("d", "None of the above")
                ),
                correctOptionId = "b",
                explanation = "Clean structure and verified fundamentals ensure long-term code quality and error-free execution."
            ),
            QuizQuestion(
                id = "q2",
                question = "Which trusted source should you consult first for official $topic behavior?",
                options = listOf(
                    QuizOption("a", "Random unverified forum post"),
                    QuizOption("b", "Official Documentation & Standard Specifications"),
                    QuizOption("c", "Social media comments"),
                    QuizOption("d", "Guesswork")
                ),
                correctOptionId = "b",
                explanation = "Official specifications and docs provide the most authoritative and up-to-date guidance."
            )
        )

        val today = SimpleDateFormat("MMM dd, yyyy", Locale.US).format(Date())

        return LessonDetail(
            id = lessonItem.id,
            courseId = courseId,
            weekNumber = lessonItem.weekNumber,
            title = lessonItem.title,
            minutes = lessonItem.minutes,
            learningGoals = goals,
            contentMarkdown = markdownContent,
            sources = lessonItem.sources,
            quiz = quiz,
            status = lessonItem.status,
            quizScore = 0,
            quizTotal = quiz.size,
            lastVerified = today,
            needsReview = false
        )
    }

    suspend fun generateBotResponse(
        courseTopic: String,
        lessonTitle: String,
        userQuery: String
    ): String {
        delay(800)
        val queryLower = userQuery.lowercase()

        return when {
            queryLower.contains("hello") || queryLower.contains("hi") -> {
                "Hello! I'm your 24/7 AI Helper Bot for **$courseTopic**. How can I help you with **$lessonTitle** today?"
            }
            queryLower.contains("example") || queryLower.contains("code") -> {
                "Here is a quick code example related to **$lessonTitle**:\n\n```kotlin\n// Example for $courseTopic\nval isReady = true\nif (isReady) {\n    println(\"Successfully applying $lessonTitle!\")\n}\n```\nFeel free to ask if you'd like a step-by-step breakdown!"
            }
            queryLower.contains("source") || queryLower.contains("docs") -> {
                "For verified details on **$courseTopic**, check the 'Sources' section at the bottom of the current lesson page. Official documentation is always the best primary reference!"
            }
            else -> {
                "In the context of **$lessonTitle** ($courseTopic): $userQuery\n\nKey recommendation: Focus on understanding the foundational principles introduced in this lesson and try modifying the practical examples to test your knowledge."
            }
        }
    }

    fun computeEndDate(deadlineWeeks: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.WEEK_OF_YEAR, deadlineWeeks)
        val sdf = SimpleDateFormat("MMMM dd, yyyy", Locale.US)
        return sdf.format(calendar.time)
    }

    private fun getOfficialDocsUrl(topic: String): String {
        val t = topic.lowercase()
        return when {
            t.contains("c++") -> "https://en.cppreference.com"
            t.contains("python") -> "https://docs.python.org/3/"
            t.contains("java") -> "https://docs.oracle.com/en/java/"
            t.contains("js") || t.contains("javascript") -> "https://developer.mozilla.org"
            t.contains("kotlin") -> "https://kotlinlang.org/docs/home.html"
            else -> "https://developer.android.com/reference"
        }
    }
}
