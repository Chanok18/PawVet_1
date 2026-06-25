package com.example.pawvet_1.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pawvet_1.data.model.Mascota
import com.example.pawvet_1.data.repository.GeminiRepository
import com.example.pawvet_1.data.repository.MascotaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ConsultaUiState(
    val respuesta: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class ConsultaIaViewModel(
    private val mascotaRepository: MascotaRepository,
    private val geminiRepository: GeminiRepository
) : ViewModel() {

    var uiState by mutableStateOf(ConsultaUiState())
        private set

    val mascotas: StateFlow<List<Mascota>> = mascotaRepository.getMascotasByUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    var consultaTexto by mutableStateOf("")
        private set

    var mascotaSeleccionada by mutableStateOf<Mascota?>(null)
        private set

    fun onConsultaChange(nuevaConsulta: String) {
        consultaTexto = nuevaConsulta
    }

    fun onMascotaSelected(mascota: Mascota) {
        mascotaSeleccionada = mascota
    }

    fun consultarIA() {
        val mascota = mascotaSeleccionada
        if (mascota == null) {
            uiState = uiState.copy(error = "Por favor, selecciona una mascota")
            return
        }
        if (consultaTexto.isBlank()) {
            uiState = uiState.copy(error = "Por favor, escribe tu consulta")
            return
        }

        uiState = uiState.copy(isLoading = true, error = null)

        val prompt = """
            Eres un asistente veterinario virtual de PawVet.
            Tu función es responder consultas básicas sobre mascotas de forma educativa y preventiva.
            
            Datos de la mascota:
            Nombre: ${mascota.nombre}
            Tipo: ${mascota.tipo}
            Raza: ${mascota.raza}
            Edad: ${mascota.edad} años
            Peso: ${mascota.peso} kg
            
            Consulta del propietario:
            $consultaTexto
            
            Reglas:
            * Responde de forma breve y clara.
            * Usa lenguaje sencillo.
            * Máximo 150 palabras.
            * No inventes diagnósticos.
            * No recetes medicamentos.
            * No reemplaces a un veterinario profesional.
            * Si detectas síntomas potencialmente graves, recomienda acudir a una clínica veterinaria.
            * Considera la raza, edad y peso al responder.
            * Responde siempre en español.
        """.trimIndent()

        viewModelScope.launch {
            try {
                val resultado = geminiRepository.getAiResponse(prompt)
                uiState = uiState.copy(respuesta = resultado, isLoading = false)
            } catch (e: Exception) {
                e.printStackTrace()
                uiState = uiState.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    fun limpiarRespuesta() {
        uiState = ConsultaUiState()
        consultaTexto = ""
    }
}
