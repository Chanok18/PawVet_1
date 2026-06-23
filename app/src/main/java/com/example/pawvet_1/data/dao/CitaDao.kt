package com.example.pawvet_1.data.dao

import androidx.room.*
import com.example.pawvet_1.data.model.Cita
import kotlinx.coroutines.flow.Flow

@Dao
interface CitaDao {
    @Query("SELECT * FROM citas WHERE usuarioId = :usuarioId ORDER BY fecha ASC")
    fun getCitasByUser(usuarioId: String): Flow<List<Cita>>

    @Query("SELECT * FROM citas WHERE id = :id")
    suspend fun getCitaById(id: Int): Cita?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCita(cita: Cita)

    @Update
    suspend fun updateCita(cita: Cita)

    @Delete
    suspend fun deleteCita(cita: Cita)
}
