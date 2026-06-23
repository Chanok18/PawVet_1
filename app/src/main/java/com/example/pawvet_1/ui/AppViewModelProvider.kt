package com.example.pawvet_1.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pawvet_1.PawVetApplication
import com.example.pawvet_1.ui.viewmodel.AuthViewModel
import com.example.pawvet_1.ui.viewmodel.BreedsViewModel
import com.example.pawvet_1.ui.viewmodel.CitaViewModel
import com.example.pawvet_1.ui.viewmodel.MascotaViewModel
import com.example.pawvet_1.ui.viewmodel.ServicioViewModel

/**
 * Proveedor global de ViewModels con inyección de dependencias.
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            AuthViewModel(pawVetApplication().container.authRepository)
        }
        
        initializer {
            MascotaViewModel(pawVetApplication().container.mascotaRepository)
        }

        initializer {
            BreedsViewModel(pawVetApplication().container.breedsRepository)
        }

        initializer {
            CitaViewModel(
                pawVetApplication().container.citaRepository,
                pawVetApplication().container.notificationHelper
            )
        }

        initializer {
            ServicioViewModel(pawVetApplication().container.servicioRepository)
        }
    }
}

fun CreationExtras.pawVetApplication(): PawVetApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PawVetApplication)
