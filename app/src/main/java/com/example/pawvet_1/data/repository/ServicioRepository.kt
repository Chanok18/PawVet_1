package com.example.pawvet_1.data.repository

import com.example.pawvet_1.data.dao.MascotaDao
import com.example.pawvet_1.data.dao.ServicioDao
import com.example.pawvet_1.data.firebase.RemoteServicioPayload
import com.example.pawvet_1.data.firebase.ServicioRemoteDataSource
import com.example.pawvet_1.data.model.Servicio
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow

class ServicioRepository(
    private val servicioDao: ServicioDao,
    private val mascotaDao: MascotaDao,
    private val auth: FirebaseAuth,
    private val remoteDataSource: ServicioRemoteDataSource
) {
    fun getAllServicios(): Flow<List<Servicio>> = servicioDao.getAllServicios(requireUid())

    fun getServiciosByMascota(mascotaId: Int): Flow<List<Servicio>> = servicioDao.getServiciosByMascota(mascotaId)

    suspend fun getServicioById(id: Int): Servicio? = servicioDao.getServicioById(id)

    suspend fun insertServicio(servicio: Servicio) {
        val uid = requireUid()
        val mascota = mascotaDao.getMascotaById(servicio.mascotaId) ?: return
        val remoteId = remoteDataSource.create(uid, servicio.toRemotePayload(mascota.remoteId))
        servicioDao.insertServicio(
            servicio.copy(
                remoteId = remoteId,
                userId = uid,
                mascotaRemoteId = mascota.remoteId
            )
        )
    }

    suspend fun updateServicio(servicio: Servicio) {
        val uid = requireUid()
        val mascota = mascotaDao.getMascotaById(servicio.mascotaId) ?: return
        val remoteId = servicio.remoteId.ifBlank { return }
        remoteDataSource.update(uid, remoteId, servicio.toRemotePayload(mascota.remoteId))
        servicioDao.updateServicio(
            servicio.copy(
                userId = uid,
                mascotaRemoteId = mascota.remoteId
            )
        )
    }

    suspend fun deleteServicio(servicio: Servicio) {
        val uid = requireUid()
        if (servicio.remoteId.isNotBlank()) {
            remoteDataSource.delete(uid, servicio.remoteId)
        }
        servicioDao.deleteServicio(servicio)
    }

    suspend fun refreshFromCloud() {
        val uid = requireUid()
        val servicios = remoteDataSource.fetchAll(uid).mapNotNull { remoteServicio ->
            val mascotaRemoteId = remoteServicio.mascotaRemoteId
            val mascota = mascotaDao.getMascotaByRemoteId(mascotaRemoteId) ?: return@mapNotNull null
            Servicio(
                remoteId = remoteServicio.remoteId,
                userId = uid,
                mascotaId = mascota.id,
                mascotaRemoteId = mascotaRemoteId,
                tipoServicio = remoteServicio.tipoServicio,
                fecha = remoteServicio.fecha,
                hora = remoteServicio.hora
            )
        }
        servicioDao.clearByUser(uid)
        servicioDao.insertAll(servicios)
    }

    suspend fun clearLocalForUser() {
        auth.currentUser?.uid?.let { servicioDao.clearByUser(it) } ?: servicioDao.clearAll()
    }

    private fun requireUid(): String =
        auth.currentUser?.uid ?: error("No hay usuario autenticado")

    private fun Servicio.toRemotePayload(mascotaRemoteId: String): RemoteServicioPayload = RemoteServicioPayload(
        mascotaRemoteId = mascotaRemoteId,
        tipoServicio = tipoServicio,
        fecha = fecha,
        hora = hora
    )
}
