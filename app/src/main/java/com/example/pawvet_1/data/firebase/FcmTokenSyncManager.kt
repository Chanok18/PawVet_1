package com.example.pawvet_1.data.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class FcmTokenSyncManager(
    private val auth: FirebaseAuth,
    private val userProfileRemoteDataSource: UserProfileRemoteDataSource
) {
    suspend fun syncCurrentTokenIfPossible() {
        val uid = auth.currentUser?.uid ?: return
        val token = FirebaseMessaging.getInstance().token.await()
        userProfileRemoteDataSource.updateFcmToken(uid, token)
        Log.d("PawVetFCM", "Token FCM sincronizado para uid=$uid")
    }

    suspend fun syncTokenForCurrentUser(token: String) {
        val uid = auth.currentUser?.uid ?: return
        userProfileRemoteDataSource.updateFcmToken(uid, token)
        Log.d("PawVetFCM", "Nuevo token FCM guardado para uid=$uid")
    }

    suspend fun clearCurrentUserToken() {
        val uid = auth.currentUser?.uid ?: return
        userProfileRemoteDataSource.clearFcmToken(uid)
        Log.d("PawVetFCM", "Token FCM eliminado para uid=$uid")
    }
}
