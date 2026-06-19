package com.example.pawvet_1.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pawvet_1.data.repository.BreedsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * [API/RETROFIT]
 * - CONSUMO REMOTO: Traemos datos de internet (Dog API).
 * - MANEJO DE ESTADOS: Loading (Cargando), Success (Éxito), Error (Falla).
 * - TRY-CATCH: Captura errores (Ej: No hay internet).
 */
sealed class ImagesUiState {
    object Loading : ImagesUiState()
    data class Success(val images: List<String>) : ImagesUiState()
    data class Error(val message: String) : ImagesUiState()
}

class BreedsViewModel(private val repository: BreedsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<BreedsUiState>(BreedsUiState.Loading)
    val uiState: StateFlow<BreedsUiState> = _uiState.asStateFlow()

    private val _imagesUiState = MutableStateFlow<ImagesUiState>(ImagesUiState.Loading)
    val imagesUiState: StateFlow<ImagesUiState> = _imagesUiState.asStateFlow()

    init {
        fetchBreeds()
        fetchServiceImages()
    }

    fun fetchBreeds() {
        viewModelScope.launch {
            _uiState.value = BreedsUiState.Loading
            try {
                val breeds = repository.getBreeds()
                _uiState.value = BreedsUiState.Success(breeds)
            } catch (e: Exception) {
                // - MANEJO DE ERROR: Si falla la red, avisamos a la UI.
                _uiState.value = BreedsUiState.Error("Error al cargar razas: ${e.message}")
            }
        }
    }

    fun fetchServiceImages() {
        viewModelScope.launch {
            _imagesUiState.value = ImagesUiState.Loading
            try {
                val images = repository.getServiceImages()
                _imagesUiState.value = ImagesUiState.Success(images)
            } catch (e: Exception) {
                _imagesUiState.value = ImagesUiState.Error("Error al cargar estilos: ${e.message}")
            }
        }
    }
}
