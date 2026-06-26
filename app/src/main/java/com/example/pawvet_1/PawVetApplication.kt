package com.example.pawvet_1

import android.app.Application
import android.util.Log
import com.example.pawvet_1.data.AppContainer
import com.example.pawvet_1.data.AppDataContainer
import com.example.pawvet_1.ui.notifications.NotificationHelper
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class PawVetApplication : Application() {
    /**
     * Instancia de AppContainer utilizada por el resto de las clases para obtener dependencias.
     */
    lateinit var container: AppContainer
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        container = AppDataContainer(this)
        NotificationHelper.createNotificationChannel(this)
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                Log.d("PawVetFCM", "Token actual FCM: $token")
                applicationScope.launch {
                    runCatching { container.fcmTokenSyncManager.syncTokenForCurrentUser(token) }
                        .onFailure { error ->
                            Log.e("PawVetFCM", "No se pudo sincronizar el token FCM", error)
                        }
                }
            }
            .addOnFailureListener { error ->
                Log.e("PawVetFCM", "No se pudo obtener el token FCM", error)
            }
    }
}
