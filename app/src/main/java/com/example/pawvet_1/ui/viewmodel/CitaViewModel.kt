package com.example.pawvet_1.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pawvet_1.data.model.Cita
import com.example.pawvet_1.data.repository.CitaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** 
 * [MVVM - VIEWMODEL CITAS]
 * - ESTADO: Controla qué se muestra en la pantalla de citas.
 * - REACTIVIDAD: Usa StateFlow para avisar a la UI cuando hay cambios.
 */
class CitaViewModel(private val repository: CitaRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CitaUiState())
    val uiState: StateFlow<CitaUiState> = _uiState.asStateFlow()

    init { listarCitas() }

    private fun listarCitas() {
        // - CORRUTINA: Busca los datos sin trabar la aplicación.
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // - REAL-TIME: Room nos manda la lista actualizada automáticamente.
            repository.getAllCitas().collect { lista ->
                _uiState.update { it.copy(listaCitas = lista, isLoading = false) }
            }
        }
    }

    fun seleccionarCita(id: Int) {
        viewModelScope.launch {
            val cita = repository.getCitaById(id)
            _uiState.update { it.copy(citaSeleccionada = cita) }
        }
    }

    fun resetSeleccion() {
        _uiState.update { it.copy(citaSeleccionada = null) }
    }

    /**
     * [FLUJO]
     * VISTA (Click) -> VIEWMODEL (Guardar) -> REPOSITORY -> ROOM
     */
    fun guardarCita(id: Int = 0, mascotaId: Int, fecha: String, hora: String, tipo: String) {
        viewModelScope.launch {
            val cita = Cita(id = id, mascotaId = mascotaId, fecha = fecha, hora = hora, tipo = tipo)
            if (id == 0) repository.insertCita(cita) else repository.updateCita(cita)
        }
    }

    fun eliminarCita(cita: Cita) {
        viewModelScope.launch { repository.deleteCita(cita) }
    }
}
