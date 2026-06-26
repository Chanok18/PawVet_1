package com.example.pawvet_1.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class RemoteServicioPayload(
    val mascotaRemoteId: String,
    val tipoServicio: String,
    val fecha: String,
    val hora: String
)

data class RemoteServicioRecord(
    val remoteId: String,
    val mascotaRemoteId: String,
    val tipoServicio: String,
    val fecha: String,
    val hora: String
)

class ServicioRemoteDataSource(
    private val firestore: FirebaseFirestore
) {
    suspend fun create(uid: String, payload: RemoteServicioPayload): String {
        return firestore.userServiciosCollection(uid).add(payload.toMap(uid)).await().id
    }

    suspend fun update(uid: String, remoteId: String, payload: RemoteServicioPayload) {
        firestore.userServiciosCollection(uid).document(remoteId).set(payload.toMap(uid)).await()
    }

    suspend fun delete(uid: String, remoteId: String) {
        firestore.userServiciosCollection(uid).document(remoteId).delete().await()
    }

    suspend fun fetchAll(uid: String): List<RemoteServicioRecord> {
        return firestore.userServiciosCollection(uid).get().await().documents.map { document ->
            RemoteServicioRecord(
                remoteId = document.id,
                mascotaRemoteId = document.getString("mascotaRemoteId").orEmpty(),
                tipoServicio = document.getString("tipoServicio").orEmpty(),
                fecha = document.getString("fecha").orEmpty(),
                hora = document.getString("hora").orEmpty()
            )
        }
    }

    private fun RemoteServicioPayload.toMap(uid: String): Map<String, Any> = mapOf(
        "userId" to uid,
        "mascotaRemoteId" to mascotaRemoteId,
        "tipoServicio" to tipoServicio,
        "fecha" to fecha,
        "hora" to hora
    )
}
