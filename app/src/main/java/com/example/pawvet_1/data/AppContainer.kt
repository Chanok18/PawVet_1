package com.example.pawvet_1.data

import android.content.Context
import com.example.pawvet_1.data.remote.GeminiRetrofitClient
import com.example.pawvet_1.data.remote.RetrofitClient
import com.example.pawvet_1.data.repository.AuthRepository
import com.example.pawvet_1.data.repository.BreedsRepository
import com.example.pawvet_1.data.repository.CitaRepository
import com.example.pawvet_1.data.repository.GeminiRepository
import com.example.pawvet_1.data.repository.MascotaRepository
import com.example.pawvet_1.data.repository.ServicioRepository
import com.example.pawvet_1.notifications.NotificationHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Contenedor de dependencias actualizado para la Etapa 2.
 */
interface AppContainer {
    val authRepository: AuthRepository
    val mascotaRepository: MascotaRepository
    val breedsRepository: BreedsRepository
    val citaRepository: CitaRepository
    val servicioRepository: ServicioRepository
    val notificationHelper: NotificationHelper
    val geminiRepository: GeminiRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val database: PawVetDatabase by lazy {
        PawVetDatabase.getDatabase(context)
    }

    override val authRepository: AuthRepository by lazy {
        AuthRepository(firebaseAuth)
    }

    override val mascotaRepository: MascotaRepository by lazy {
        MascotaRepository(database.mascotaDao(), firestore, firebaseAuth)
    }

    override val breedsRepository: BreedsRepository by lazy {
        BreedsRepository(RetrofitClient.dogApiService)
    }

    override val citaRepository: CitaRepository by lazy {
        CitaRepository(database.citaDao(), firestore, firebaseAuth)
    }

    override val servicioRepository: ServicioRepository by lazy {
        ServicioRepository(database.servicioDao())
    }

    // Nuevo: Helper para notificaciones
    override val notificationHelper: NotificationHelper by lazy {
        NotificationHelper(context)
    }

    override val geminiRepository: GeminiRepository by lazy {
        GeminiRepository(GeminiRetrofitClient.geminiApiService)
    }
}
