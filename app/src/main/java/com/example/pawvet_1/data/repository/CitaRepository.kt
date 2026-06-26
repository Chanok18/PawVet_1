package com.example.pawvet_1.data.repository

import com.example.pawvet_1.data.dao.CitaDao
import com.example.pawvet_1.data.dao.MascotaDao
import com.example.pawvet_1.data.firebase.CitaRemoteDataSource
import com.example.pawvet_1.data.firebase.RemoteCitaPayload
import com.example.pawvet_1.data.model.Cita
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow

class CitaRepository(
    private val citaDao: CitaDao,
    private val mascotaDao: MascotaDao,
    private val auth: FirebaseAuth,
    private val remoteDataSource: CitaRemoteDataSource
) {
    fun getAllCitas(): Flow<List<Cita>> = citaDao.getAllCitas(requireUid())

    fun getCitasByMascota(mascotaId: Int): Flow<List<Cita>> = citaDao.getCitasByMascota(mascotaId)

    suspend fun getCitaById(id: Int): Cita? = citaDao.getCitaById(id)

    suspend fun insertCita(cita: Cita) {
        val uid = requireUid()
        val mascota = mascotaDao.getMascotaById(cita.mascotaId) ?: return
        val remoteId = remoteDataSource.create(uid, cita.toRemotePayload(mascota.remoteId))
        citaDao.insertCita(
            cita.copy(
                remoteId = remoteId,
                userId = uid,
                mascotaRemoteId = mascota.remoteId
            )
        )
    }

    suspend fun updateCita(cita: Cita) {
        val uid = requireUid()
        val mascota = mascotaDao.getMascotaById(cita.mascotaId) ?: return
        val remoteId = cita.remoteId.ifBlank { return }
        remoteDataSource.update(uid, remoteId, cita.toRemotePayload(mascota.remoteId))
        citaDao.updateCita(
            cita.copy(
                userId = uid,
                mascotaRemoteId = mascota.remoteId
            )
        )
    }

    suspend fun deleteCita(cita: Cita) {
        val uid = requireUid()
        if (cita.remoteId.isNotBlank()) {
            remoteDataSource.delete(uid, cita.remoteId)
        }
        citaDao.deleteCita(cita)
    }

    suspend fun refreshFromCloud() {
        val uid = requireUid()
        val citas = remoteDataSource.fetchAll(uid).mapNotNull { remoteCita ->
            val mascotaRemoteId = remoteCita.mascotaRemoteId
            val mascota = mascotaDao.getMascotaByRemoteId(mascotaRemoteId) ?: return@mapNotNull null
            Cita(
                remoteId = remoteCita.remoteId,
                userId = uid,
                mascotaId = mascota.id,
                mascotaRemoteId = mascotaRemoteId,
                fecha = remoteCita.fecha,
                hora = remoteCita.hora,
                tipo = remoteCita.tipo
            )
        }
        citaDao.clearByUser(uid)
        citaDao.insertAll(citas)
    }

    suspend fun clearLocalForUser() {
        auth.currentUser?.uid?.let { citaDao.clearByUser(it) } ?: citaDao.clearAll()
    }

    private fun requireUid(): String =
        auth.currentUser?.uid ?: error("No hay usuario autenticado")

    private fun Cita.toRemotePayload(mascotaRemoteId: String): RemoteCitaPayload = RemoteCitaPayload(
        mascotaRemoteId = mascotaRemoteId,
        fecha = fecha,
        hora = hora,
        tipo = tipo
    )
}
