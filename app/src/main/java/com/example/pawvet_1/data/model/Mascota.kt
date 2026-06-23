package com.example.pawvet_1.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mascotas")
data class Mascota(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idFirestore: String = "",
    val usuarioId: String = "",
    val nombre: String = "",
    val tipo: String = "Perro",
    val raza: String = "",
    val edad: Int = 0,
    val peso: Double = 0.0
)
