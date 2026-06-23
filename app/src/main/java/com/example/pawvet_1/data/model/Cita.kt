package com.example.pawvet_1.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * [ROOM - ENTITY]
 * - idFirestore: Para sincronizar con Firebase.
 * - usuarioId: Para filtrar las citas por dueño.
 */
@Entity(tableName = "citas")
data class Cita(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idFirestore: String = "", 
    val usuarioId: String = "",    // UID del dueño
    val mascotaId: Int,
    val fecha: String,
    val hora: String,
    val tipo: String
)
