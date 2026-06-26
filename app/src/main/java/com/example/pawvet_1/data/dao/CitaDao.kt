package com.example.pawvet_1.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pawvet_1.data.model.Cita
import kotlinx.coroutines.flow.Flow

@Dao
interface CitaDao {
    @Query("SELECT * FROM citas WHERE userId = :userId ORDER BY fecha ASC")
    fun getAllCitas(userId: String): Flow<List<Cita>>

    @Query("SELECT * FROM citas WHERE mascotaId = :mascotaId")
    fun getCitasByMascota(mascotaId: Int): Flow<List<Cita>>

    @Query("SELECT * FROM citas WHERE id = :id")
    suspend fun getCitaById(id: Int): Cita?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCita(cita: Cita): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(citas: List<Cita>)

    @Update
    suspend fun updateCita(cita: Cita)

    @Delete
    suspend fun deleteCita(cita: Cita)

    @Query("DELETE FROM citas WHERE userId = :userId")
    suspend fun clearByUser(userId: String)

    @Query("DELETE FROM citas")
    suspend fun clearAll()
}
