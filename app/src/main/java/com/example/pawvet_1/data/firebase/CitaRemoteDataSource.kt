package com.example.pawvet_1.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class RemoteCitaPayload(
    val mascotaRemoteId: String,
    val fecha: String,
    val hora: String,
    val tipo: String
)

data class RemoteCitaRecord(
    val remoteId: String,
    val mascotaRemoteId: String,
    val fecha: String,
    val hora: String,
    val tipo: String
)

class CitaRemoteDataSource(
    private val firestore: FirebaseFirestore
) {
    suspend fun create(uid: String, payload: RemoteCitaPayload): String {
        return firestore.userCitasCollection(uid).add(payload.toMap(uid)).await().id
    }

    suspend fun update(uid: String, remoteId: String, payload: RemoteCitaPayload) {
        firestore.userCitasCollection(uid).document(remoteId).set(payload.toMap(uid)).await()
    }

    suspend fun delete(uid: String, remoteId: String) {
        firestore.userCitasCollection(uid).document(remoteId).delete().await()
    }

    suspend fun fetchAll(uid: String): List<RemoteCitaRecord> {
        return firestore.userCitasCollection(uid).get().await().documents.map { document ->
            RemoteCitaRecord(
                remoteId = document.id,
                mascotaRemoteId = document.getString("mascotaRemoteId").orEmpty(),
                fecha = document.getString("fecha").orEmpty(),
                hora = document.getString("hora").orEmpty(),
                tipo = document.getString("tipo").orEmpty()
            )
        }
    }

    private fun RemoteCitaPayload.toMap(uid: String): Map<String, Any> = mapOf(
        "userId" to uid,
        "mascotaRemoteId" to mascotaRemoteId,
        "fecha" to fecha,
        "hora" to hora,
        "tipo" to tipo
    )
}
