package com.example.pawvet_1.data.repository

import com.example.pawvet_1.data.dao.MascotaDao
import com.example.pawvet_1.data.firebase.MascotaRemoteDataSource
import com.example.pawvet_1.data.firebase.RemoteMascotaPayload
import com.example.pawvet_1.data.model.Mascota
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow

class MascotaRepository(
    private val mascotaDao: MascotaDao,
    private val auth: FirebaseAuth,
    private val remoteDataSource: MascotaRemoteDataSource
) {

    fun getAllMascotas(): Flow<List<Mascota>> = mascotaDao.getAllMascotas(requireUid())

    suspend fun getMascotaById(id: Int): Mascota? = mascotaDao.getMascotaById(id)

    suspend fun insertMascota(mascota: Mascota) {
        val uid = requireUid()
        val remoteId = remoteDataSource.create(uid, mascota.toRemotePayload())
        mascotaDao.insertMascota(
            mascota.copy(
                remoteId = remoteId,
                userId = uid
            )
        )
    }

    suspend fun updateMascota(mascota: Mascota) {
        val uid = requireUid()
        val remoteId = mascota.remoteId.ifBlank { return }
        remoteDataSource.update(uid, remoteId, mascota.toRemotePayload())
        mascotaDao.updateMascota(mascota.copy(userId = uid))
    }

    suspend fun deleteMascota(mascota: Mascota) {
        val uid = requireUid()
        if (mascota.remoteId.isNotBlank()) {
            remoteDataSource.delete(uid, mascota.remoteId)
        }
        mascotaDao.deleteMascota(mascota)
    }

    suspend fun refreshFromCloud() {
        val uid = requireUid()
        val mascotas = remoteDataSource.fetchAll(uid).map { remoteMascota ->
            Mascota(
                remoteId = remoteMascota.remoteId,
                userId = uid,
                nombre = remoteMascota.nombre,
                tipo = remoteMascota.tipo,
                raza = remoteMascota.raza,
                edad = remoteMascota.edad,
                peso = remoteMascota.peso
            )
        }
        mascotaDao.clearByUser(uid)
        mascotaDao.insertAll(mascotas)
    }

    suspend fun clearLocalForUser() {
        auth.currentUser?.uid?.let { mascotaDao.clearByUser(it) } ?: mascotaDao.clearAll()
    }

    private fun requireUid(): String =
        auth.currentUser?.uid ?: error("No hay usuario autenticado")

    private fun Mascota.toRemotePayload(): RemoteMascotaPayload = RemoteMascotaPayload(
        nombre = nombre,
        tipo = tipo,
        raza = raza,
        edad = edad,
        peso = peso
    )
}
