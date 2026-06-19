package com.example.pawvet_1.data.repository

import com.example.pawvet_1.data.dao.MascotaDao
import com.example.pawvet_1.data.model.Mascota
import kotlinx.coroutines.flow.Flow

/**
 * REPOSITORY:
 * - "MEDIADOR": Une el ViewModel con la base de datos.
 * - "CAPA DE DATOS": Aquí se decide de dónde vienen los datos.
 * - "LIMPIEZA": El ViewModel no ve el DAO directamente, pasa por aquí.
 */
class MascotaRepository(private val mascotaDao: MascotaDao) {
    
    fun getAllMascotas(): Flow<List<Mascota>> = mascotaDao.getAllMascotas()

    suspend fun getMascotaById(id: Int): Mascota? = mascotaDao.getMascotaById(id)

    suspend fun insertMascota(mascota: Mascota) = mascotaDao.insertMascota(mascota)

    suspend fun updateMascota(mascota: Mascota) = mascotaDao.updateMascota(mascota)

    suspend fun deleteMascota(mascota: Mascota) = mascotaDao.deleteMascota(mascota)
}
