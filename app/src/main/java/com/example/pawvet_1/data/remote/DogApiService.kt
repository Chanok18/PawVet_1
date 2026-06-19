package com.example.pawvet_1.data.remote

import com.example.pawvet_1.data.remote.model.DogBreedResponse
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * RETROFIT
 * - INTERFACE: Define las "Rutas" o Endpoints de la API.
 * - @GET: Indica que vamos a pedir/traer datos de internet.
 * - GSON: Convierte el JSON (texto) de internet a clases de Kotlin.
 */

data class DogImagesResponse(
    @SerializedName("message") val images: List<String>,
    @SerializedName("status") val status: String
)

interface DogApiService {
    @GET("breeds/list/all")
    suspend fun getBreeds(): DogBreedResponse

    @GET("breeds/image/random/10")
    suspend fun getRandomImages(): DogImagesResponse
}

object RetrofitClient {
    // - BASEURL: La dirección principal del servidor.
    private const val BASE_URL = "https://dog.ceo/api/"

    val dogApiService: DogApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DogApiService::class.java)
    }
}
