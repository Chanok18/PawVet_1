package com.example.pawvet_1.data.repository

import com.example.pawvet_1.data.remote.DogApiService

/**
 * [PALABRAS CLAVE - REPOSITORY API]
 * - ABSTRACCIÓN: Al ViewModel no le importa si los datos vienen de Retrofit o de una DB.
 * - MAPEO: Transforma la respuesta cruda de la API en una lista simple para la UI.
 */
class BreedsRepository(private val dogApiService: DogApiService) {
    suspend fun getBreeds(): List<String> {
        // Hacemos la petición a través del servicio de Retrofit
        val response = dogApiService.getBreeds()
        return response.breeds.keys.toList()
    }

    suspend fun getServiceImages(): List<String> {
        val response = dogApiService.getRandomImages()
        return response.images
    }
}
