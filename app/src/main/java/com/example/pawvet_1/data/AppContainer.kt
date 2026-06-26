package com.example.pawvet_1.data

import android.content.Context
import com.example.pawvet_1.data.firebase.CitaRemoteDataSource
import com.example.pawvet_1.data.firebase.FcmTokenSyncManager
import com.example.pawvet_1.data.firebase.FirebaseModule
import com.example.pawvet_1.data.firebase.MascotaRemoteDataSource
import com.example.pawvet_1.data.firebase.ServicioRemoteDataSource
import com.example.pawvet_1.data.firebase.UserProfileRemoteDataSource
import com.example.pawvet_1.data.remote.RetrofitClient
import com.example.pawvet_1.data.repository.BreedsRepository
import com.example.pawvet_1.data.repository.CitaRepository
import com.example.pawvet_1.data.repository.MascotaRepository
import com.example.pawvet_1.data.repository.ServicioRepository
import com.example.pawvet_1.data.session.SessionManager

interface AppContainer {
    val mascotaRepository: MascotaRepository
    val breedsRepository: BreedsRepository
    val citaRepository: CitaRepository
    val servicioRepository: ServicioRepository
    val sessionManager: SessionManager
    val fcmTokenSyncManager: FcmTokenSyncManager
}

class AppDataContainer(private val context: Context) : AppContainer {

    private val database: PawVetDatabase by lazy {
        PawVetDatabase.getDatabase(context)
    }

    private val firebaseModule: FirebaseModule by lazy {
        FirebaseModule()
    }

    private val userProfileRemoteDataSource: UserProfileRemoteDataSource by lazy {
        UserProfileRemoteDataSource(firebaseModule.firestore)
    }

    private val mascotaRemoteDataSource: MascotaRemoteDataSource by lazy {
        MascotaRemoteDataSource(firebaseModule.firestore)
    }

    private val citaRemoteDataSource: CitaRemoteDataSource by lazy {
        CitaRemoteDataSource(firebaseModule.firestore)
    }

    private val servicioRemoteDataSource: ServicioRemoteDataSource by lazy {
        ServicioRemoteDataSource(firebaseModule.firestore)
    }

    override val mascotaRepository: MascotaRepository by lazy {
        MascotaRepository(
            mascotaDao = database.mascotaDao(),
            auth = firebaseModule.auth,
            remoteDataSource = mascotaRemoteDataSource
        )
    }

    override val breedsRepository: BreedsRepository by lazy {
        BreedsRepository(RetrofitClient.dogApiService)
    }

    override val citaRepository: CitaRepository by lazy {
        CitaRepository(
            citaDao = database.citaDao(),
            mascotaDao = database.mascotaDao(),
            auth = firebaseModule.auth,
            remoteDataSource = citaRemoteDataSource
        )
    }

    override val servicioRepository: ServicioRepository by lazy {
        ServicioRepository(
            servicioDao = database.servicioDao(),
            mascotaDao = database.mascotaDao(),
            auth = firebaseModule.auth,
            remoteDataSource = servicioRemoteDataSource
        )
    }

    override val sessionManager: SessionManager by lazy {
        SessionManager(
            auth = firebaseModule.auth,
            userProfileRemoteDataSource = userProfileRemoteDataSource,
            fcmTokenSyncManager = fcmTokenSyncManager
        )
    }

    override val fcmTokenSyncManager: FcmTokenSyncManager by lazy {
        FcmTokenSyncManager(
            auth = firebaseModule.auth,
            userProfileRemoteDataSource = userProfileRemoteDataSource
        )
    }
}
