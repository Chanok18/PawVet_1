package com.example.pawvet_1.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pawvet_1.data.model.Mascota
import kotlinx.coroutines.flow.Flow

@Dao
interface MascotaDao {
    @Query("SELECT * FROM mascotas WHERE userId = :userId ORDER BY nombre ASC")
    fun getAllMascotas(userId: String): Flow<List<Mascota>>

    @Query("SELECT * FROM mascotas WHERE id = :id")
    suspend fun getMascotaById(id: Int): Mascota?

    @Query("SELECT * FROM mascotas WHERE remoteId = :remoteId LIMIT 1")
    suspend fun getMascotaByRemoteId(remoteId: String): Mascota?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMascota(mascota: Mascota): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(mascotas: List<Mascota>)

    @Update
    suspend fun updateMascota(mascota: Mascota)

    @Delete
    suspend fun deleteMascota(mascota: Mascota)

    @Query("DELETE FROM mascotas WHERE userId = :userId")
    suspend fun clearByUser(userId: String)

    @Query("DELETE FROM mascotas")
    suspend fun clearAll()
}
