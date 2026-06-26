package com.example.pawvet_1.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * [ROOM - ENTITY]
 * - TABLA: Representa la tabla "mascotas" en SQLite.
 * - PRIMARY KEY: ID auto-generado para identificar cada fila.
 * - MODELO: Clase de datos pura (POJO).
 */
@Entity(tableName = "mascotas")
data class Mascota(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val remoteId: String = "",
    val userId: String = "",
    val nombre: String,
    val tipo: String = "Perro",
    val raza: String,
    val edad: Int,
    val peso: Double
)
