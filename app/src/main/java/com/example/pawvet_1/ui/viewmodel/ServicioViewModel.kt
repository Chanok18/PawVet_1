package com.example.pawvet_1.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pawvet_1.data.model.Servicio
import com.example.pawvet_1.data.repository.ServicioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar la lógica de negocio de los Servicios (Baño, Corte, etc).
 */
class ServicioViewModel(private val repository: ServicioRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ServicioUiState())
    val uiState: StateFlow<ServicioUiState> = _uiState.asStateFlow()

    init {
        listarServicios()
    }

    private fun listarServicios() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getAllServicios().collect { lista ->
                _uiState.update { it.copy(listaServicios = lista, isLoading = false) }
            }
        }
    }

    fun seleccionarServicio(id: Int) {
        viewModelScope.launch {
            val servicio = repository.getServicioById(id)
            _uiState.update { it.copy(servicioSeleccionado = servicio) }
        }
    }

    fun guardarServicio(id: Int = 0, mascotaId: Int, tipo: String, fecha: String, hora: String) {
        viewModelScope.launch {
            val servicio = Servicio(id = id, mascotaId = mascotaId, tipoServicio = tipo, fecha = fecha, hora = hora)
            if (id == 0) {
                repository.insertServicio(servicio)
            } else {
                repository.updateServicio(servicio)
            }
        }
    }

    fun eliminarServicio(servicio: Servicio) {
        viewModelScope.launch {
            repository.deleteServicio(servicio)
        }
    }
}
