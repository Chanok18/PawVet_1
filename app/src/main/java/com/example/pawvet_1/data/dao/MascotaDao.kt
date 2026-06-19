package com.example.pawvet_1.data.dao

import androidx.room.*
import com.example.pawvet_1.data.model.Mascota
import kotlinx.coroutines.flow.Flow

/**
 * [ROOM - DAO]
 * - CONSULTAS SQL: Define qué hacemos con la DB.
 * - @DAO: Interfaz que Room implementa sola.
 * - FLOW: Retorno reactivo para actualizaciones en vivo.
 */
@Dao
interface MascotaDao {
    @Query("SELECT * FROM mascotas ORDER BY nombre ASC")
    fun getAllMascotas(): Flow<List<Mascota>>

    @Query("SELECT * FROM mascotas WHERE id = :id")
    suspend fun getMascotaById(id: Int): Mascota?

    // - ESTRATEGIA: Replace si hay conflicto (útil en edit).
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMascota(mascota: Mascota)

    @Update
    suspend fun updateMascota(mascota: Mascota)

    @Delete
    suspend fun deleteMascota(mascota: Mascota)
}
