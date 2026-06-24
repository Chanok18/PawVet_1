package com.example.pawvet_1.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pawvet_1.data.dao.CitaDao
import com.example.pawvet_1.data.dao.MascotaDao
import com.example.pawvet_1.data.dao.ServicioDao
import com.example.pawvet_1.data.model.Cita
import com.example.pawvet_1.data.model.Mascota
import com.example.pawvet_1.data.model.Servicio

@Database(
    entities = [Mascota::class, Cita::class, Servicio::class], 
    version = 6, // Incrementamos a 5 para las nuevas columnas de Firebase
    exportSchema = false
)
abstract class PawVetDatabase : RoomDatabase() {

    abstract fun mascotaDao(): MascotaDao
    abstract fun citaDao(): CitaDao
    abstract fun servicioDao(): ServicioDao

    companion object {
        @Volatile
        private var Instance: PawVetDatabase? = null

        fun getDatabase(context: Context): PawVetDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, PawVetDatabase::class.java, "pawvet_db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
