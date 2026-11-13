package com.alllearn.app.data.remote

import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * OpenRouter AI REST client.
 * The API key is injected at construction — never hardcoded here.
 */
class OpenRouterClient(private val apiKey: String) {

    companion object {
        private const val BASE_URL  = "https://openrouter.ai/api/v1/chat/completions"
        private const val MODEL     = "mistralai/mistral-7b-instruct"
        private const val JSON_MIME = "application/json"
        private const val APP_NAME  = "All Learn"
    }

    private val gson   = Gson()
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.NONE
        })
        .build()

    /** Send messages and return the assistant reply text. Throws on error. */
    suspend fun complete(systemPrompt: String, userMessage: String): String {
        val messagesArray = buildString {
            append("[")
            if (systemPrompt.isNotBlank()) {
                append("""{"role":"system","content":${gson.toJson(systemPrompt)}}""")
                append(",")
            }
            append("""{"role":"user","content":${gson.toJson(userMessage)}}""")
            append("]")
        }

        val body = """
            {
              "model": "$MODEL",
              "messages": $messagesArray,
              "max_tokens": 1024,
              "temperature": 0.7
            }
        """.trimIndent()

        val request = Request.Builder()
            .url(BASE_URL)
            .header("Authorization", "Bearer $apiKey")
            .header("HTTP-Referer", "https://alllearn.app")
            .header("X-Title", APP_NAME)
            .post(body.toRequestBody(JSON_MIME.toMediaType()))
            .build()

        // OkHttp execute is blocking — call from IO coroutine dispatcher
        val response     = client.newCall(request).execute()
        val responseBody = response.body?.string()
            ?: throw IllegalStateException("Empty response body from OpenRouter")

        if (!response.isSuccessful) {
            throw IllegalStateException("OpenRouter error ${response.code}: $responseBody")
        }

        val json = gson.fromJson(responseBody, JsonObject::class.java)
        return json
            .getAsJsonArray("choices")
            .get(0).asJsonObject
            .getAsJsonObject("message")
            .get("content").asString
            .trim()
    }
}
