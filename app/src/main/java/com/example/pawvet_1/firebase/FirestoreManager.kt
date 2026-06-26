package com.example.pawvet_1.firebase

import com.google.firebase.firestore.FirebaseFirestore

object FirestoreManager {

    private val db = FirebaseFirestore.getInstance()

    fun guardarToken(uid: String, token: String) {

        db.collection("usuarios")
            .document(uid)
            .set(
                mapOf("fcmToken" to token),
                com.google.firebase.firestore.SetOptions.merge()
            )

    }
}