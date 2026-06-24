
package com.example.pawvet_1.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "servicios")
data class Servicio(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val mascotaId: Int,
    val tipoServicio: String,
    val fecha: String,
    val hora: String
)
