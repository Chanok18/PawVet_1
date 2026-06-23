package com.example.pawvet_1.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pawvet_1.data.model.Cita
import com.example.pawvet_1.data.repository.CitaRepository
import com.example.pawvet_1.notifications.NotificationHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CitaViewModel(
    private val repository: CitaRepository,
    private val notificationHelper: NotificationHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(CitaUiState())
    val uiState: StateFlow<CitaUiState> = _uiState.asStateFlow()

    private var currentListingJob: Job? = null

    /**
     * Carga las citas filtradas por el usuario actual.
     */
    fun listarCitas() {
        currentListingJob?.cancel()
        currentListingJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                repository.sincronizarDesdeFirestore()
            } catch (e: Exception) { }

            // Corregido: Pasamos el usuarioId al repositorio
            repository.getCitasByUser().collect { lista ->
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

    fun guardarCita(id: Int = 0, mascotaId: Int, fecha: String, hora: String, tipo: String) {
        viewModelScope.launch {
            val cita = Cita(
                id = id, 
                mascotaId = mascotaId, 
                fecha = fecha, 
                hora = hora, 
                tipo = tipo,
                idFirestore = _uiState.value.citaSeleccionada?.idFirestore ?: ""
            )

            if (id == 0) repository.insertCita(cita) else repository.updateCita(cita)
            
            notificationHelper.programarRecordatorio(
                citaId = if (id == 0) System.currentTimeMillis().toInt() else id,
                fecha = fecha,
                hora = hora,
                tipo = tipo
            )
        }
    }

    fun eliminarCita(cita: Cita) {
        viewModelScope.launch { repository.deleteCita(cita) }
    }
}
