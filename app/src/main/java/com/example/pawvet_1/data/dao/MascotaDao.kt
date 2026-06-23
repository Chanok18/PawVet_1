package com.example.pawvet_1.data.dao

import androidx.room.*
import com.example.pawvet_1.data.model.Mascota
import kotlinx.coroutines.flow.Flow

/**
 * [ROOM - DAO]
 * Actualizado para filtrar mascotas por el ID del usuario autenticado.
 */
@Dao
interface MascotaDao {

    @Query("SELECT * FROM mascotas WHERE usuarioId = :usuarioId ORDER BY nombre ASC")
    fun getMascotasByUser(usuarioId: String): Flow<List<Mascota>>

    @Query("SELECT * FROM mascotas WHERE id = :id")
    suspend fun getMascotaById(id: Int): Mascota?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMascota(mascota: Mascota)

    @Update
    suspend fun updateMascota(mascota: Mascota)

    @Delete
    suspend fun deleteMascota(mascota: Mascota)

    @Query("DELETE FROM mascotas")
    suspend fun deleteAll()

    @Query("DELETE FROM mascotas WHERE usuarioId = :usuarioId")
    suspend fun deleteMascotasByUser(usuarioId: String)
}