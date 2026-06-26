package com.example.pawvet_1.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "citas")
data class Cita(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val remoteId: String = "",
    val userId: String = "",
    val mascotaId: Int,
    val mascotaRemoteId: String = "",
    val fecha: String,
    val hora: String,
    val tipo: String
)
