package com.example.pawvet_1

import android.app.Application
import com.example.pawvet_1.data.AppContainer
import com.example.pawvet_1.data.AppDataContainer

class PawVetApplication : Application() {

     //Instancia de AppContainer utilizada por el resto de las clases para obtener dependencias.

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
