package com.example.pawvet_1.data.repository

import com.example.pawvet_1.data.remote.GeminiApiService
import com.example.pawvet_1.data.remote.model.Content
import com.example.pawvet_1.data.remote.model.GeminiRequest
import com.example.pawvet_1.data.remote.model.Part

class GeminiRepository(private val geminiApiService: GeminiApiService) {
    private val apiKey = "apikey"



    suspend fun getAiResponse(prompt: String): String {
        return try {
            val request = GeminiRequest(
                contents = listOf(
                    Content(parts = listOf(Part(text = prompt)))
                )
            )
            val response = geminiApiService.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "No se recibió respuesta de la IA."
        } catch (e: Exception) {
            e.printStackTrace()
            "Error al consultar la IA: ${e.message}"
        }
    }
}
