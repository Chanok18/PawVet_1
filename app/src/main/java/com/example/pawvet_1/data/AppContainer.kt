package com.example.pawvet_1.data

import android.content.Context
import com.example.pawvet_1.data.remote.RetrofitClient
import com.example.pawvet_1.data.repository.BreedsRepository
import com.example.pawvet_1.data.repository.CitaRepository
import com.example.pawvet_1.data.repository.MascotaRepository
import com.example.pawvet_1.data.repository.ServicioRepository
import com.example.pawvet_1.data.repository.AuthRepository
import com.example.pawvet_1.data.repository.GeminiRepository
import com.example.pawvet_1.notifications.NotificationHelper
import com.google.firebase.auth.FirebaseAuth

/**
 * Contenedor de dependencias a nivel de aplicación.
 */
interface AppContainer {
    val authRepository: AuthRepository
    val mascotaRepository: MascotaRepository
    val breedsRepository: BreedsRepository
    val citaRepository: CitaRepository
    val servicioRepository: ServicioRepository
    val geminiRepository: GeminiRepository
    val notificationHelper: NotificationHelper
}

/**
 * Implementación del contenedor que provee las instancias de los repositorios.
 */
class AppDataContainer(private val context: Context) : AppContainer {
    
    // Lazy property para la base de datos
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val database: PawVetDatabase by lazy {
        PawVetDatabase.getDatabase(context)
    }

    // Repositorio para Mascotas (Room)
    override val mascotaRepository: MascotaRepository by lazy {
        MascotaRepository(database.mascotaDao())
    }

    // Repositorio para Razas (Retrofit)
    override val breedsRepository: BreedsRepository by lazy {
        BreedsRepository(RetrofitClient.dogApiService)
    }

    // Repositorio para Citas (Room)
    override val citaRepository: CitaRepository by lazy {
        CitaRepository(database.citaDao())
    }

    // Repositorio para Servicios (Room)
    override val servicioRepository: ServicioRepository by lazy {
        ServicioRepository(database.servicioDao())
    }
}
