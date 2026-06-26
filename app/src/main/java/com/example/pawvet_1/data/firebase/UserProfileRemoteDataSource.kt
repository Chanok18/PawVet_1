package com.example.pawvet_1.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class RemoteUserProfile(
    val name: String = "",
    val email: String = "",
    val fcmToken: String = ""
)

class UserProfileRemoteDataSource(
    private val firestore: FirebaseFirestore
) {
    suspend fun createOrUpdateProfile(uid: String, name: String, email: String) {
        firestore.userDocument(uid).set(
            mapOf(
                "name" to name,
                "email" to email
            ),
            com.google.firebase.firestore.SetOptions.merge()
        ).await()
    }

    suspend fun getProfile(uid: String): RemoteUserProfile? {
        val document = firestore.userDocument(uid).get().await()
        if (!document.exists()) return null

        return RemoteUserProfile(
            name = document.getString("name").orEmpty(),
            email = document.getString("email").orEmpty(),
            fcmToken = document.getString("fcmToken").orEmpty()
        )
    }

    suspend fun updateFcmToken(uid: String, token: String) {
        firestore.userDocument(uid).set(
            mapOf("fcmToken" to token),
            com.google.firebase.firestore.SetOptions.merge()
        ).await()
    }

    suspend fun clearFcmToken(uid: String) {
        firestore.userDocument(uid).set(
            mapOf("fcmToken" to com.google.firebase.firestore.FieldValue.delete()),
            com.google.firebase.firestore.SetOptions.merge()
        ).await()
    }
}
