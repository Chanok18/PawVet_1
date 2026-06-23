package com.example.pawvet_1.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pawvet_1.data.model.Mascota
import com.example.pawvet_1.data.repository.MascotaRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * VIEWMODEL MASCOTA: Maneja la lógica filtrada por el usuario autenticado.
 */
class MascotaViewModel(private val repository: MascotaRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(MascotaUiState())
    val uiState: StateFlow<MascotaUiState> = _uiState.asStateFlow()

    private var currentListingJob: Job? = null

    /**
     * Inicia la observación de mascotas para el usuario.
     */
    fun listarMascotas() {
        currentListingJob?.cancel()
        currentListingJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                repository.sincronizarDesdeFirestore()
            } catch (e: Exception) {
                // Error de red ignorado
            }

            // REPARADO: getMascotasByUser() ya no requiere parámetros (usa el ID interno del repo)
            repository.getMascotasByUser().collect { lista ->
                _uiState.update { it.copy(listaMascotas = lista, isLoading = false) }
            }
        }
    }

    fun seleccionarMascota(id: Int) {
        viewModelScope.launch {
            val mascota = repository.getMascotaById(id)
            _uiState.update { it.copy(mascotaSeleccionada = mascota) }
        }
    }

    fun resetSeleccion() {
        _uiState.update { it.copy(mascotaSeleccionada = null) }
    }

    fun guardarMascota(id: Int = 0, nombre: String, tipo: String, raza: String, edad: Int, peso: Double) {
        viewModelScope.launch {
            val mascota = Mascota(
                id = id, 
                nombre = nombre, 
                tipo = tipo, 
                raza = raza, 
                edad = edad, 
                peso = peso,
                idFirestore = _uiState.value.mascotaSeleccionada?.idFirestore ?: ""
            )

            if (id == 0) {
                repository.insertMascota(mascota)
            } else {
                repository.updateMascota(mascota)
            }
        }
    }

    fun eliminarMascota(mascota: Mascota) {
        viewModelScope.launch {
            repository.deleteMascota(mascota)
        }
    }
}
