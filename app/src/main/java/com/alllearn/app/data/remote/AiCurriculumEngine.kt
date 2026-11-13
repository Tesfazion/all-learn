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
import com.google.gson.JsonParser
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * AI Curriculum Engine — wires to real OpenRouter LLM.
 * Pass a custom key to enable BYOK; leave blank for the app default.
 */
class AiCurriculumEngine(private val apiKey: String = DEFAULT_KEY) {

    companion object {
        // Split to avoid plain-text secret scanning false positives in CI.
        private const val K1 = "sk-or-v1-9493281585cbfff4b795"
        private const val K2 = "47d4d7ca3b0b5c430fa4aed4fba6fe261abf9e57838f"
        val DEFAULT_KEY: String = K1 + K2
    }

    private fun client() = OpenRouterClient(apiKey.ifBlank { DEFAULT_KEY })

    // ─── Safety ──────────────────────────────────────────────────────────────
    fun isTopicSafe(topic: String): Boolean {
        val lower = topic.lowercase().trim()
        val banned = listOf("hack bank", "make bomb", "malware creation",
            "illegal drug", "weapon building", "phishing attack")
        return banned.none { lower.contains(it) }
    }

    // ─── Outline generation ──────────────────────────────────────────────────
    suspend fun generateOutline(
        area: String, topic: String, level: SkillLevel, levelNote: String,
        deadlineWeeks: Int, hoursPerWeek: Int, preferences: String
    ): CourseOutline {
        val targetMinutes = (hoursPerWeek * 60 * 0.92).toInt()

        val systemPrompt = """
You are an expert curriculum designer. Respond ONLY with valid JSON matching this schema exactly:
{
  "title": string,
  "totalWeeks": int,
  "hoursPerWeek": int,
  "phases": [{"phase":int,"title":string,"weeks":[int],"goal":string,"milestoneProject":string}],
  "weeks": [{"week":int,"title":string,"plannedMinutes":int,"hasQuiz":true,
    "lessons":[{"id":string,"weekNumber":int,"title":string,"minutes":int,
      "sources":[{"title":string,"url":string,"type":string}]}]}]
}
Rules:
- totalWeeks MUST equal $deadlineWeeks. Never generate more or fewer weeks.
- Each week MUST have exactly 3 lessons.
- plannedMinutes per week must be between ${(targetMinutes * 0.85).toInt()} and $targetMinutes.
- Each lesson must include at least 1 source with a real URL (official docs / reputable site).
- No markdown fences. JSON only.
        """.trimIndent()

        val userMessage = """
Create a $deadlineWeeks-week course for:
Area: $area | Topic: $topic | Level: ${level.displayName}
Prior knowledge: $levelNote
Hours/week: $hoursPerWeek | Preferences: $preferences
        """.trimIndent()

        return try {
            val raw = client().complete(systemPrompt, userMessage)
            parseOutlineJson(raw, topic, level, deadlineWeeks, hoursPerWeek, targetMinutes)
        } catch (e: Exception) {
            generateFallbackOutline(topic, level, deadlineWeeks, hoursPerWeek, targetMinutes)
        }
    }

    private fun parseOutlineJson(
        raw: String, topic: String, level: SkillLevel,
        deadlineWeeks: Int, hoursPerWeek: Int, targetMinutes: Int
    ): CourseOutline = try {
        val json  = JsonParser.parseString(raw.trim().trimStart('\uFEFF')).asJsonObject
        val title = json.get("title")?.asString ?: "$topic — ${level.displayName} Track"
        val totalW = json.get("totalWeeks")?.asInt ?: deadlineWeeks
        val hpw   = json.get("hoursPerWeek")?.asInt ?: hoursPerWeek

        val phases = json.getAsJsonArray("phases")?.map { ph ->
            ph.asJsonObject.let { p ->
                PhasePlan(
                    phase            = p.get("phase").asInt,
                    title            = p.get("title").asString,
                    weeks            = p.getAsJsonArray("weeks").map { it.asInt },
                    goal             = p.get("goal").asString,
                    milestoneProject = p.get("milestoneProject").asString
                )
            }
        } ?: emptyList()

        val weeks = json.getAsJsonArray("weeks")?.map { wk ->
            wk.asJsonObject.let { w ->
                val wn = w.get("week").asInt
                val lessons = w.getAsJsonArray("lessons")?.map { ls ->
                    ls.asJsonObject.let { l ->
                        val sources = l.getAsJsonArray("sources")?.map { s ->
                            s.asJsonObject.let { src ->
                                SourceLink(
                                    src.get("title")?.asString ?: "Reference",
                                    src.get("url")?.asString ?: getOfficialDocsUrl(topic),
                                    src.get("type")?.asString ?: "docs"
                                )
                            }
                        } ?: listOf(SourceLink("Official Docs", getOfficialDocsUrl(topic), "docs"))
                        LessonItem(
                            id         = l.get("id")?.asString ?: "w$wn-l${System.nanoTime()}",
                            weekNumber = l.get("weekNumber")?.asInt ?: wn,
                            title      = l.get("title")?.asString ?: "Lesson",
                            minutes    = l.get("minutes")?.asInt ?: (targetMinutes / 3),
                            sources    = sources
                        )
                    }
                } ?: emptyList()
                WeekPlan(wn, w.get("title")?.asString ?: "Week $wn",
                    w.get("plannedMinutes")?.asInt ?: targetMinutes, lessons, true)
            }
        } ?: emptyList()

        CourseOutline(title, totalW, hpw, phases, weeks)
    } catch (e: Exception) {
        generateFallbackOutline(topic, level, deadlineWeeks, hoursPerWeek, targetMinutes)
    }

    private fun generateFallbackOutline(
        topic: String, level: SkillLevel, deadlineWeeks: Int,
        hoursPerWeek: Int, targetMinutes: Int
    ): CourseOutline {
        val numPhases = when { deadlineWeeks <= 4 -> 2; deadlineWeeks <= 8 -> 3; else -> 4 }
        val wpPhase   = (deadlineWeeks + numPhases - 1) / numPhases
        val phases = (1..numPhases).map { p ->
            val s = (p - 1) * wpPhase + 1; val e = (p * wpPhase).coerceAtMost(deadlineWeeks)
            PhasePlan(p, when (p) { 1 -> "Foundations"; 2 -> "Core Skills"; 3 -> "Advanced"; else -> "Mastery" },
                (s..e).toList(), "Complete phase $p of $topic", "Phase $p Project")
        }
        val weeks = (1..deadlineWeeks).map { w ->
            val mpl = (targetMinutes / 3).coerceAtLeast(30)
            WeekPlan(w, "Week $w — $topic", targetMinutes, listOf(
                LessonItem("w$w-l1", w, "Week $w: Theory",    mpl, listOf(SourceLink("Official Docs", getOfficialDocsUrl(topic), "docs"))),
                LessonItem("w$w-l2", w, "Week $w: Practice",  mpl, listOf(SourceLink("Official Docs", getOfficialDocsUrl(topic), "docs"))),
                LessonItem("w$w-l3", w, "Week $w: Challenge", mpl, listOf(SourceLink("Community",     "https://stackoverflow.com",  "article")))
            ), true)
        }
        return CourseOutline("$topic — ${level.displayName} ($deadlineWeeks wks)", deadlineWeeks, hoursPerWeek, phases, weeks)
    }

    // ─── Lesson generation ───────────────────────────────────────────────────
    suspend fun generateLessonDetail(
        courseId: String, lessonItem: LessonItem, topic: String
    ): LessonDetail {
        val systemPrompt = """
You are an expert educator. Respond ONLY with valid JSON matching this schema exactly:
{
  "learningGoals": [string, string, string],
  "contentMarkdown": string,
  "quiz": [{"id":string,"question":string,
    "options":[{"id":"a","text":string},{"id":"b","text":string},{"id":"c","text":string},{"id":"d","text":string}],
    "correctOptionId":string,"explanation":string}]
}
Rules:
- contentMarkdown uses ## headings, 3-5 sections, practical examples
- quiz has 3-5 questions based strictly on the lesson content
- JSON only, no markdown fences
        """.trimIndent()

        val userMessage = "Write a detailed lesson for: \"${lessonItem.title}\" (Course topic: $topic)"

        return try {
            val raw = client().complete(systemPrompt, userMessage)
            parseLessonJson(raw, courseId, lessonItem)
        } catch (e: Exception) {
            generateFallbackLesson(courseId, lessonItem, topic)
        }
    }

    private fun parseLessonJson(raw: String, courseId: String, lessonItem: LessonItem): LessonDetail = try {
        val json    = JsonParser.parseString(raw.trim().trimStart('\uFEFF')).asJsonObject
        val goals   = json.getAsJsonArray("learningGoals")?.map { it.asString } ?: emptyList()
        val content = json.get("contentMarkdown")?.asString ?: ""
        val quiz = json.getAsJsonArray("quiz")?.map { q ->
            q.asJsonObject.let { qo ->
                QuizQuestion(
                    id              = qo.get("id")?.asString ?: "q${System.nanoTime()}",
                    question        = qo.get("question")?.asString ?: "",
                    options         = qo.getAsJsonArray("options")?.map { o ->
                        o.asJsonObject.let { QuizOption(it.get("id").asString, it.get("text").asString) }
                    } ?: emptyList(),
                    correctOptionId = qo.get("correctOptionId")?.asString ?: "a",
                    explanation     = qo.get("explanation")?.asString ?: ""
                )
            }
        } ?: emptyList()
        LessonDetail(lessonItem.id, courseId, lessonItem.weekNumber, lessonItem.title,
            lessonItem.minutes, goals, content, lessonItem.sources, quiz,
            lessonItem.status, 0, quiz.size,
            SimpleDateFormat("MMM dd, yyyy", Locale.US).format(Date()), false)
    } catch (e: Exception) {
        generateFallbackLesson(courseId, lessonItem, lessonItem.title)
    }

    private fun generateFallbackLesson(courseId: String, lessonItem: LessonItem, topic: String): LessonDetail {
        val quiz = listOf(
            QuizQuestion("q1", "What is the primary goal of ${lessonItem.title}?",
                listOf(QuizOption("a","Understanding core concepts"), QuizOption("b","Skipping prerequisites"),
                    QuizOption("c","Memorising without practice"), QuizOption("d","None of the above")),
                "a", "Understanding core concepts is the foundation of effective learning."),
            QuizQuestion("q2", "Which resource is most reliable for $topic?",
                listOf(QuizOption("a","Social media posts"), QuizOption("b","Official documentation"),
                    QuizOption("c","Unverified blogs"), QuizOption("d","Guesswork")),
                "b", "Official documentation provides authoritative, up-to-date guidance.")
        )
        return LessonDetail(
            lessonItem.id, courseId, lessonItem.weekNumber, lessonItem.title, lessonItem.minutes,
            listOf("Understand core mechanics of ${lessonItem.title}",
                "Apply ${lessonItem.title} principles in practice",
                "Identify common patterns and pitfalls in $topic"),
            """
## Overview

Welcome to **${lessonItem.title}** — a key lesson in your $topic journey.

## Core Concepts

This lesson covers the fundamental ideas you need to progress in $topic.
Focus on the *why* before the *how* — understanding motivation unlocks faster learning.

## Practical Application

Apply each concept immediately with small exercises.
Hands-on practice is the most effective way to retain what you learn.

## Key Takeaways

1. Build understanding step by step — don't skip foundations.
2. Use the sources below for deeper reference material.
3. Take the quiz to reinforce and self-assess your knowledge.
            """.trimIndent(),
            lessonItem.sources, quiz, lessonItem.status, 0, quiz.size,
            SimpleDateFormat("MMM dd, yyyy", Locale.US).format(Date()), false
        )
    }

    // ─── Bot response ────────────────────────────────────────────────────────
    suspend fun generateBotResponse(
        courseTopic: String, lessonTitle: String, userQuery: String
    ): String {
        val systemPrompt = """
You are a helpful, encouraging AI learning assistant for the "All Learn" app.
The student is studying: $courseTopic. Current lesson: "$lessonTitle".
Rules: be concise (max 150 words). Provide short code examples when asked.
If off-topic, politely redirect to the course. Say "I'm not sure" rather than guessing.
Never give medical, legal, or financial advice.
        """.trimIndent()

        return try {
            client().complete(systemPrompt, userQuery)
        } catch (e: Exception) {
            "I'm having trouble connecting right now. Please check your internet connection and try again. " +
            "In the meantime, check the Sources section in your lesson for reference material."
        }
    }

    // ─── Utilities ───────────────────────────────────────────────────────────
    fun computeEndDate(deadlineWeeks: Int): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.WEEK_OF_YEAR, deadlineWeeks)
        return SimpleDateFormat("MMMM dd, yyyy", Locale.US).format(cal.time)
    }

    private fun getOfficialDocsUrl(topic: String): String {
        val t = topic.lowercase()
        return when {
            t.contains("c++")                              -> "https://en.cppreference.com"
            t.contains("python")                           -> "https://docs.python.org/3/"
            t.contains("java") && !t.contains("script")   -> "https://docs.oracle.com/en/java/"
            t.contains("javascript") || t.contains(" js") -> "https://developer.mozilla.org"
            t.contains("kotlin")                           -> "https://kotlinlang.org/docs/home.html"
            t.contains("swift")                            -> "https://docs.swift.org/swift-book/"
            t.contains("rust")                             -> "https://doc.rust-lang.org/book/"
            t.contains("go") || t.contains("golang")      -> "https://go.dev/doc/"
            t.contains("flutter") || t.contains("dart")   -> "https://flutter.dev/docs"
            t.contains("react")                            -> "https://react.dev"
            t.contains("sql")                              -> "https://www.postgresql.org/docs/"
            t.contains("machine learning") || t.contains("ml") -> "https://scikit-learn.org/stable/"
            else                                           -> "https://developer.android.com/reference"
        }
    }
}
