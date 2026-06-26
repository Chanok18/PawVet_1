package com.example.pawvet_1.ui.notifications

import android.util.Log
import com.example.pawvet_1.PawVetApplication
import com.example.pawvet_1.navigation.Screen
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class PawVetFirebaseMessagingService : FirebaseMessagingService() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("PawVetFCM", "Nuevo token FCM: $token")
        val appContainer = (applicationContext as PawVetApplication).container
        serviceScope.launch {
            runCatching { appContainer.fcmTokenSyncManager.syncTokenForCurrentUser(token) }
                .onFailure { error ->
                    Log.e("PawVetFCM", "No se pudo guardar el nuevo token FCM", error)
                }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title
            ?: message.data["title"]
            ?: "PawVet"
        val body = message.notification?.body
            ?: message.data["body"]
            ?: "Tienes una nueva notificacion."
        val route = message.data["route"] ?: Screen.Perfil.route

        NotificationHelper.showNotification(
            context = applicationContext,
            notificationId = System.currentTimeMillis().toInt(),
            title = title,
            body = body,
            targetRoute = route
        )
    }
}
