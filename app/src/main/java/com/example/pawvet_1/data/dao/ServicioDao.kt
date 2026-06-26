package com.example.pawvet_1.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pawvet_1.data.model.Servicio
import kotlinx.coroutines.flow.Flow

@Dao
interface ServicioDao {
    @Query("SELECT * FROM servicios WHERE userId = :userId ORDER BY fecha DESC")
    fun getAllServicios(userId: String): Flow<List<Servicio>>

    @Query("SELECT * FROM servicios WHERE mascotaId = :mascotaId")
    fun getServiciosByMascota(mascotaId: Int): Flow<List<Servicio>>

    @Query("SELECT * FROM servicios WHERE id = :id")
    suspend fun getServicioById(id: Int): Servicio?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServicio(servicio: Servicio): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(servicios: List<Servicio>)

    @Update
    suspend fun updateServicio(servicio: Servicio)

    @Delete
    suspend fun deleteServicio(servicio: Servicio)

    @Query("DELETE FROM servicios WHERE userId = :userId")
    suspend fun clearByUser(userId: String)

    @Query("DELETE FROM servicios")
    suspend fun clearAll()
}
