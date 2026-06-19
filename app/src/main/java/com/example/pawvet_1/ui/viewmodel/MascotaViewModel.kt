package com.example.pawvet_1.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pawvet_1.data.model.Mascota
import com.example.pawvet_1.data.repository.MascotaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * [MVVM - VIEWMODEL]
 * - CEREBRO: Maneja la lógica.
 * - SOBREVIVE: No muere al rotar pantalla.
 * - DESACOPLADO: No toca la DB directo.
 */
class MascotaViewModel(private val repository: MascotaRepository) : ViewModel() {

    // [ESTADO - UI STATE]
    // - MUTABLE: Privado, solo yo lo cambio.
    private val _uiState = MutableStateFlow(MascotaUiState())
    
    // - REACTIVO: La vista lo observa (StateFlow).
    val uiState: StateFlow<MascotaUiState> = _uiState.asStateFlow()

    init {
        listarMascotas()
    }

    private fun listarMascotas() {
        // [CORRUTINAS]
        // - HILO SECUNDARIO: No congela la app.
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // [FLOW]
            // - TIEMPO REAL: Si la DB cambia, la UI se actualiza sola.
            repository.getAllMascotas().collect { lista ->
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

    /**
     * [FLUJO DEL DATO]
     * VIEW -> VIEWMODEL -> REPOSITORY -> ROOM (DB)
     */
    fun guardarMascota(id: Int = 0, nombre: String, tipo: String, raza: String, edad: Int, peso: Double) {
        viewModelScope.launch {
            val mascota = Mascota(id = id, nombre = nombre, tipo = tipo, raza = raza, edad = edad, peso = peso)
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
