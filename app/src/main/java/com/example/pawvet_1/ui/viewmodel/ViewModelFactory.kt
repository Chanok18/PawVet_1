package com.example.pawvet_1.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pawvet_1.data.repository.*
import com.example.pawvet_1.notifications.NotificationHelper

/**
 * Fábrica para instanciar ViewModels con sus respectivos repositorios.
 * Soporta inyección de dependencias para Firebase y Notificaciones.
 */
class ViewModelFactory(
    private val repository: Any,
    private val extraRepository: Any? = null,
    private val notificationHelper: NotificationHelper? = null
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(repository as AuthRepository) as T
            }
            modelClass.isAssignableFrom(MascotaViewModel::class.java) -> {
                MascotaViewModel(repository as MascotaRepository) as T
            }
            modelClass.isAssignableFrom(BreedsViewModel::class.java) -> {
                BreedsViewModel(repository as BreedsRepository) as T
            }
            modelClass.isAssignableFrom(CitaViewModel::class.java) -> {
                println("DEBUG notificationHelper = $notificationHelper")
                CitaViewModel(repository as CitaRepository, notificationHelper!!) as T
            }
            modelClass.isAssignableFrom(ServicioViewModel::class.java) -> {
                ServicioViewModel(repository as ServicioRepository) as T
            }
            modelClass.isAssignableFrom(ConsultaIaViewModel::class.java) -> {
                ConsultaIaViewModel(repository as MascotaRepository, extraRepository as GeminiRepository) as T
            }
            else -> throw IllegalArgumentException("Clase ViewModel desconocida: ${modelClass.name}")
        }
    }
}
