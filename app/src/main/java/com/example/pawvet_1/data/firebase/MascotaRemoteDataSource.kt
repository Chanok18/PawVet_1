package com.example.pawvet_1.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class RemoteMascotaPayload(
    val nombre: String,
    val tipo: String,
    val raza: String,
    val edad: Int,
    val peso: Double
)

data class RemoteMascotaRecord(
    val remoteId: String,
    val nombre: String,
    val tipo: String,
    val raza: String,
    val edad: Int,
    val peso: Double
)

class MascotaRemoteDataSource(
    private val firestore: FirebaseFirestore
) {
    suspend fun create(uid: String, payload: RemoteMascotaPayload): String {
        return firestore.userMascotasCollection(uid).add(payload.toMap(uid)).await().id
    }

    suspend fun update(uid: String, remoteId: String, payload: RemoteMascotaPayload) {
        firestore.userMascotasCollection(uid).document(remoteId).set(payload.toMap(uid)).await()
    }

    suspend fun delete(uid: String, remoteId: String) {
        firestore.userMascotasCollection(uid).document(remoteId).delete().await()
    }

    suspend fun fetchAll(uid: String): List<RemoteMascotaRecord> {
        return firestore.userMascotasCollection(uid).get().await().documents.map { document ->
            RemoteMascotaRecord(
                remoteId = document.id,
                nombre = document.getString("nombre").orEmpty(),
                tipo = document.getString("tipo").orEmpty().ifBlank { "Perro" },
                raza = document.getString("raza").orEmpty(),
                edad = (document.getLong("edad") ?: 0L).toInt(),
                peso = document.getDouble("peso") ?: 0.0
            )
        }
    }

    private fun RemoteMascotaPayload.toMap(uid: String): Map<String, Any> = mapOf(
        "userId" to uid,
        "nombre" to nombre,
        "tipo" to tipo,
        "raza" to raza,
        "edad" to edad,
        "peso" to peso
    )
}
